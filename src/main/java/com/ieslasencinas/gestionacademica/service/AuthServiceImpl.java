package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.LoginRequestDTO;
import com.ieslasencinas.gestionacademica.dto.LoginResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import com.ieslasencinas.gestionacademica.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());


        Usuario usuario = usuarioRepository.findByEmail(dto.email()).orElseThrow();

        String rolActivo = null;
        if (roles.size() == 1) {
            rolActivo = roles.iterator().next();
        }

        String jwt = jwtUtils.generateJwtToken(authentication, usuario.getIdUsuario(), roles, rolActivo);


        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return new LoginResponseDTO(
                jwt,
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellidos(),
                roles
        );
    }

    @Override
    @Transactional
    public LoginResponseDTO selectProfile(String rolActivo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow();
        Set<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toSet());

        if (!roles.contains(rolActivo)) {
            throw new IllegalArgumentException("El usuario no tiene el rol especificado");
        }

        String jwt = jwtUtils.generateJwtToken(authentication, usuario.getIdUsuario(), roles, rolActivo);

        return new LoginResponseDTO(
                jwt,
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellidos(),
                roles
        );
    }
}
