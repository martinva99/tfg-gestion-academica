package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CentroDTO(
        Long idCentro,
        @NotBlank(message = "El nombre del centro es obligatorio") String nombre,
        @NotBlank(message = "La dirección es obligatoria") String direccion,
        @NotBlank(message = "El teléfono es obligatorio") String telefono
) {
}
