package com.api.totalmed.dto;

import com.api.totalmed.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

// PATCH = atualizacao parcial: todos os campos sao opcionais
@Getter
@Setter
public class PatchPacienteDTO {

    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String nome;

    @Email
    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String email;

    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String password;

    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String tell;

    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String convenio;

    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String historicoMedico;

    @Valid
    private DadosEndereco endereco;

    @DecimalMin("0.1")
    @DecimalMax("3.0")
    private Double altura;

    @DecimalMin("0.1")
    @DecimalMax("500.0")
    private Double peso;
}
