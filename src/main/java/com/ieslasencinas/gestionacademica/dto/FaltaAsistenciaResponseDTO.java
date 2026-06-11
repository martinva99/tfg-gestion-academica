package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.TipoFalta;

import java.time.LocalDate;


public record FaltaAsistenciaResponseDTO(
        Long idAsistencia,
        LocalDate fecha,
        TipoFalta tipo,
        boolean justificada,
        String observaciones,
        String nombreAlumno,
        String nombreAsignatura,
        String nombreGrupo
) {
}
