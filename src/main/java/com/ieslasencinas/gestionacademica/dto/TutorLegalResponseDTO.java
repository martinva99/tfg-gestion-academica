package com.ieslasencinas.gestionacademica.dto;

import java.util.List;


public record TutorLegalResponseDTO(
        Long idTutorLegal,
        String nombre,
        String apellidos,
        String email,
        List<AlumnoResponseDTO> alumnos
) {
}
