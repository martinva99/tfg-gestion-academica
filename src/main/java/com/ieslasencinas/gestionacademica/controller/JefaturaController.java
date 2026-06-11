package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jefatura")
@RequiredArgsConstructor
public class JefaturaController {

    private final EvaluacionService evaluacionService;
    private final HorarioService horarioService;
    private final SesionHorariaService sesionHorariaService;


    @GetMapping("/evaluaciones")
    public ResponseEntity<List<EvaluacionResponseDTO>> listarEvaluaciones() {

        return ResponseEntity.ok(evaluacionService.buscarTodas());
    }

    @PatchMapping("/evaluaciones/{id}/estado")
    public ResponseEntity<EvaluacionResponseDTO> cambiarEstadoEvaluacion(@PathVariable Long id, @Valid @RequestBody EvaluacionEstadoDTO dto) {
        return ResponseEntity.ok(evaluacionService.cambiarEstado(id, dto));
    }


    @GetMapping("/horarios/grupo/{idGrupo}")
    public ResponseEntity<HorarioSemanalDTO> obtenerHorarioGrupo(@PathVariable Long idGrupo) {
        return ResponseEntity.ok(horarioService.obtenerHorarioSemanalGrupo(idGrupo));
    }


    @PostMapping("/sesiones")
    public ResponseEntity<SesionHorariaResponseDTO> crearSesion(@Valid @RequestBody SesionHorariaCreateDTO dto) {
        return new ResponseEntity<>(sesionHorariaService.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/sesiones/{id}")
    public ResponseEntity<SesionHorariaResponseDTO> actualizarSesion(@PathVariable Long id, @Valid @RequestBody SesionHorariaUpdateDTO dto) {
        return ResponseEntity.ok(sesionHorariaService.actualizar(id, dto));
    }

    @DeleteMapping("/sesiones/{id}")
    public ResponseEntity<Void> eliminarSesion(@PathVariable Long id) {
        sesionHorariaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/horarios/lote")
    public ResponseEntity<Void> crearSesionesLote(@Valid @RequestBody List<SesionHorariaCreateDTO> dtos) {
        sesionHorariaService.crearLote(dtos);
        return ResponseEntity.noContent().build();
    }
}
