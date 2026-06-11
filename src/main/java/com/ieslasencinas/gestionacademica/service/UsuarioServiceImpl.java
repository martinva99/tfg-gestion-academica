package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.UsuarioRegistroDTO;
import com.ieslasencinas.gestionacademica.dto.UsuarioResponseDTO;
import com.ieslasencinas.gestionacademica.dto.UsuarioUpdateDTO;
import com.ieslasencinas.gestionacademica.entity.Rol;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.exception.DuplicateResourceException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.UsuarioMapper;
import com.ieslasencinas.gestionacademica.repository.RolRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO registroDTO) {
        if (usuarioRepository.existsByEmail(registroDTO.email())) {
            throw new DuplicateResourceException("El email ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(registroDTO);
        usuario.setContraseniaHash(passwordEncoder.encode(registroDTO.password()));

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(usuarioMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> buscarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long idUsuario, UsuarioUpdateDTO updateDTO) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (updateDTO.nombre() != null) usuario.setNombre(updateDTO.nombre());
        if (updateDTO.apellidos() != null) usuario.setApellidos(updateDTO.apellidos());
        if (updateDTO.telefonoContacto() != null) usuario.setTelefonoContacto(updateDTO.telefonoContacto());
        if (updateDTO.activo() != null) usuario.setActivo(updateDTO.activo());
        if (updateDTO.nuevaPassword() != null && !updateDTO.nuevaPassword().isBlank()) {
            usuario.setContraseniaHash(passwordEncoder.encode(updateDTO.nuevaPassword()));
        }

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void desactivarUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void asignarRol(Long idUsuario, String nombreRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + nombreRol));

        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void revocarRol(Long idUsuario, String nombreRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.getRoles().removeIf(rol -> rol.getNombre().equals(nombreRol));
        usuarioRepository.save(usuario);
    }
}