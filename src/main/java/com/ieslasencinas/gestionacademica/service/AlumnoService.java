package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.*;

import java.util.List;


public interface AlumnoService {

    AlumnoResponseDTO crearPerfilAlumno(AlumnoRegistroDTO dto);

    AlumnoResponseDTO buscarPorId(Long idAlumno);

    AlumnoResponseDTO buscarPorEmail(String email);

    AlumnoExpedienteDTO buscarPorIdExpediente(Long idAlumno);

    List<AlumnoResponseDTO> buscarTodos();

    void vincularTutorLegal(VinculacionAlumnoTutorDTO dto);

    void desvincularTutorLegal(VinculacionAlumnoTutorDTO dto);

    List<SesionHorariaResponseDTO> obtenerMiHorario();

    AlumnoExpedienteDTO obtenerMiExpediente();

    List<FaltaAsistenciaResponseDTO> obtenerMiAsistencia();

    List<ParteResponseDTO> obtenerMisPartes();

    List<NotificacionResponseDTO> obtenerMisNotificaciones();
}
