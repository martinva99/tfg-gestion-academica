package com.ieslasencinas.gestionacademica.dto;

import java.util.List;


public record TutorDashboardDTO(
        List<HijoResumenDTO> hijos,
        List<NotificacionResponseDTO> notificacionesRecientes,
        long notificacionesNoLeidas
) {
}
