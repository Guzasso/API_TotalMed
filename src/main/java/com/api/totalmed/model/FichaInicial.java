package com.api.totalmed.model;

import com.api.totalmed.Enum.Motivo;
import com.api.totalmed.Enum.Status;
import com.api.totalmed.Enum.TipoFichaAtendimento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FichaInicial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @Column(name = "Data_preferencial", nullable = false)
    private LocalDateTime dataPreferencial; //Hora e Data para agendar

    @NotNull
    @Column(name = "Data_Registrada", nullable = false)
    private LocalDateTime dataHoraCriacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoFichaAtendimento tipoFichaAtendimento;

    @Column(length = 2000)
    private String motivoString;

    @Enumerated(EnumType.STRING)
    private Motivo motivoEnum;

    @Column
    private String codigo;                  // gerado quando a ficha e aceita

    @Column(length = 500)
    private String justificativaRecusa;     // obrigatoria ao recusar

    @ManyToOne
    @JsonIgnore
    private FilaAtendimento filaAtendimento;
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
    @ManyToOne
    private Clinica clinica;

}
