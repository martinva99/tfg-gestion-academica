package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.LoginRequestDTO;
import com.ieslasencinas.gestionacademica.dto.LoginResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Rol;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import com.ieslasencinas.gestionacademica.security.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Autenticación")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Debe loguear correctamente y devolver JWT")
    void login_datosValidos_retornaJwt() {
        LoginRequestDTO dto = new LoginRequestDTO("test@test.com", "password");
        Authentication authentication = mock(Authentication.class);
        User userDetails = new User("test@test.com", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_DOCENTE")));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Test");
        usuario.setApellidos("Test");

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));
        when(jwtUtils.generateJwtToken(any(), any(), any(), any())).thenReturn("jwt-token");

        LoginResponseDTO result = authService.login(dto);

        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Debe permitir seleccionar perfil si tiene el rol")
    void selectProfile_rolValido_retornaJwt() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("test@test.com");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        Rol rol = new Rol();
        rol.setNombre("ROLE_DOCENTE");
        usuario.setRoles(Collections.singleton(rol));

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));
        when(jwtUtils.generateJwtToken(any(), any(), any(), any())).thenReturn("nuevo-jwt-token");

        LoginResponseDTO result = authService.selectProfile("ROLE_DOCENTE");

        assertNotNull(result);
        assertEquals("nuevo-jwt-token", result.token());
    }

    @Test
    @DisplayName("Debe lanzar excepcion si selecciona rol que no tiene")
    void selectProfile_rolInvalido_lanzaExcepcion() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("test@test.com");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setRoles(Collections.emptySet());

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));

        assertThrows(IllegalArgumentException.class, () -> authService.selectProfile("ROLE_DOCENTE"));
    }
}
