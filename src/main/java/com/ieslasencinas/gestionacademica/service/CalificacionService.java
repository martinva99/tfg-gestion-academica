package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.CalificacionCreateDTO;
import com.ieslasencinas.gestionacademica.dto.CalificacionResponseDTO;
import com.ieslasencinas.gestionacademica.dto.CalificacionUpdateDTO;

import java.util.List;


public interface CalificacionService {
    CalificacionResponseDTO registrar(CalificacionCreateDTO dto);

    List<CalificacionResponseDTO> buscarPorEvaluacion(Long idEvaluacion);

    List<CalificacionResponseDTO> buscarPorMatricula(Long idMatricula);

    CalificacionResponseDTO actualizar(Long idCalificacion, CalificacionUpdateDTO dto);

    double calcularMedia(Long idMatricula);

    void registrarCalificacionesLote(List<CalificacionCreateDTO> dtos);
}
