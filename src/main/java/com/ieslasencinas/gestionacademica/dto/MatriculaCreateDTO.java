package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotNull;


public record MatriculaCreateDTO(
        @NotNull(message = "El ID del alumno es obligatorio") Long idAlumno,
        @NotNull(message = "El ID de la asignatura es obligatorio") Long idAsignatura,
        @NotNull(message = "El ID del grupo es obligatorio") Long idGrupo,
        @NotNull(message = "El ID del año académico es obligatorio") Long idAnio
) {
}
