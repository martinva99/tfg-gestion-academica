package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Usuario Actual")
class CurrentUserServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private CurrentUserServiceImpl currentUserService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Debe detectar si hay un usuario autenticado")
    void hasAuthenticatedUser_autenticado_retornaTrue() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");

        assertTrue(currentUserService.hasAuthenticatedUser());
    }

    @Test
    @DisplayName("Debe obtener el email del usuario autenticado")
    void getEmailActual_autenticado_retornaEmail() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");
        when(authentication.getName()).thenReturn("user@test.com");

        assertEquals("user@test.com", currentUserService.getEmailActual());
    }

    @Test
    @DisplayName("Debe obtener la entidad Usuario del usuario autenticado")
    void getUsuarioActual_existente_retornaUsuario() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user@test.com");

        Usuario usuario = new Usuario();
        usuario.setEmail("user@test.com");
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));

        Usuario result = currentUserService.getUsuarioActual();

        assertNotNull(result);
        assertEquals("user@test.com", result.getEmail());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el usuario autenticado no está en BD")
    void getUsuarioActual_noEnBD_lanzaExcepcion() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user@test.com");
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> currentUserService.getUsuarioActual());
    }

    @Test
    @DisplayName("Debe obtener el rol activo")
    @SuppressWarnings("unchecked")
    void getRolActivo_conAutoridades_retornaRol() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_DOCENTE")))
                .when(authentication).getAuthorities();

        assertEquals("ROLE_DOCENTE", currentUserService.getRolActivo());
    }

    @Test
    @DisplayName("Debe verificar si el usuario tiene un rol específico")
    @SuppressWarnings("unchecked")
    void tieneRol_conRol_retornaTrue() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_DOCENTE")))
                .when(authentication).getAuthorities();

        assertTrue(currentUserService.tieneRol("ROLE_DOCENTE"));
        assertFalse(currentUserService.tieneRol("ROLE_ADMINISTRADOR"));
    }
}
