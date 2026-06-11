package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record CalificacionCreateDTO(
        @NotNull(message = "El ID de la evaluación es obligatorio") Long idEvaluacion,
        @NotNull(message = "El ID de la matrícula es obligatorio") Long idMatricula,
        @NotNull(message = "El ID del docente es obligatorio") Long idDocente,
        @NotNull(message = "La nota es obligatoria")
        @Min(value = 0, message = "La nota mínima es 0")
        @Max(value = 10, message = "La nota máxima es 10")
        Integer nota,
        String observaciones
) {
}
