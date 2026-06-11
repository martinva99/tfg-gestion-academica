package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.DashboardDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final HorarioService horarioService;
    private final NotificacionService notificacionService;
    private final UsuarioRepository usuarioRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO obtenerDashboard(Long idUsuario) {
        long notificacionesPendientes = notificacionService.contarNoLeidas(idUsuario);

        boolean showSchedule = true;
        if (currentUserService.hasAuthenticatedUser()) {
            boolean hasNonScheduleRole = currentUserService.tieneRol("ROLE_ADMINISTRADOR") ||
                    currentUserService.tieneRol("ROLE_SECRETARIA") ||
                    currentUserService.tieneRol("ROLE_JEFATURA");
            if (hasNonScheduleRole) {
                showSchedule = false;
            }
        }

        List<SesionHorariaResponseDTO> horarioHoy = java.util.Collections.emptyList();
        if (showSchedule) {
            DiaSemana diaHoy = DiaSemana.values()[LocalDate.now().getDayOfWeek().getValue() - 1];
            horarioHoy = horarioService.obtenerHorarioDiario(idUsuario, diaHoy);
        }

        return new DashboardDTO(horarioHoy, notificacionesPendientes);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO obtenerDashboardAutenticado() {
        Usuario usuario = currentUserService.getUsuarioActual();
        return obtenerDashboard(usuario.getIdUsuario());
    }
}
