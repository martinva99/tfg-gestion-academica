package com.ieslasencinas.gestionacademica.dto;

import java.time.LocalDateTime;
import java.util.Set;


public record UsuarioResponseDTO(
        Long idUsuario,
        String nombre,
        String apellidos,
        String email,
        String telefonoContacto,
        boolean activo,
        LocalDateTime ultimoAcceso,
        Set<String> roles
) {
}