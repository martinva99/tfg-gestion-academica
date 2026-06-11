package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AnioAcademicoDTO;

import java.util.List;

public interface AnioAcademicoService {
    AnioAcademicoDTO crear(AnioAcademicoDTO dto);

    AnioAcademicoDTO buscarPorId(Long idAnio);

    List<AnioAcademicoDTO> buscarTodos();

    AnioAcademicoDTO actualizar(Long idAnio, AnioAcademicoDTO dto);
}
