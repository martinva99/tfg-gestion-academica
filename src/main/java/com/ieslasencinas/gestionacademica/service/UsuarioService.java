package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.UsuarioRegistroDTO;
import com.ieslasencinas.gestionacademica.dto.UsuarioResponseDTO;
import com.ieslasencinas.gestionacademica.dto.UsuarioUpdateDTO;

import java.util.List;


public interface UsuarioService {

    UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO registroDTO);

    UsuarioResponseDTO buscarPorId(Long idUsuario);

    UsuarioResponseDTO buscarPorEmail(String email);

    List<UsuarioResponseDTO> buscarTodos();

    UsuarioResponseDTO actualizarUsuario(Long idUsuario, UsuarioUpdateDTO updateDTO);

    void desactivarUsuario(Long idUsuario);

    void asignarRol(Long idUsuario, String nombreRol);

    void revocarRol(Long idUsuario, String nombreRol);
}