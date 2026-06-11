package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.AsignaturaDTO;
import com.ieslasencinas.gestionacademica.dto.GrupoCreateDTO;
import com.ieslasencinas.gestionacademica.dto.GrupoResponseDTO;
import com.ieslasencinas.gestionacademica.service.GrupoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoController {
    private final GrupoService grupoService;

    @PostMapping
    public ResponseEntity<GrupoResponseDTO> crear(@Valid @RequestBody GrupoCreateDTO dto) {
        return new ResponseEntity<>(grupoService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(grupoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<GrupoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(grupoService.buscarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody GrupoCreateDTO dto) {
        return ResponseEntity.ok(grupoService.actualizar(id, dto));
    }

    @PostMapping("/{idGrupo}/asignaturas/{idAsignatura}")
    public ResponseEntity<Void> asignarAsignatura(@PathVariable Long idGrupo, @PathVariable Long idAsignatura) {
        grupoService.asignarAsignatura(idGrupo, idAsignatura);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idGrupo}/asignaturas/{idAsignatura}")
    public ResponseEntity<Void> desasignarAsignatura(@PathVariable Long idGrupo, @PathVariable Long idAsignatura) {
        grupoService.desasignarAsignatura(idGrupo, idAsignatura);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idGrupo}/asignaturas")
    public ResponseEntity<List<AsignaturaDTO>> obtenerAsignaturasDeGrupo(@PathVariable Long idGrupo) {
        GrupoResponseDTO grupo = grupoService.buscarPorId(idGrupo);
        return ResponseEntity.ok(grupo.asignaturas());
    }
}

