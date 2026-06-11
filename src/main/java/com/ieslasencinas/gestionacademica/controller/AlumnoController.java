package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.service.AlumnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    @PostMapping("/asignar")
    public ResponseEntity<AlumnoResponseDTO> crearPerfil(@Valid @RequestBody AlumnoRegistroDTO dto) {
        return new ResponseEntity<>(alumnoService.crearPerfilAlumno(dto), HttpStatus.CREATED);
    }

    @GetMapping("/buscar")
    public ResponseEntity<AlumnoResponseDTO> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(alumnoService.buscarPorEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AlumnoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(alumnoService.buscarTodos());
    }

    @GetMapping("/horario")
    public ResponseEntity<List<SesionHorariaResponseDTO>> obtenerMiHorario() {
        return ResponseEntity.ok(alumnoService.obtenerMiHorario());
    }

    @GetMapping("/{id}/expediente")
    public ResponseEntity<AlumnoExpedienteDTO> obtenerExpediente(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.buscarPorIdExpediente(id));
    }

    @GetMapping("/expediente")
    public ResponseEntity<AlumnoExpedienteDTO> obtenerMiExpediente() {
        return ResponseEntity.ok(alumnoService.obtenerMiExpediente());
    }

    @GetMapping("/asistencia")
    public ResponseEntity<List<FaltaAsistenciaResponseDTO>> obtenerMiAsistencia() {
        return ResponseEntity.ok(alumnoService.obtenerMiAsistencia());
    }

    @GetMapping("/partes")
    public ResponseEntity<List<ParteResponseDTO>> obtenerMisPartes() {
        return ResponseEntity.ok(alumnoService.obtenerMisPartes());
    }

    @GetMapping("/notificaciones")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerMisNotificaciones() {
        return ResponseEntity.ok(alumnoService.obtenerMisNotificaciones());
    }
}
