package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.TutorLegalRegistroDTO;
import com.ieslasencinas.gestionacademica.dto.TutorLegalResponseDTO;
import com.ieslasencinas.gestionacademica.service.TutorLegalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutores-legales")
@RequiredArgsConstructor
public class TutorLegalController {

    private final TutorLegalService tutorLegalService;

    @PostMapping("/asignar")
    public ResponseEntity<TutorLegalResponseDTO> crearPerfil(@Valid @RequestBody TutorLegalRegistroDTO dto) {
        return new ResponseEntity<>(tutorLegalService.crearPerfilTutor(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorLegalResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tutorLegalService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TutorLegalResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(tutorLegalService.buscarTodos());
    }

    @GetMapping("/resumen")
    public ResponseEntity<com.ieslasencinas.gestionacademica.dto.TutorDashboardDTO> obtenerResumenDashboard() {
        return ResponseEntity.ok(tutorLegalService.obtenerResumenDashboard());
    }

    @GetMapping("/horarios")
    public ResponseEntity<List<com.ieslasencinas.gestionacademica.dto.TutorAgregadosDTO.HijoHorarioDTO>> obtenerHorariosAgrupados() {
        return ResponseEntity.ok(tutorLegalService.obtenerHorariosAgrupados());
    }

    @GetMapping("/expedientes")
    public ResponseEntity<List<com.ieslasencinas.gestionacademica.dto.TutorAgregadosDTO.HijoExpedienteDTO>> obtenerExpedientesAgrupados() {
        return ResponseEntity.ok(tutorLegalService.obtenerExpedientesAgrupados());
    }

    @GetMapping("/asistencia")
    public ResponseEntity<List<com.ieslasencinas.gestionacademica.dto.TutorAgregadosDTO.HijoAsistenciaDTO>> obtenerAsistenciaAgrupada() {
        return ResponseEntity.ok(tutorLegalService.obtenerAsistenciaAgrupada());
    }

    @GetMapping("/partes")
    public ResponseEntity<List<com.ieslasencinas.gestionacademica.dto.TutorAgregadosDTO.HijoParteDTO>> obtenerPartesAgrupados() {
        return ResponseEntity.ok(tutorLegalService.obtenerPartesAgrupados());
    }
}
