package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;


public record FranjaHorariaDTO(
        Long idFranja,
        @NotNull(message = "El día de la semana es obligatorio") DiaSemana diaSemana,
        @NotNull(message = "La hora de inicio es obligatoria") LocalTime horaInicio,
        @NotNull(message = "La hora de fin es obligatoria") LocalTime horaFin
) {
}
