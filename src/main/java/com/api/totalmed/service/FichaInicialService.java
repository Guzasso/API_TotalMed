package com.api.totalmed.service;

import com.api.totalmed.Enum.Motivo;
import com.api.totalmed.Enum.Status;
import com.api.totalmed.dto.FichaInicialRequestDTO;
import com.api.totalmed.exception.BusinessException;
import com.api.totalmed.exception.ResourceNotFoundException;
import com.api.totalmed.model.Clinica;
import com.api.totalmed.model.FichaInicial;
import com.api.totalmed.model.Paciente;
import com.api.totalmed.repository.FichaInicialRepository;
import com.api.totalmed.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FichaInicialService {

    @Autowired private ClinicaService clinicaService;
    @Autowired private FichaInicialRepository fichaInicialRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private FilaAtendimentoService filaAtendimentoService;

//region Paciente
    public FichaInicial criar(Long pacienteId, FichaInicialRequestDTO dto) {
        validarMotivo(dto);

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
        if (!paciente.isAtivo()) {
            throw new BusinessException("Paciente desativado");
        }

        Clinica clinica = clinicaService.buscarPorId(dto.getUnidadeId());

        if (!clinica.isAtiva()) {
            throw new BusinessException("Unidade inativa");
        }
        if (fichaInicialRepository.existsByPacienteIdAndClinicaIdAndStatusIn(
                pacienteId, clinica.getId(), List.of(Status.PENDENTE, Status.ACEITO))) {
            throw new BusinessException("Você já tem uma ficha em andamento nesta unidade");
        }

        FichaInicial ficha = new FichaInicial();
        ficha.setPaciente(paciente);
        ficha.setClinica(clinica);
        ficha.setDataPreferencial(dto.getDataPreferencial());
        ficha.setTipoFichaAtendimento(dto.getTipoFichaAtendimento());
        ficha.setMotivoString(dto.getMotivoString());
        ficha.setMotivoEnum(dto.getMotivoEnum());
        ficha.setStatus(Status.PENDENTE);
        ficha.setDataHoraCriacao(LocalDateTime.now());
        return fichaInicialRepository.save(ficha);
    }

    public FichaInicial cancelar(Long id) {
        FichaInicial ficha = buscarPorId(id);
        if (ficha.getStatus() != Status.PENDENTE && ficha.getStatus() != Status.ACEITO) {
            throw new BusinessException("Só é possível cancelar ficha pendente ou aceita");
        }
        ficha.setStatus(Status.CANCELADO);
        return fichaInicialRepository.save(ficha);
    }

    public List<FichaInicial> listarPorPaciente(Long pacienteId) {
        return fichaInicialRepository.findByPacienteId(pacienteId);
    }
//endregion

//region Consulta
    public FichaInicial buscarPorId(Long id) {
        return fichaInicialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha não encontrada"));
    }

    public List<FichaInicial> listar(Status status) {
        return status == null ? fichaInicialRepository.findAll()
                              : fichaInicialRepository.findByStatus(status);
    }
//endregion

//region Recepcao (admin)
    public FichaInicial aceitar(Long id) {
        FichaInicial ficha = buscarPorId(id);
        exigirStatus(ficha, Status.PENDENTE);
        if (LocalDateTime.now().isAfter(ficha.getDataPreferencial().plusMinutes(15))) {
            throw new BusinessException("Ficha vencida, não pode ser aceita");
        }
        ficha.setStatus(Status.ACEITO);
        ficha.setCodigo(gerarCodigoUnico());
        return fichaInicialRepository.save(ficha);
    }

    public FichaInicial recusar(Long id, String justificativa) {
        FichaInicial ficha = buscarPorId(id);
        exigirStatus(ficha, Status.PENDENTE);
        ficha.setStatus(Status.RECUSADO);
        ficha.setJustificativaRecusa(justificativa);
        return fichaInicialRepository.save(ficha);
    }

    @Transactional
    public FichaInicial validarCodigo(String codigo) {
        FichaInicial ficha = fichaInicialRepository.findByCodigo(codigo.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Código não encontrado"));
        exigirStatus(ficha, Status.ACEITO);

        if (LocalDate.now().isBefore(ficha.getDataPreferencial().toLocalDate())) {
            throw new BusinessException("A ficha é para outro dia");
        }
        if (LocalDateTime.now().isAfter(ficha.getDataPreferencial().plusMinutes(15))) {
            throw new BusinessException("Ficha vencida");
        }

        ficha.setStatus(Status.VALIDADO);
        filaAtendimentoService.entrarNaFila(ficha);   // paciente entra na fila da triagem
        return fichaInicialRepository.save(ficha);
    }

    public int expirarVencidas() {
        List<FichaInicial> vencidas = fichaInicialRepository.findByStatusInAndDataPreferencialBefore(
                List.of(Status.PENDENTE, Status.ACEITO), LocalDateTime.now().minusMinutes(15));
        vencidas.forEach(f -> f.setStatus(Status.EXPIRADO));
        fichaInicialRepository.saveAll(vencidas);
        return vencidas.size();
    }
//endregion

//region Auxiliares
    private String gerarCodigoUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (fichaInicialRepository.findByCodigo(codigo).isPresent());
        return codigo;
    }

    private void exigirStatus(FichaInicial ficha, Status esperado) {
        if (ficha.getStatus() != esperado) {
            throw new BusinessException("A ficha precisa estar " + esperado + " (está " + ficha.getStatus() + ")");
        }
    }

    private void validarMotivo(FichaInicialRequestDTO dto) {
        if (dto.getMotivoEnum() == Motivo.COM_MOTIVO) {
            if (dto.getMotivoString() == null || dto.getMotivoString().isBlank()) {
                throw new BusinessException("Descrição dos motivos é obrigatória quando há motivo");
            }
        }
        if (dto.getMotivoEnum() == Motivo.SEM_MOTIVO) {
            if (dto.getMotivoString() != null && !dto.getMotivoString().isBlank()) {
                throw new BusinessException("Não deve existir descrição quando não há motivos");
            }
        }
    }
//endregion
}
