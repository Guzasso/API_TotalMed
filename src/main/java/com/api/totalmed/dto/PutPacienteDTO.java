package com.api.totalmed.dto;

import com.api.totalmed.Enum.Sexo;
import com.api.totalmed.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PutPacienteDTO {

    @NotBlank
    private String nome;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String cpf;
    @NotBlank
    private String rg;
    @NotBlank
    private String password;
    @NotBlank
    private String tell;
    private String convenio;
    private String historicoMedico;

    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("3.0")
    private Double altura;
    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("500.0")
    private Double peso;
    @NotNull
    private LocalDate dataNascimento;
    @NotNull
    private Sexo sexo;
    @NotNull
    @Valid
    private DadosEndereco endereco;
}