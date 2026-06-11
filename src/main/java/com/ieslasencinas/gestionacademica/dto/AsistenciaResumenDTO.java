package com.ieslasencinas.gestionacademica.dto;

import java.util.List;


public record AsistenciaResumenDTO(
        String nombreAsignatura,
        double totalHoras,
        double horasAusentesInjustificadas,
        double porcentajeAusencias,
        List<Integer> umbralesSuperados
) {
}
