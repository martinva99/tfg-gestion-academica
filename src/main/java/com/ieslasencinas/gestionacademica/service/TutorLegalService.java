package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.TutorAgregadosDTO;
import com.ieslasencinas.gestionacademica.dto.TutorDashboardDTO;
import com.ieslasencinas.gestionacademica.dto.TutorLegalRegistroDTO;
import com.ieslasencinas.gestionacademica.dto.TutorLegalResponseDTO;

import java.util.List;


public interface TutorLegalService {

    TutorLegalResponseDTO crearPerfilTutor(TutorLegalRegistroDTO dto);

    TutorLegalResponseDTO buscarPorId(Long idTutorLegal);

    List<TutorLegalResponseDTO> buscarTodos();

    TutorLegalResponseDTO buscarPorIdUsuario(Long idUsuario);

    TutorDashboardDTO obtenerResumenDashboard();

    List<TutorAgregadosDTO.HijoHorarioDTO> obtenerHorariosAgrupados();

    List<TutorAgregadosDTO.HijoExpedienteDTO> obtenerExpedientesAgrupados();

    List<TutorAgregadosDTO.HijoAsistenciaDTO> obtenerAsistenciaAgrupada();

    List<TutorAgregadosDTO.HijoParteDTO> obtenerPartesAgrupados();
}
