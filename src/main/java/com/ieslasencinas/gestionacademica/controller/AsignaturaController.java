package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.AsignaturaDTO;
import com.ieslasencinas.gestionacademica.service.AsignaturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
@RequiredArgsConstructor
public class AsignaturaController {
    private final AsignaturaService asignaturaService;

    @PostMapping
    public ResponseEntity<AsignaturaDTO> crear(@Valid @RequestBody AsignaturaDTO dto) {
        return new ResponseEntity<>(asignaturaService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AsignaturaDTO>> buscarTodos() {
        return ResponseEntity.ok(asignaturaService.buscarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AsignaturaDTO dto) {
        return ResponseEntity.ok(asignaturaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asignaturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
