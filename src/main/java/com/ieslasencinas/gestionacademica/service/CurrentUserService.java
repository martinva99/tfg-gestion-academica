package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.entity.Usuario;

public interface CurrentUserService {
    Usuario getUsuarioActual();

    String getEmailActual();

    String getRolActivo();

    boolean tieneRol(String rol);

    boolean hasAuthenticatedUser();
}
