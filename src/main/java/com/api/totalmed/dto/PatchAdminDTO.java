package com.api.totalmed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// PATCH = atualizacao parcial: todos os campos sao opcionais
@Getter
@Setter
public class PatchAdminDTO {

    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String nome;
    @Email
    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String email;
    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String password;
    @Pattern(regexp = ".*\\S.*", message = "não pode ser vazio")
    private String tell;
}
