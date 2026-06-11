package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record DocenteRegistroDTO(

        @NotNull(message = "El ID del usuario es obligatorio")
        Long idUsuario,

        @NotBlank(message = "La especialidad es obligatoria")
        String especialidad
) {
}