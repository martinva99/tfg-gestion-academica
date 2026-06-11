package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotNull;


public record TutorLegalRegistroDTO(
        @NotNull(message = "El ID del usuario es obligatorio") Long idUsuario
) {
}
