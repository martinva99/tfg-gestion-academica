package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record AnioAcademicoDTO(
        Long idAnio,
        @NotBlank(message = "El curso escolar es obligatorio") String cursoEscolar,
        @NotNull(message = "La fecha de inicio es obligatoria") LocalDate fechaInicio,
        @NotNull(message = "La fecha de fin es obligatoria") LocalDate fechaFin
) {
}
