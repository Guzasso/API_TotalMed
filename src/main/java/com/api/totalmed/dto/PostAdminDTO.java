package com.api.totalmed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostAdminDTO {
    @NotBlank
    private String nome;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String cpf;
    @NotBlank
    private String rg;
    @NotBlank
    private String password;
    @NotBlank
    private String tell;
    @NotNull
    private LocalDate dataNascimento;
}
