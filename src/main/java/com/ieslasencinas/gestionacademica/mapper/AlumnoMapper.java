package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.AlumnoResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Alumno;
import org.springframework.stereotype.Component;


@Component
public class AlumnoMapper {

    public AlumnoResponseDTO toResponseDTO(Alumno alumno) {
        if (alumno == null) return null;

        return new AlumnoResponseDTO(
                alumno.getIdAlumno(),
                alumno.getUsuario().getNombre(),
                alumno.getUsuario().getApellidos(),
                alumno.getUsuario().getEmail(),
                alumno.getFechaNacimiento()
        );
    }
}
