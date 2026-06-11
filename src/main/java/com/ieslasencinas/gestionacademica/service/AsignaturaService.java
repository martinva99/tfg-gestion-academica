package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AsignaturaDTO;

import java.util.List;

public interface AsignaturaService {
    AsignaturaDTO crear(AsignaturaDTO dto);

    AsignaturaDTO buscarPorId(Long idAsignatura);

    List<AsignaturaDTO> buscarTodos();

    AsignaturaDTO actualizar(Long idAsignatura, AsignaturaDTO dto);

    void eliminar(Long idAsignatura);
}
