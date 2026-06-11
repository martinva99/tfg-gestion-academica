package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.LoginRequestDTO;
import com.ieslasencinas.gestionacademica.dto.LoginResponseDTO;
import com.ieslasencinas.gestionacademica.dto.SelectProfileRequestDTO;
import com.ieslasencinas.gestionacademica.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/select-profile")
    public ResponseEntity<LoginResponseDTO> selectProfile(@Valid @RequestBody SelectProfileRequestDTO dto) {
        return ResponseEntity.ok(authService.selectProfile(dto.rolActivo()));
    }
}
