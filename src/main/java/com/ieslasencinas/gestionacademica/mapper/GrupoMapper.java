package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.GrupoResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Grupo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class GrupoMapper {

    private final AsignaturaMapper asignaturaMapper;

    public GrupoResponseDTO toResponseDTO(Grupo grupo) {
        if (grupo == null) return null;

        return new GrupoResponseDTO(
                grupo.getIdGrupo(),
                grupo.getNombre(),
                grupo.getAnioAcademico().getCursoEscolar(),
                grupo.getCentro().getNombre(),
                grupo.getAsignaturas().stream()
                        .map(asignaturaMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }
}
