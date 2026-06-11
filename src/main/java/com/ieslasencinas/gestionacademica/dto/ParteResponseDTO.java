package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.EstadoParte;
import com.ieslasencinas.gestionacademica.entity.enums.TipoParte;

import java.time.LocalDate;


public record ParteResponseDTO(
        Long idParte,
        LocalDate fecha,
        TipoParte tipo,
        String descripcion,
        EstadoParte estado,
        String nombreAlumno,
        String nombreDocente
) {
}
