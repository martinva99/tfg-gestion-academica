package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


public record CalificacionUpdateDTO(
        @Min(value = 0, message = "La nota mínima es 0")
        @Max(value = 10, message = "La nota máxima es 10")
        Integer nota,
        String observaciones
) {
}
