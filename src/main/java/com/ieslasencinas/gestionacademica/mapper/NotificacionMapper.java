package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.NotificacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Notificacion;
import org.springframework.stereotype.Component;


@Component
public class NotificacionMapper {

    public NotificacionResponseDTO toResponseDTO(Notificacion notificacion) {
        if (notificacion == null) return null;

        return new NotificacionResponseDTO(
                notificacion.getIdNotificacion(),
                notificacion.getMensaje(),
                notificacion.getFecha(),
                notificacion.isLeida()
        );
    }
}
