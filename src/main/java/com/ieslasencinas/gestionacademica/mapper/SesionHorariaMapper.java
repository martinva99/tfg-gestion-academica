package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.SesionHoraria;
import org.springframework.stereotype.Component;


@Component
public class SesionHorariaMapper {

    public SesionHorariaResponseDTO toResponseDTO(SesionHoraria sesion) {
        if (sesion == null) return null;

        return new SesionHorariaResponseDTO(
                sesion.getIdSesion(),
                sesion.getGrupo().getNombre(),
                sesion.getDocente().getUsuario().getNombre() + " " + sesion.getDocente().getUsuario().getApellidos(),
                sesion.getAsignatura().getNombre(),
                sesion.getFranjaHoraria().getDiaSemana(),
                sesion.getFranjaHoraria().getHoraInicio(),
                sesion.getFranjaHoraria().getHoraFin(),
                sesion.getAula()
        );
    }
}
