package com.api.totalmed.repository;

import com.api.totalmed.model.FilaAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilaAtendimentoRepository extends JpaRepository<FilaAtendimento, Long> {
    List<FilaAtendimento> findByAtendidoFalseOrderByHorarioEntradaAsc();
}
