package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.AnioAcademicoDTO;
import com.ieslasencinas.gestionacademica.service.AnioAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anios-academicos")
@RequiredArgsConstructor
public class AnioAcademicoController {
    private final AnioAcademicoService anioAcademicoService;

    @PostMapping
    public ResponseEntity<AnioAcademicoDTO> crear(@Valid @RequestBody AnioAcademicoDTO dto) {
        return new ResponseEntity<>(anioAcademicoService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnioAcademicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(anioAcademicoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AnioAcademicoDTO>> buscarTodos() {
        return ResponseEntity.ok(anioAcademicoService.buscarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnioAcademicoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AnioAcademicoDTO dto) {
        return ResponseEntity.ok(anioAcademicoService.actualizar(id, dto));
    }
}
