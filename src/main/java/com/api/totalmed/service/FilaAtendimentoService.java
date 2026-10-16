package com.api.totalmed.service;

import com.api.totalmed.exception.ResourceNotFoundException;
import com.api.totalmed.model.FichaInicial;
import com.api.totalmed.model.FilaAtendimento;
import com.api.totalmed.repository.FilaAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FilaAtendimentoService {

    @Autowired
    private FilaAtendimentoRepository filaAtendimentoRepository;

    // Chamado quando a recepcao valida o codigo da ficha
    public FilaAtendimento entrarNaFila(FichaInicial ficha) {
        FilaAtendimento fila = new FilaAtendimento();
        fila.setPaciente(ficha.getPaciente());
        fila.setOrdemChegada(ficha.getTipoFichaAtendimento().name());
        fila.setHorarioEntrada(LocalDateTime.now());
        fila.setAtendido(false);
        fila = filaAtendimentoRepository.save(fila);
        ficha.setFilaAtendimento(fila);
        return fila;
    }

    public List<FilaAtendimento> listarFila() {
        return filaAtendimentoRepository.findByAtendidoFalseOrderByHorarioEntradaAsc();
    }

    public FilaAtendimento marcarAtendido(Long filaId) {
        FilaAtendimento fila = filaAtendimentoRepository.findById(filaId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrada na fila não encontrada"));
        fila.setAtendido(true);
        return filaAtendimentoRepository.save(fila);
    }
}
