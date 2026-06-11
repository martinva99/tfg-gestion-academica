package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.NotificacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.NotificacionMapper;
import com.ieslasencinas.gestionacademica.repository.NotificacionRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionMapper notificacionMapper;

    @Override
    @Transactional
    public void crear(Long idUsuario, String mensaje) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setLeida(false);

        notificacionRepository.save(notificacion);
    }

    @Override
    @Transactional
    public void notificarAlumnoYTutores(Alumno alumno, String mensaje) {

        crear(alumno.getUsuario().getIdUsuario(), mensaje);

        if (alumno.getTutoresLegales() != null) {
            for (TutorLegal tutor : alumno.getTutoresLegales()) {
                crear(tutor.getUsuario().getIdUsuario(), mensaje);
            }
        }
    }

    @Override
    @Transactional
    public void notificarDocentes(List<Docente> docentes, String mensaje) {
        if (docentes != null) {
            for (Docente docente : docentes) {
                crear(docente.getUsuario().getIdUsuario(), mensaje);
            }
        }
    }

    @Override
    @Transactional
    public void notificarUsuariosPorRol(String nombreRol, String mensaje) {
        List<Usuario> usuarios = usuarioRepository.findByRoles_Nombre(nombreRol);
        for (Usuario usuario : usuarios) {
            crear(usuario.getIdUsuario(), mensaje);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> buscarPorUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuario_IdUsuarioOrderByFechaDesc(idUsuario).stream()
                .map(notificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));
        notificacion.setLeida(true);
        notificacionRepository.save(notificacion);
    }

    @Override
    @Transactional
    public void marcarTodasComoLeidas(Long idUsuario) {
        List<Notificacion> noLeidas = notificacionRepository.findByUsuario_IdUsuarioAndLeidaFalse(idUsuario);
        noLeidas.forEach(n -> n.setLeida(true));
        notificacionRepository.saveAll(noLeidas);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarNoLeidas(Long idUsuario) {
        return notificacionRepository.countByUsuario_IdUsuarioAndLeidaFalse(idUsuario);
    }
}
