package com.ieslasencinas.gestionacademica.controller;


import com.ieslasencinas.gestionacademica.dto.FranjaHorariaDTO;
import com.ieslasencinas.gestionacademica.repository.FranjaHorariaRepository;
import com.ieslasencinas.gestionacademica.service.FranjaHorariaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/franjas-horarias")
@RequiredArgsConstructor
public class FranjaHorariaController {

    private final FranjaHorariaService franjaHorariaService;

    @GetMapping
    public ResponseEntity<List<FranjaHorariaDTO>> obtenerTodas() {
        return ResponseEntity.ok(franjaHorariaService.buscarTodos());
    }
}
