package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.LoginRequestDTO;
import com.ieslasencinas.gestionacademica.dto.LoginResponseDTO;


public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO dto);

    LoginResponseDTO selectProfile(String rolActivo);
}
