package com.ieslasencinas.gestionacademica.dto;


public record DocenteResponseDTO(
        Long idDocente,
        String nombre,
        String apellidos,
        String email,
        String especialidad
) {
}
