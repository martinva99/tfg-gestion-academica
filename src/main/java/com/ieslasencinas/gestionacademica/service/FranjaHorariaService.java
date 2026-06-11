package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.FranjaHorariaDTO;

import java.util.List;

public interface FranjaHorariaService {
    FranjaHorariaDTO crear(FranjaHorariaDTO dto);

    FranjaHorariaDTO buscarPorId(Long idFranja);

    List<FranjaHorariaDTO> buscarTodos();

    FranjaHorariaDTO actualizar(Long idFranja, FranjaHorariaDTO dto);

    void eliminar(Long idFranja);
}
