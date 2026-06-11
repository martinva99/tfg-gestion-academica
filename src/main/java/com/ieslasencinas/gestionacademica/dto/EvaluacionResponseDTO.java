package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.EstadoEvaluacion;


public record EvaluacionResponseDTO(
        Long idEvaluacion,
        Integer trimestre,
        EstadoEvaluacion estado,
        String nombreGrupo
) {
}
