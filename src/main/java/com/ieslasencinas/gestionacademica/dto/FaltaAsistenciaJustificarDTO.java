package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotNull;


public record FaltaAsistenciaJustificarDTO(
        @NotNull(message = "Debe indicar si la falta está justificada") Boolean justificada,
        String observaciones
) {}
