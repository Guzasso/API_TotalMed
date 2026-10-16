package com.api.totalmed.dto;

import com.api.totalmed.Enum.Motivo;
import com.api.totalmed.Enum.TipoFichaAtendimento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FichaInicialRequestDTO {

    @NotNull
    private Long unidadeId;
    @NotNull
    @FutureOrPresent
    private LocalDateTime dataPreferencial;
    @NotNull
    private TipoFichaAtendimento tipoFichaAtendimento;

    private String motivoString;
    private Motivo motivoEnum;
}
