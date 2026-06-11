package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.*;

import java.util.List;


public interface DocenteService {

    void asignarPerfilDocente(DocenteRegistroDTO dto);

    DocenteResponseDTO buscarPorId(Long idDocente);

    DocenteResponseDTO buscarPorIdUsuario(Long idUsuario);

    List<DocenteResponseDTO> buscarTodos();

    List<MatriculaResponseDTO> obtenerAlumnosPorSesion(Long idSesion);

    List<GrupoAsignaturaDTO> obtenerGruposYAsignaturas(Long idDocente);

    List<CalificacionResponseDTO> obtenerCalificacionesPorAsignaturaYGrupo(Long idAsignatura, Long idGrupo, Integer trimestre);

    List<ParteResponseDTO> obtenerPartesPorDocente(Long idUsuario);

    List<DocenteResponseDTO> obtenerDocentesDisponibles(Long idFranja);
}