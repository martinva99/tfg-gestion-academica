package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.EstadoEvaluacion;
import jakarta.validation.constraints.NotNull;


public record EvaluacionEstadoDTO(
        @NotNull(message = "El nuevo estado es obligatorio") EstadoEvaluacion nuevoEstado
) {
}
