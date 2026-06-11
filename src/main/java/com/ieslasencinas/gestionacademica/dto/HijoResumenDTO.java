package com.ieslasencinas.gestionacademica.dto;

import java.util.List;


public record HijoResumenDTO(
        Long idAlumno,
        String nombre,
        String apellidos,
        List<SesionHorariaResponseDTO> horarioHoy,
        List<AsistenciaResumenDTO> asistencia
) {
}
