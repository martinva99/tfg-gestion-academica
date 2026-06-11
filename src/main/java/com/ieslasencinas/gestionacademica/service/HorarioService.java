package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.HorarioSemanalDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;

import java.util.List;


public interface HorarioService {
    HorarioSemanalDTO obtenerHorarioSemanalGrupo(Long idGrupo);

    HorarioSemanalDTO obtenerHorarioSemanalDocente(Long idDocente);

    List<SesionHorariaResponseDTO> obtenerHorarioDiario(Long idUsuario, DiaSemana dia);

    List<SesionHorariaResponseDTO> obtenerHorarioSemanal(Long idUsuario);
}
