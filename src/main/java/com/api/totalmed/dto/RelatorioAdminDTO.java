package com.api.totalmed.dto;

public record RelatorioAdminDTO(long totalPacientes,
                                long fichasPendentes,
                                long fichasAceitas,
                                long fichasRecusadas) {
}
