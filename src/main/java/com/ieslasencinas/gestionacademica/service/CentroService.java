package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.CentroDTO;

import java.util.List;

public interface CentroService {
    CentroDTO crear(CentroDTO dto);

    CentroDTO buscarPorId(Long idCentro);

    List<CentroDTO> buscarTodos();

    CentroDTO actualizar(Long idCentro, CentroDTO dto);

    void eliminar(Long idCentro);
}
