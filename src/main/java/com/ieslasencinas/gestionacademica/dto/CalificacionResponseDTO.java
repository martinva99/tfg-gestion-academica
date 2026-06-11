package com.ieslasencinas.gestionacademica.dto;


public record CalificacionResponseDTO(
        Long idCalificacion,
        Long idMatricula,
        Long idEvaluacion,
        Long idDocente,
        Integer nota,
        String observaciones,
        Integer trimestre,
        String nombreAsignatura,
        String nombreAlumno,
        String nombreDocente,
        String estadoEvaluacion,
        String anioAcademico
) {
}
