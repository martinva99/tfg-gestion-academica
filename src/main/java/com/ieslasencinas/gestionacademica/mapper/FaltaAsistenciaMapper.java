package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.FaltaAsistenciaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.FaltaAsistencia;
import org.springframework.stereotype.Component;


@Component
public class FaltaAsistenciaMapper {

    public FaltaAsistenciaResponseDTO toResponseDTO(FaltaAsistencia falta) {
        if (falta == null) return null;

        return new FaltaAsistenciaResponseDTO(
                falta.getIdAsistencia(),
                falta.getFecha(),
                falta.getTipo(),
                falta.isJustificada(),
                falta.getObservaciones(),
                falta.getMatricula().getAlumno().getUsuario().getNombre() + " " + falta.getMatricula().getAlumno().getUsuario().getApellidos(),
                falta.getSesionHoraria().getAsignatura().getNombre(),
                falta.getSesionHoraria().getGrupo().getNombre()
        );
    }
}
