package com.ieslasencinas.gestionacademica.dto;

public record GrupoAsignaturaDTO(
        Long idAsignatura,
        String nombreAsignatura,
        Long idGrupo,
        String nombreGrupo
) {
}