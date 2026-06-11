package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.CalificacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Calificacion;
import org.springframework.stereotype.Component;


@Component
public class CalificacionMapper {

    public CalificacionResponseDTO toResponseDTO(Calificacion calificacion) {
        if (calificacion == null) return null;

        return new CalificacionResponseDTO(
                calificacion.getIdCalificacion(),
                calificacion.getMatricula().getIdMatricula(),
                calificacion.getEvaluacion().getIdEvaluacion(),
                calificacion.getDocente().getIdDocente(),
                calificacion.getNota(),
                calificacion.getObservaciones(),
                calificacion.getEvaluacion().getTrimestre(),
                calificacion.getMatricula().getAsignatura().getNombre(),
                calificacion.getMatricula().getAlumno().getUsuario().getNombre() + " " + calificacion.getMatricula().getAlumno().getUsuario().getApellidos(),
                calificacion.getDocente().getUsuario().getNombre() + " " + calificacion.getDocente().getUsuario().getApellidos(),
                calificacion.getEvaluacion().getEstado().name(),
                calificacion.getMatricula().getAnioAcademico().getCursoEscolar()
        );
    }
}
