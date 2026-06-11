package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.UsuarioRegistroDTO;
import com.ieslasencinas.gestionacademica.dto.UsuarioResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Rol;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getTelefonoContacto(),
                usuario.isActivo(),
                usuario.getUltimoAcceso(),
                usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet())
        );
    }

    public Usuario toEntity(UsuarioRegistroDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setApellidos(dto.apellidos());
        usuario.setEmail(dto.email());
        usuario.setContraseniaHash(dto.password());
        usuario.setTelefonoContacto(dto.telefonoContacto());
        usuario.setActivo(true);
        return usuario;
    }
}
