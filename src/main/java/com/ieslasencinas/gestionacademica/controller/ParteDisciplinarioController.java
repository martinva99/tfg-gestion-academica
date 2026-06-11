package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.ParteCreateDTO;
import com.ieslasencinas.gestionacademica.dto.ParteResponseDTO;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoParte;
import com.ieslasencinas.gestionacademica.service.ParteDisciplinarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partes")
@RequiredArgsConstructor
public class ParteDisciplinarioController {
    private final ParteDisciplinarioService parteDisciplinarioService;

    @PostMapping
    public ResponseEntity<ParteResponseDTO> crear(@Valid @RequestBody ParteCreateDTO dto) {
        return new ResponseEntity<>(parteDisciplinarioService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(parteDisciplinarioService.buscarPorId(id));
    }

    @GetMapping("/alumno/{idAlumno}")
    public ResponseEntity<List<ParteResponseDTO>> buscarPorAlumno(@PathVariable Long idAlumno) {
        return ResponseEntity.ok(parteDisciplinarioService.buscarPorAlumno(idAlumno));
    }
}
