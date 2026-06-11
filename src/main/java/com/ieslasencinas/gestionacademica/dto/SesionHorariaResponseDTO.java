package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;

import java.time.LocalTime;


public record SesionHorariaResponseDTO(
        Long idSesion,
        String nombreGrupo,
        String nombreDocente,
        String nombreAsignatura,
        DiaSemana diaSemana,
        LocalTime horaInicio,
        LocalTime horaFin,
        String aula
) {
}
