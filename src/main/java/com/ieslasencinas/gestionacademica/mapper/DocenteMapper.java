package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.DocenteResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Docente;
import org.springframework.stereotype.Component;


@Component
public class DocenteMapper {

    public DocenteResponseDTO toResponseDTO(Docente docente) {
        if (docente == null) return null;

        return new DocenteResponseDTO(
                docente.getIdDocente(),
                docente.getUsuario().getNombre(),
                docente.getUsuario().getApellidos(),
                docente.getUsuario().getEmail(),
                docente.getEspecialidad()
        );
    }
}
