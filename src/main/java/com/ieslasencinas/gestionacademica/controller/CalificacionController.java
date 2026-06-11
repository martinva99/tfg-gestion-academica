package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {
    private final CalificacionService calificacionService;

    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> registrar(@Valid @RequestBody CalificacionCreateDTO dto) {
        return new ResponseEntity<>(calificacionService.registrar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/evaluacion/{idEvaluacion}")
    public ResponseEntity<List<CalificacionResponseDTO>> buscarPorEvaluacion(@PathVariable Long idEvaluacion) {
        return ResponseEntity.ok(calificacionService.buscarPorEvaluacion(idEvaluacion));
    }

    @GetMapping("/matricula/{idMatricula}")
    public ResponseEntity<List<CalificacionResponseDTO>> buscarPorMatricula(@PathVariable Long idMatricula) {
        return ResponseEntity.ok(calificacionService.buscarPorMatricula(idMatricula));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CalificacionUpdateDTO dto) {
        return ResponseEntity.ok(calificacionService.actualizar(id, dto));
    }

    @PostMapping("/lote")
    public ResponseEntity<Void> registrarLote(@Valid @RequestBody List<CalificacionCreateDTO> dtos) {
        calificacionService.registrarCalificacionesLote(dtos);
        return ResponseEntity.noContent().build();
    }
}
