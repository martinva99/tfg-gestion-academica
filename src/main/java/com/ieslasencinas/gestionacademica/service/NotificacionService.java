package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.NotificacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Alumno;
import com.ieslasencinas.gestionacademica.entity.Docente;

import java.util.List;


public interface NotificacionService {
    void crear(Long idUsuario, String mensaje);

    void notificarAlumnoYTutores(Alumno alumno, String mensaje);

    void notificarDocentes(List<Docente> docentes, String mensaje);

    void notificarUsuariosPorRol(String nombreRol, String mensaje);

    List<NotificacionResponseDTO> buscarPorUsuario(Long idUsuario);

    void marcarComoLeida(Long idNotificacion);

    void marcarTodasComoLeidas(Long idUsuario);

    long contarNoLeidas(Long idUsuario);
}
