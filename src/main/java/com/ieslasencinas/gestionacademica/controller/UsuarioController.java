package com.ieslasencinas.gestionacademica.controller;

import com.ieslasencinas.gestionacademica.dto.UsuarioRegistroDTO;
import com.ieslasencinas.gestionacademica.dto.UsuarioResponseDTO;
import com.ieslasencinas.gestionacademica.dto.UsuarioUpdateDTO;
import com.ieslasencinas.gestionacademica.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO registroDTO) {
        return new ResponseEntity<>(usuarioService.registrarUsuario(registroDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/roles/{nombreRol}")
    public ResponseEntity<Void> asignarRol(@PathVariable Long id, @PathVariable String nombreRol) {
        usuarioService.asignarRol(id, nombreRol);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/roles/{nombreRol}")
    public ResponseEntity<Void> revocarRol(@PathVariable Long id, @PathVariable String nombreRol) {
        usuarioService.revocarRol(id, nombreRol);
        return ResponseEntity.noContent().build();
    }
}