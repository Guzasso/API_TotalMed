package com.api.totalmed.service;

import com.api.totalmed.exception.ResourceNotFoundException;
import com.api.totalmed.model.Clinica;
import com.api.totalmed.model.FichaInicial;
import com.api.totalmed.model.FilaAtendimento;
import com.api.totalmed.repository.ClinicaRepository;
import com.api.totalmed.repository.FichaInicialRepository;
import com.api.totalmed.repository.FilaAtendimentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClinicaService {

    @Autowired
    private ClinicaRepository clinicaRepository;
    @Autowired
    private FichaInicialRepository fichaInicialRepository;
    @Autowired
    private FilaAtendimentoRepository filaAtendimentoRepository;

    public Clinica cadastrar(Clinica clinica) {
        clinica.setId(null);      // garante que e criacao, nao edicao
        clinica.setAtiva(true);
        return clinicaRepository.save(clinica);
    }

    public List<Clinica> listar() { return clinicaRepository.findAll(); }

    public Clinica buscarPorId(Long id) {
        return clinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
    }

    public Clinica buscarPorNome(String nome) {
        return clinicaRepository.findByNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
    }

    // Exclusao definitiva: apaga as fichas da unidade, as entradas na fila dessas fichas e a unidade
    @Transactional
    public void deletar(Long id) {
        Clinica clinica = buscarPorId(id);

        List<FichaInicial> fichas = fichaInicialRepository.findByClinicaId(id);
        List<FilaAtendimento> filas = fichas.stream()
                .map(FichaInicial::getFilaAtendimento)
                .filter(Objects::nonNull)
                .toList();

        fichaInicialRepository.deleteAll(fichas);
        filaAtendimentoRepository.deleteAll(filas);
        clinicaRepository.delete(clinica);
    }
}
