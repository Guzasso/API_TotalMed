package com.api.totalmed.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("PACIENTE")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Paciente extends Usuario {

    @Column(length = 200)
    private String convenio;

    @Column(length = 2000)
    private String historicoMedico;

    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("3.0")
    private Double altura;

    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("500.0")
    private Double peso;

}
