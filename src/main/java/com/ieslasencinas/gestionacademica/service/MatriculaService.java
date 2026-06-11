package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.MatriculaCreateDTO;
import com.ieslasencinas.gestionacademica.dto.MatriculaResponseDTO;

import java.util.List;

public interface MatriculaService {
    MatriculaResponseDTO matricular(MatriculaCreateDTO dto);

    void matricularLote(List<MatriculaCreateDTO> dtos);

    MatriculaResponseDTO buscarPorId(Long idMatricula);

    List<MatriculaResponseDTO> buscarPorAlumno(Long idAlumno);

    List<MatriculaResponseDTO> buscarPorAlumnoYAnio(Long idAlumno, Long idAnio);

    void eliminar(Long idMatricula);
}
