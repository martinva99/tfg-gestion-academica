package com.ieslasencinas.gestionacademica.dto;

import com.ieslasencinas.gestionacademica.entity.enums.TipoFalta;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record FaltaAsistenciaCreateDTO(
        @NotNull(message = "El ID de la matrícula es obligatorio") Long idMatricula,
        @NotNull(message = "El ID de la sesión horaria es obligatorio") Long idSesion,
        @NotNull(message = "La fecha es obligatoria") LocalDate fecha,
        @NotNull(message = "El tipo de falta es obligatorio") TipoFalta tipo
) {
}
