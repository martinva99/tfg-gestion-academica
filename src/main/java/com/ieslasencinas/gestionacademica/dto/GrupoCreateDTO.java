package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;


public record GrupoCreateDTO(
        @NotBlank(message = "El nombre del grupo es obligatorio") String nombre,
        @NotNull(message = "El ID del año académico es obligatorio") Long idAnio,
        @NotNull(message = "El ID del centro es obligatorio") Long idCentro,
        Set<Long> asignaturasIds
) {
}
