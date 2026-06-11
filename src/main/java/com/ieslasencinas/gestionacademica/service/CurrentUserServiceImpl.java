package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public boolean hasAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    @Override
    public String getEmailActual() {
        if (!hasAuthenticatedUser()) {
            return null;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return auth.getName();
    }

    @Override
    public Usuario getUsuarioActual() {
        String email = getEmailActual();
        if (email == null) {
            throw new ResourceNotFoundException("No hay usuario autenticado en el contexto");
        }
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado en base de datos"));
    }

    @Override
    public String getRolActivo() {
        if (!hasAuthenticatedUser()) {
            return null;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse(null);
    }

    @Override
    public boolean tieneRol(String rol) {
        if (!hasAuthenticatedUser()) {
            return false;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(rol));
    }
}
