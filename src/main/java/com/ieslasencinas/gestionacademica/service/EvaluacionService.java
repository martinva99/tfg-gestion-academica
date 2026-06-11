package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.EvaluacionEstadoDTO;
import com.ieslasencinas.gestionacademica.dto.EvaluacionResponseDTO;

import java.util.List;


public interface EvaluacionService {
    EvaluacionResponseDTO buscarPorId(Long idEvaluacion);

    List<EvaluacionResponseDTO> buscarPorGrupo(Long idGrupo);

    List<EvaluacionResponseDTO> buscarTodas();

    EvaluacionResponseDTO cambiarEstado(Long idEvaluacion, EvaluacionEstadoDTO dto);
}
