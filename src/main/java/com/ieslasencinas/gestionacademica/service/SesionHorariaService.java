package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.SesionHorariaCreateDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaUpdateDTO;

import java.util.List;

public interface SesionHorariaService {
    SesionHorariaResponseDTO crear(SesionHorariaCreateDTO dto);

    SesionHorariaResponseDTO buscarPorId(Long idSesion);

    List<SesionHorariaResponseDTO> buscarPorGrupo(Long idGrupo);

    List<SesionHorariaResponseDTO> buscarPorDocente(Long idDocente);

    SesionHorariaResponseDTO actualizar(Long idSesion, SesionHorariaUpdateDTO dto);

    void eliminar(Long idSesion);

    void crearLote(List<SesionHorariaCreateDTO> dtos);
}
