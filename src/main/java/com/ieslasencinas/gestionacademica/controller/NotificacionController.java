package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.NotificacionResponseDTO;
import com.ieslasencinas.gestionacademica.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionService notificacionService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacionResponseDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(notificacionService.buscarPorUsuario(idUsuario));
    }

    @PatchMapping("/{id}/leida")
    public ResponseEntity<Void> marcarComoLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/usuario/{idUsuario}/leidas")
    public ResponseEntity<Void> marcarTodasComoLeidas(@PathVariable Long idUsuario) {
        notificacionService.marcarTodasComoLeidas(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
