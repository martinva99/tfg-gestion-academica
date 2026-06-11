package com.ieslasencinas.gestionacademica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UsuarioUpdateDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "Los apellidos son obligatorios") String apellidos,
        String telefonoContacto,
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String nuevaPassword,
        Boolean activo
) {
}
