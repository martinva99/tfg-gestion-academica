package com.ieslasencinas.gestionacademica.dto;


public record MatriculaResponseDTO(
        Long idMatricula,
        String nombreAlumno,
        String nombreAsignatura,
        String nombreGrupo,
        String cursoEscolar
) {
}
