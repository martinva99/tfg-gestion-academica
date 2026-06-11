package com.ieslasencinas.gestionacademica.dto;

import java.time.LocalDate;


public record AlumnoResponseDTO(
        Long idAlumno,
        String nombre,
        String apellidos,
        String email,
        LocalDate fechaNacimiento
) {
}
