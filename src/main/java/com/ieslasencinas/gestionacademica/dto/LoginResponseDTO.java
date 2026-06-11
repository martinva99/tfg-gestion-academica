package com.ieslasencinas.gestionacademica.dto;

import java.util.Set;


public record LoginResponseDTO(
        String token,
        Long idUsuario,
        String nombre,
        String apellidos,
        Set<String> roles
) {
}
