package com.ieslasencinas.gestionacademica.dto;

import java.util.List;


public record GrupoResponseDTO(
        Long idGrupo,
        String nombre,
        String cursoEscolar,
        String nombreCentro,
        List<AsignaturaDTO> asignaturas
) {
}
