package com.api.totalmed.model;


import com.api.totalmed.Enum.Role;
import com.api.totalmed.Enum.Sexo;
import com.api.totalmed.endereco.Endereco;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "tipo_usuario")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank
    @JsonIgnore
    @Column(name = "senha", nullable = false)
    private String password;

    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String cpf;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String rg;

    @NotBlank
    @Column(nullable = false)
    private String tell;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotNull
    @Enumerated(EnumType.STRING)
    // Mesma coluna do @DiscriminatorColumn: so leitura aqui, quem grava e o discriminador
    // (por isso os @DiscriminatorValue de Admin/Paciente usam o mesmo texto do enum Role)
    @Column(name = "tipo_usuario", nullable = false, insertable = false, updatable = false)
    private Role role;

    @ColumnDefault("1")
    @Column(nullable = false)
    private boolean ativo = true;

    @Embedded
    private Endereco endereco;

}
