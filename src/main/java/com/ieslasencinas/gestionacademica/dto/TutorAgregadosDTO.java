package com.ieslasencinas.gestionacademica.dto;

import java.util.List;

public class TutorAgregadosDTO {

    public record HijoHorarioDTO(
            Long idAlumno,
            String nombreAlumno,
            List<SesionHorariaResponseDTO> horario
    ) {
    }

    public record HijoExpedienteDTO(
            Long idAlumno,
            String nombreAlumno,
            AlumnoExpedienteDTO expediente
    ) {
    }

    public record HijoAsistenciaDTO(
            Long idAlumno,
            String nombreAlumno,
            List<FaltaAsistenciaResponseDTO> faltas,
            AsistenciaResumenDTO resumen
    ) {
    }

    public record HijoParteDTO(
            Long idAlumno,
            String nombreAlumno,
            List<ParteResponseDTO> partes
    ) {
    }
}
