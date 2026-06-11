package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuario_IdUsuarioOrderByFechaDesc(Long idUsuario);

    List<Notificacion> findByUsuario_IdUsuarioAndLeidaFalse(Long idUsuario);

    long countByUsuario_IdUsuarioAndLeidaFalse(Long idUsuario);
}
