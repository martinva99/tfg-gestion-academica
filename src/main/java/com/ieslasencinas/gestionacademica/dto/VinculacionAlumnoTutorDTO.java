package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotNull;


public record VinculacionAlumnoTutorDTO(
        @NotNull(message = "El ID del alumno es obligatorio") Long idAlumno,
        @NotNull(message = "El ID del tutor legal es obligatorio") Long idTutorLegal
) {
}
