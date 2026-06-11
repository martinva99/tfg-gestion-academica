package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.GrupoCreateDTO;
import com.ieslasencinas.gestionacademica.dto.GrupoResponseDTO;

import java.util.List;

public interface GrupoService {
    GrupoResponseDTO crear(GrupoCreateDTO dto);

    GrupoResponseDTO buscarPorId(Long idGrupo);

    List<GrupoResponseDTO> buscarTodos();

    List<GrupoResponseDTO> buscarPorAnioAcademico(Long idAnio);

    GrupoResponseDTO actualizar(Long idGrupo, GrupoCreateDTO dto);

    void asignarAsignatura(Long idGrupo, Long idAsignatura);

    void desasignarAsignatura(Long idGrupo, Long idAsignatura);
}
