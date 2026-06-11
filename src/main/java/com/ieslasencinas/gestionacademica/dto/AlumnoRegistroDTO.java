package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record AlumnoRegistroDTO(
        @NotNull(message = "El ID del usuario es obligatorio") Long idUsuario,
        @NotNull(message = "La fecha de nacimiento es obligatoria") LocalDate fechaNacimiento
) {
}
