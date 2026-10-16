package com.api.totalmed.service;

import com.api.totalmed.Enum.Role;
import com.api.totalmed.Enum.Status;
import com.api.totalmed.dto.PatchPacienteDTO;
import com.api.totalmed.dto.PostPacienteDTO;
import com.api.totalmed.dto.PutPacienteDTO;
import com.api.totalmed.endereco.Endereco;
import com.api.totalmed.exception.ResourceNotFoundException;
import com.api.totalmed.model.FichaInicial;
import com.api.totalmed.model.FilaAtendimento;
import com.api.totalmed.model.Paciente;
import com.api.totalmed.repository.FichaInicialRepository;
import com.api.totalmed.repository.FilaAtendimentoRepository;
import com.api.totalmed.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PacienteService {

    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private UsuarioService usuarioService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private FichaInicialRepository fichaInicialRepository;
    @Autowired private FilaAtendimentoRepository filaAtendimentoRepository;

//region CRUD

    //region Post
    public Paciente cadastrar(PostPacienteDTO dto) {
        usuarioService.validarUnicidade(dto.getEmail(), dto.getCpf(), dto.getRg(), null);

        Paciente paciente = new Paciente();
        paciente.setNome(dto.getNome());
        paciente.setEmail(dto.getEmail());
        paciente.setCpf(dto.getCpf());
        paciente.setRg(dto.getRg());
        paciente.setPassword(passwordEncoder.encode(dto.getPassword()));
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setSexo(dto.getSexo());
        paciente.setTell(dto.getTell());
        paciente.setRole(Role.PACIENTE);
        paciente.setConvenio(dto.getConvenio());
        paciente.setHistoricoMedico(dto.getHistoricoMedico());
        paciente.setAltura(dto.getAltura());
        paciente.setPeso(dto.getPeso());
        if (dto.getEndereco() != null) {
            paciente.setEndereco(new Endereco(dto.getEndereco()));
        }
        return pacienteRepository.save(paciente);
    }
    //endregion

    //region Put
    public Paciente redefinir(Long id, PutPacienteDTO dto) {
        Paciente paciente = buscarPorId(id);
        usuarioService.validarUnicidade(dto.getEmail(), dto.getCpf(), dto.getRg(), id);

        paciente.setNome(dto.getNome());
        paciente.setEmail(dto.getEmail());
        paciente.setCpf(dto.getCpf());
        paciente.setRg(dto.getRg());
        paciente.setPassword(passwordEncoder.encode(dto.getPassword()));
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setSexo(dto.getSexo());
        paciente.setTell(dto.getTell());
        paciente.setConvenio(dto.getConvenio());
        paciente.setHistoricoMedico(dto.getHistoricoMedico());
        paciente.setAltura(dto.getAltura());
        paciente.setPeso(dto.getPeso());
        if (dto.getEndereco() != null) {
            paciente.setEndereco(new Endereco(dto.getEndereco()));
        }
        return pacienteRepository.save(paciente);
    }
    //endregion

    //region Patch
    public Paciente atualizar(Long id, PatchPacienteDTO dto) {
        Paciente paciente = buscarPorId(id);
        usuarioService.validarUnicidade(dto.getEmail(), null, null, id);

        if (dto.getEmail() != null)           paciente.setEmail(dto.getEmail());
        if (dto.getNome() != null)            paciente.setNome(dto.getNome());
        if (dto.getPassword() != null)        paciente.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getTell() != null)            paciente.setTell(dto.getTell());
        if (dto.getConvenio() != null)        paciente.setConvenio(dto.getConvenio());
        if (dto.getHistoricoMedico() != null) paciente.setHistoricoMedico(dto.getHistoricoMedico());
        if (dto.getAltura() != null)          paciente.setAltura(dto.getAltura());
        if (dto.getPeso() != null)            paciente.setPeso(dto.getPeso());
        if (dto.getEndereco() != null)        paciente.setEndereco(new Endereco(dto.getEndereco()));

        return pacienteRepository.save(paciente);
    }
    //endregion

    //region Delete
    @Transactional
    public void desativar(Long id) {
        Paciente paciente = buscarPorId(id);
        paciente.setAtivo(false);
        pacienteRepository.save(paciente);

        // cancela o que ainda esta em aberto
        List<FichaInicial> abertas = fichaInicialRepository.findByPacienteIdAndStatusIn(
                id, List.of(Status.PENDENTE, Status.ACEITO));
        abertas.forEach(f -> f.setStatus(Status.CANCELADO));
        fichaInicialRepository.saveAll(abertas);
    }

    @Transactional
    public void deletar(Long id) {
        Paciente paciente = buscarPorId(id);

        List<FichaInicial> fichas = fichaInicialRepository.findByPacienteId(id);
        List<FilaAtendimento> filas = fichas.stream()
                .map(FichaInicial::getFilaAtendimento)
                .filter(Objects::nonNull)
                .toList();

        fichaInicialRepository.deleteAll(fichas);
        filaAtendimentoRepository.deleteAll(filas);
        pacienteRepository.delete(paciente);
    }
    //endregion
//endregion

//region Consultar
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
    }

    public Paciente buscarPorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
    }

    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
    }

    public List<Paciente> listarPaciente() { return pacienteRepository.findByAtivoTrue(); }
//endregion
}
