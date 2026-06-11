package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;
import com.ieslasencinas.gestionacademica.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService docenteService;
    private final CurrentUserService currentUserService;
    private final HorarioService horarioService;
    private final FaltaAsistenciaService faltaAsistenciaService;
    private final NotificacionService notificacionService;

    @PostMapping("/asignar")
    public ResponseEntity<Void> asignarDocente(@Valid @RequestBody DocenteRegistroDTO dto) {
        docenteService.asignarPerfilDocente(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(docenteService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<DocenteResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(docenteService.buscarTodos());
    }

    @GetMapping("/sesiones/hoy")
    public ResponseEntity<List<SesionHorariaResponseDTO>> obtenerSesionesHoy() {
        Usuario usuario = currentUserService.getUsuarioActual();
        DiaSemana hoy = DiaSemana.values()[LocalDate.now().getDayOfWeek().getValue() - 1];
        return ResponseEntity.ok(horarioService.obtenerHorarioDiario(usuario.getIdUsuario(), hoy));
    }

    @GetMapping("/horario")
    public ResponseEntity<List<SesionHorariaResponseDTO>> obtenerHorarioCompleto() {
        Usuario usuario = currentUserService.getUsuarioActual();
        return ResponseEntity.ok(horarioService.obtenerHorarioSemanal(usuario.getIdUsuario()));
    }

    @GetMapping("/asistencia/sesion/{idSesion}")
    public ResponseEntity<List<FaltaAsistenciaResponseDTO>> obtenerAsistenciaSesion(@PathVariable Long idSesion) {
        return ResponseEntity.ok(faltaAsistenciaService.buscarPorSesion(idSesion));
    }

    @GetMapping("/calificaciones/asignatura/{idAsignatura}/grupo/{idGrupo}")
    public ResponseEntity<List<CalificacionResponseDTO>> obtenerCalificacionesAsignaturaGrupo(
            @PathVariable Long idAsignatura, @PathVariable Long idGrupo, @RequestParam Integer trimestre) {
        return ResponseEntity.ok(docenteService.obtenerCalificacionesPorAsignaturaYGrupo(idAsignatura, idGrupo, trimestre));
    }

    @GetMapping("/notificaciones")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificaciones() {
        Usuario usuario = currentUserService.getUsuarioActual();
        return ResponseEntity.ok(notificacionService.buscarPorUsuario(usuario.getIdUsuario()));
    }

    @GetMapping("/sesiones/{idSesion}/alumnos")
    public ResponseEntity<List<MatriculaResponseDTO>> obtenerAlumnosPorSesion(@PathVariable Long idSesion) {
        return ResponseEntity.ok(docenteService.obtenerAlumnosPorSesion(idSesion));
    }

    @GetMapping("/mis-grupos")
    public ResponseEntity<List<GrupoAsignaturaDTO>> obtenerMisGrupos() {
        Usuario usuario = currentUserService.getUsuarioActual();
        return ResponseEntity.ok(docenteService.obtenerGruposYAsignaturas(usuario.getIdUsuario()));
    }

    @GetMapping("/partes")
    public ResponseEntity<List<ParteResponseDTO>> obtenerMisPartes() {
        Usuario usuario = currentUserService.getUsuarioActual();
        return ResponseEntity.ok(docenteService.obtenerPartesPorDocente(usuario.getIdUsuario()));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<DocenteResponseDTO>> obtenerDocentesDisponibles(@RequestParam Long idFranja) {
        return ResponseEntity.ok(docenteService.obtenerDocentesDisponibles(idFranja));
    }
}