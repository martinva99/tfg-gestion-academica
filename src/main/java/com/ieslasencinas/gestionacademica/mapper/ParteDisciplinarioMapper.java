package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.ParteResponseDTO;
import com.ieslasencinas.gestionacademica.entity.ParteDisciplinario;
import org.springframework.stereotype.Component;


@Component
public class ParteDisciplinarioMapper {

    public ParteResponseDTO toResponseDTO(ParteDisciplinario parte) {
        if (parte == null) return null;

        return new ParteResponseDTO(
                parte.getIdParte(),
                parte.getFecha(),
                parte.getTipo(),
                parte.getDescripcion(),
                parte.getEstado(),
                parte.getAlumno().getUsuario().getNombre() + " " + parte.getAlumno().getUsuario().getApellidos(),
                parte.getDocente().getUsuario().getNombre() + " " + parte.getDocente().getUsuario().getApellidos()
        );
    }
}
