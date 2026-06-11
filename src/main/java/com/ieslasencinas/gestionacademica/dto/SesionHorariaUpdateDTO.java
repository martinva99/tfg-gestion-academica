package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotNull;


public record SesionHorariaUpdateDTO(
        @NotNull(message = "El ID del grupo es obligatorio") Long idGrupo,
        @NotNull(message = "El ID del docente es obligatorio") Long idDocente,
        @NotNull(message = "El ID de la asignatura es obligatorio") Long idAsignatura,
        @NotNull(message = "El ID de la franja horaria es obligatorio") Long idFranja,
        String aula
) {
}
