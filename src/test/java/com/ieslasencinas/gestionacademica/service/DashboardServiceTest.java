package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.DashboardDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Dashboard")
class DashboardServiceTest {

    @Mock
    private HorarioService horarioService;
    @Mock
    private NotificacionService notificacionService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CurrentUserService currentUserService;
    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    @DisplayName("Debe retornar el horario del día actual y el contador de notificaciones no leídas")
    void obtenerDashboard_conDatos_retornaDashboardCompleto() {
        List<SesionHorariaResponseDTO> horario = List.of(mock(SesionHorariaResponseDTO.class));
        when(horarioService.obtenerHorarioDiario(eq(1L), any())).thenReturn(horario);
        when(notificacionService.contarNoLeidas(1L)).thenReturn(5L);

        DashboardDTO result = dashboardService.obtenerDashboard(1L);

        assertNotNull(result);

        assertEquals(horario, result.horarioDiario());
        assertEquals(5, result.notificacionesNoLeidas());
    }

    @Test
    @DisplayName("Debe retornar horario vacío y 0 notificaciones cuando no hay datos")
    void obtenerDashboard_sinDatos_retornaDashboardVacio() {
        when(horarioService.obtenerHorarioDiario(eq(1L), any())).thenReturn(Collections.emptyList());
        when(notificacionService.contarNoLeidas(1L)).thenReturn(0L);

        DashboardDTO result = dashboardService.obtenerDashboard(1L);

        assertTrue(result.horarioDiario().isEmpty());
        assertEquals(0, result.notificacionesNoLeidas());
    }
}
