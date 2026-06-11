package com.ieslasencinas.gestionacademica.dto;

import java.util.List;


public record DashboardDTO(
        List<SesionHorariaResponseDTO> horarioDiario,
        long notificacionesNoLeidas
) {
}
