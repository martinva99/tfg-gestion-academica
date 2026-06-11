package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.service.FaltaAsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faltas")
@RequiredArgsConstructor
public class FaltaAsistenciaController {
    private final FaltaAsistenciaService faltaAsistenciaService;

    @PostMapping
    public ResponseEntity<FaltaAsistenciaResponseDTO> registrar(@Valid @RequestBody FaltaAsistenciaCreateDTO dto) {
        return new ResponseEntity<>(faltaAsistenciaService.registrarFalta(dto), HttpStatus.CREATED);
    }

    @GetMapping("/matricula/{idMatricula}")
    public ResponseEntity<List<FaltaAsistenciaResponseDTO>> buscarPorMatricula(@PathVariable Long idMatricula) {
        return ResponseEntity.ok(faltaAsistenciaService.buscarPorMatricula(idMatricula));
    }

    @GetMapping("/matricula/{idMatricula}/resumen")
    public ResponseEntity<AsistenciaResumenDTO> resumen(@PathVariable Long idMatricula) {
        return ResponseEntity.ok(faltaAsistenciaService.calcularResumenAsistencia(idMatricula));
    }

    @PostMapping("/lote")
    public ResponseEntity<Void> registrarLote(@Valid @RequestBody List<FaltaAsistenciaCreateDTO> dtos) {
        faltaAsistenciaService.registrarFaltasLote(dtos);
        return ResponseEntity.noContent().build();
    }
}
