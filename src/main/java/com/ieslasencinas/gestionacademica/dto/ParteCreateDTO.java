package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.TipoParte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ParteCreateDTO(
        @NotNull(message = "El ID de la matrícula es obligatorio") Long idMatricula,
        @NotNull(message = "La fecha es obligatoria") LocalDate fecha,
        @NotNull(message = "El tipo de parte es obligatorio") TipoParte tipo,
        @NotBlank(message = "La descripción es obligatoria") String descripcion
) {
}