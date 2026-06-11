package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.DashboardDTO;
import com.ieslasencinas.gestionacademica.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDTO> obtenerDashboard() {
        return ResponseEntity.ok(dashboardService.obtenerDashboardAutenticado());
    }
}
