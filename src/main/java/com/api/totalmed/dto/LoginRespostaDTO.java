package com.api.totalmed.dto;

import com.api.totalmed.Enum.Role;

public record LoginRespostaDTO(Long id, String nome, Role role) {
}
