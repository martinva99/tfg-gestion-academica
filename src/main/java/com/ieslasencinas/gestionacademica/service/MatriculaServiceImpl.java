package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.MatriculaCreateDTO;
import com.ieslasencinas.gestionacademica.dto.MatriculaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.exception.DuplicateResourceException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.MatriculaMapper;
import com.ieslasencinas.gestionacademica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlumnoRepository alumnoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final GrupoRepository grupoRepository;
    private final AnioAcademicoRepository anioAcademicoRepository;
    private final MatriculaMapper matriculaMapper;

    @Override
    @Transactional
    public MatriculaResponseDTO matricular(MatriculaCreateDTO dto) {

        if (matriculaRepository.existsByAlumno_IdAlumnoAndAsignatura_IdAsignaturaAndAnioAcademico_IdAnio(
                dto.idAlumno(), dto.idAsignatura(), dto.idAnio())) {
            throw new DuplicateResourceException("El alumno ya está matriculado en esta asignatura para el año indicado");
        }

        Alumno alumno = alumnoRepository.findById(dto.idAlumno())
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado con ID: " + dto.idAlumno()));
        Asignatura asignatura = asignaturaRepository.findById(dto.idAsignatura())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con ID: " + dto.idAsignatura()));
        Grupo grupo = grupoRepository.findById(dto.idGrupo())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado con ID: " + dto.idGrupo()));
        AnioAcademico anio = anioAcademicoRepository.findById(dto.idAnio())
                .orElseThrow(() -> new ResourceNotFoundException("Año Académico no encontrado con ID: " + dto.idAnio()));

        Matricula matricula = new Matricula();
        matricula.setAlumno(alumno);
        matricula.setAsignatura(asignatura);
        matricula.setGrupo(grupo);
        matricula.setAnioAcademico(anio);

        return matriculaMapper.toResponseDTO(matriculaRepository.save(matricula));
    }

    @Override
    @Transactional
    public void matricularLote(List<MatriculaCreateDTO> dtos) {
        for (MatriculaCreateDTO dto : dtos) {
            matricular(dto);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public MatriculaResponseDTO buscarPorId(Long idMatricula) {
        return matriculaRepository.findById(idMatricula)
                .map(matriculaMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con ID: " + idMatricula));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> buscarPorAlumno(Long idAlumno) {
        return matriculaRepository.findByAlumno_IdAlumno(idAlumno).stream()
                .map(matriculaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> buscarPorAlumnoYAnio(Long idAlumno, Long idAnio) {
        return matriculaRepository.findByAlumno_IdAlumnoAndAnioAcademico_IdAnio(idAlumno, idAnio).stream()
                .map(matriculaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long idMatricula) {
        if (!matriculaRepository.existsById(idMatricula)) {
            throw new ResourceNotFoundException("Matrícula no encontrada con ID: " + idMatricula);
        }
        matriculaRepository.deleteById(idMatricula);
    }
}
