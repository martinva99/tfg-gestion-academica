package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;

import java.util.List;
import java.util.Map;


public record HorarioSemanalDTO(
        String titular,
        Map<DiaSemana, List<SesionHorariaResponseDTO>> sesionesPorDia
) {
}
