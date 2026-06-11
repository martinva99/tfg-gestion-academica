package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.NotificacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Notificacion;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.NotificacionMapper;
import com.ieslasencinas.gestionacademica.repository.NotificacionRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Notificación")
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private NotificacionMapper notificacionMapper;
    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    @Test
    @DisplayName("Debe crear una notificación correctamente para un usuario")
    void crear_datosValidos_creaNotificacion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(notificacionRepository.save(any())).thenReturn(new Notificacion());

        notificacionService.crear(1L, "Mensaje");

        verify(notificacionRepository).save(any());
    }

    @Test
    @DisplayName("Debe retornar las notificaciones de un usuario")
    void buscarPorUsuario_conNotificaciones_retornaLista() {

        when(notificacionRepository.findByUsuario_IdUsuarioOrderByFechaDesc(1L)).thenReturn(Collections.singletonList(new Notificacion()));
        when(notificacionMapper.toResponseDTO(any())).thenReturn(mock(NotificacionResponseDTO.class));

        List<NotificacionResponseDTO> result = notificacionService.buscarPorUsuario(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe marcar una notificación como leída")
    void marcarComoLeida_existente_actualizaLeida() {
        Notificacion notificacion = new Notificacion();
        notificacion.setLeida(false);
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        notificacionService.marcarComoLeida(1L);

        assertTrue(notificacion.isLeida());
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    @DisplayName("Debe marcar todas las notificaciones de un usuario como leídas")
    void marcarTodasComoLeidas_conNotificaciones_actualizaTodas() {
        Notificacion n1 = new Notificacion();
        n1.setLeida(false);
        when(notificacionRepository.findByUsuario_IdUsuarioAndLeidaFalse(1L)).thenReturn(List.of(n1));

        notificacionService.marcarTodasComoLeidas(1L);

        assertTrue(n1.isLeida());
        verify(notificacionRepository).saveAll(any());
    }

    @Test
    @DisplayName("Debe contar correctamente las notificaciones no leídas")
    void contarNoLeidas_conNoLeidas_retornaCantidadCorrecta() {
        when(notificacionRepository.countByUsuario_IdUsuarioAndLeidaFalse(1L)).thenReturn(5L);

        long count = notificacionService.contarNoLeidas(1L);

        assertEquals(5, count);
    }
}
