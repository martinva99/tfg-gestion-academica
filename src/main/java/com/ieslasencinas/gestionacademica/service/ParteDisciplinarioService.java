package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.ParteCreateDTO;
import com.ieslasencinas.gestionacademica.dto.ParteResponseDTO;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoParte;

import java.util.List;

public interface ParteDisciplinarioService {
    ParteResponseDTO crear(ParteCreateDTO dto);

    ParteResponseDTO buscarPorId(Long idParte);

    List<ParteResponseDTO> buscarPorAlumno(Long idAlumno);

    ParteResponseDTO actualizarEstado(Long idParte, EstadoParte nuevoEstado);
}
