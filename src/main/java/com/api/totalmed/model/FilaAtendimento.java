package com.api.totalmed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class FilaAtendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ordemChegada;

    @Column(nullable = false)
    private LocalDateTime horarioEntrada;
    private boolean atendido;


    @OneToMany(mappedBy = "filaAtendimento")
    private List<FichaInicial> fichas;
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}
