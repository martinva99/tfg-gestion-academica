package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.CentroDTO;
import com.ieslasencinas.gestionacademica.service.CentroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centros")
@RequiredArgsConstructor
public class CentroController {
    private final CentroService centroService;

    @PostMapping
    public ResponseEntity<CentroDTO> crear(@Valid @RequestBody CentroDTO dto) {
        return new ResponseEntity<>(centroService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(centroService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CentroDTO>> buscarTodos() {
        return ResponseEntity.ok(centroService.buscarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CentroDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CentroDTO dto) {
        return ResponseEntity.ok(centroService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        centroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
