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
@RequestMapping("/api/secretaria")
@RequiredArgsConstructor
public class SecretariaController {

    private final MatriculaService matriculaService;
    private final AlumnoService alumnoService;
    private final FaltaAsistenciaService faltaAsistenciaService;


    @PostMapping("/matriculas")
    public ResponseEntity<MatriculaResponseDTO> matricular(@Valid @RequestBody MatriculaCreateDTO dto) {
        return new ResponseEntity<>(matriculaService.matricular(dto), HttpStatus.CREATED);
    }

    @PostMapping("/matriculas/lote")
    public ResponseEntity<Void> matricularLote(@Valid @RequestBody List<MatriculaCreateDTO> dto) {
        matriculaService.matricularLote(dto);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/vinculaciones")
    public ResponseEntity<Void> vincularAlumnoTutor(@Valid @RequestBody VinculacionAlumnoTutorDTO dto) {
        alumnoService.vincularTutorLegal(dto);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/asistencia/{id}/justificar")
    public ResponseEntity<FaltaAsistenciaResponseDTO> justificarFalta(@PathVariable Long id, @Valid @RequestBody FaltaAsistenciaJustificarDTO dto) {
        return ResponseEntity.ok(faltaAsistenciaService.justificarFalta(id, dto));
    }
}
