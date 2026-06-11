package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.*;

import java.util.List;


public interface FaltaAsistenciaService {
    FaltaAsistenciaResponseDTO registrarFalta(FaltaAsistenciaCreateDTO dto);

    List<FaltaAsistenciaResponseDTO> buscarPorMatricula(Long idMatricula);

    FaltaAsistenciaResponseDTO justificarFalta(Long idFalta, FaltaAsistenciaJustificarDTO dto);

    AsistenciaResumenDTO calcularResumenAsistencia(Long idMatricula);

    List<FaltaAsistenciaResponseDTO> buscarPorSesion(Long idSesion);

    void registrarFaltasLote(List<FaltaAsistenciaCreateDTO> dtos);
}
