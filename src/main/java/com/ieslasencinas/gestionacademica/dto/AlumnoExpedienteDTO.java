package com.ieslasencinas.gestionacademica.dto;

import java.util.List;


public record AlumnoExpedienteDTO(
        Long idAlumno,
        String nombreCompleto,
        List<CalificacionResponseDTO> calificaciones
) {
}
