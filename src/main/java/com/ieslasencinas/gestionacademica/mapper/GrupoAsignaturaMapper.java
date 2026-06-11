package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.GrupoAsignaturaDTO;
import com.ieslasencinas.gestionacademica.entity.SesionHoraria;
import org.springframework.stereotype.Component;

@Component
public class GrupoAsignaturaMapper {

    public GrupoAsignaturaDTO toDTO(SesionHoraria sesion) {
        if (sesion == null) return null;

        return new GrupoAsignaturaDTO(
                sesion.getAsignatura().getIdAsignatura(),
                sesion.getAsignatura().getNombre(),
                sesion.getGrupo().getIdGrupo(),
                sesion.getGrupo().getNombre()
        );
    }
}