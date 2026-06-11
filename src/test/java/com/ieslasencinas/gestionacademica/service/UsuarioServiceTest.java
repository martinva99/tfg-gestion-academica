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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Usuario")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    @DisplayName("Debe registrar un usuario correctamente cuando los datos son válidos")
    void registrarUsuario_datosValidos_retornaDTO() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO("test@email.com", "Apellidos", "Nombre", "password", "123456789");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioMapper.toEntity(any())).thenReturn(new Usuario());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(usuarioRepository.save(any())).thenReturn(new Usuario());
        when(usuarioMapper.toResponseDTO(any())).thenReturn(mock(UsuarioResponseDTO.class));

        UsuarioResponseDTO result = usuarioService.registrarUsuario(dto);

        assertNotNull(result);
        verify(usuarioRepository).save(any());
        verify(passwordEncoder).encode("password");
    }

    @Test
    @DisplayName("Debe lanzar DuplicateResourceException cuando el email ya existe")
    void registrarUsuario_emailDuplicado_lanzaExcepcion() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO("test@email.com", "Apellidos", "Nombre", "password", "123456789");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.registrarUsuario(dto));
    }

    @Test
    @DisplayName("Debe encontrar un usuario por su ID cuando existe")
    void buscarPorId_existente_retornaDTO() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(usuarioMapper.toResponseDTO(any())).thenReturn(mock(UsuarioResponseDTO.class));

        UsuarioResponseDTO result = usuarioService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el usuario no existe")
    void buscarPorId_noExistente_lanzaExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarPorId(1L));
    }

    @Test
    @DisplayName("Debe retornar la lista de todos los usuarios")
    void buscarTodos_retornaLista() {
        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(new Usuario()));
        when(usuarioMapper.toResponseDTO(any())).thenReturn(mock(UsuarioResponseDTO.class));

        List<UsuarioResponseDTO> result = usuarioService.buscarTodos();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe actualizar los datos de un usuario correctamente")
    void actualizarUsuario_datosValidos_retornaDTO() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Original");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO("Nuevo", "Apellidos", "987654321", null, true);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(any())).thenReturn(mock(UsuarioResponseDTO.class));

        usuarioService.actualizarUsuario(1L, dto);

        assertEquals("Nuevo", usuario.getNombre());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Debe desactivar un usuario correctamente poniendo activo a false")
    void desactivarUsuario_existente_cambiaActivoAFalse() {
        Usuario usuario = new Usuario();
        usuario.setActivo(true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.desactivarUsuario(1L);

        assertFalse(usuario.isActivo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Debe asignar un rol a un usuario correctamente")
    void asignarRol_existente_agregaRol() {
        Usuario usuario = new Usuario();
        usuario.setRoles(new HashSet<>());
        Rol rol = new Rol();
        rol.setNombre("ADMIN");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(rol));

        usuarioService.asignarRol(1L, "ADMIN");

        assertTrue(usuario.getRoles().contains(rol));
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Debe revocar un rol a un usuario correctamente")
    void revocarRol_existente_eliminaRol() {
        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        rol.setNombre("ADMIN");
        usuario.getRoles().add(rol);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.revocarRol(1L, "ADMIN");

        assertTrue(usuario.getRoles().isEmpty());
        verify(usuarioRepository).save(usuario);
    }
}
