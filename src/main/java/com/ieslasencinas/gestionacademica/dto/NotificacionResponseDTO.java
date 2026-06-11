package com.ieslasencinas.gestionacademica.dto;

import java.time.LocalDateTime;


public record NotificacionResponseDTO(
        Long idNotificacion,
        String mensaje,
        LocalDateTime fecha,
        boolean leida
) {
}
