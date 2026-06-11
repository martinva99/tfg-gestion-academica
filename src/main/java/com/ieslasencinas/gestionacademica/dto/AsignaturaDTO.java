package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record AsignaturaDTO(
        Long idAsignatura,
        @NotBlank(message = "El nombre de la asignatura es obligatorio") String nombre,
        String descripcion,
        @NotNull(message = "Las horas semanales son obligatorias") Double horasSemanales
) {
}
