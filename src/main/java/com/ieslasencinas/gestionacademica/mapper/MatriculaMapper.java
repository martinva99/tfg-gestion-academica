package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.MatriculaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Matricula;
import org.springframework.stereotype.Component;


@Component
public class MatriculaMapper {

    public MatriculaResponseDTO toResponseDTO(Matricula matricula) {
        if (matricula == null) return null;

        return new MatriculaResponseDTO(
                matricula.getIdMatricula(),
                matricula.getAlumno().getUsuario().getNombre() + " " + matricula.getAlumno().getUsuario().getApellidos(),
                matricula.getAsignatura().getNombre(),
                matricula.getGrupo().getNombre(),
                matricula.getAnioAcademico().getCursoEscolar()
        );
    }
}
