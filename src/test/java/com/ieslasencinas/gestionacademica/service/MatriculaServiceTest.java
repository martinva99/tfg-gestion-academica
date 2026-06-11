package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.MatriculaCreateDTO;
import com.ieslasencinas.gestionacademica.dto.MatriculaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.exception.DuplicateResourceException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.MatriculaMapper;
import com.ieslasencinas.gestionacademica.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Matrícula")
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private AsignaturaRepository asignaturaRepository;
    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private AnioAcademicoRepository anioAcademicoRepository;
    @Mock
    private MatriculaMapper matriculaMapper;
    @InjectMocks
    private MatriculaServiceImpl matriculaService;

    @Test
    @DisplayName("Debe matricular un alumno correctamente cuando no hay matrícula duplicada")
    void matricular_sinDuplicado_creaMatricula() {
        MatriculaCreateDTO dto = new MatriculaCreateDTO(1L, 1L, 1L, 1L);
        when(matriculaRepository.existsByAlumno_IdAlumnoAndAsignatura_IdAsignaturaAndAnioAcademico_IdAnio(1L, 1L, 1L)).thenReturn(false);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(new Alumno()));
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(new Asignatura()));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(new Grupo()));
        when(anioAcademicoRepository.findById(1L)).thenReturn(Optional.of(new AnioAcademico()));
        when(matriculaRepository.save(any())).thenReturn(new Matricula());
        when(matriculaMapper.toResponseDTO(any())).thenReturn(mock(MatriculaResponseDTO.class));

        MatriculaResponseDTO result = matriculaService.matricular(dto);

        assertNotNull(result);
        verify(matriculaRepository).save(any());
    }

    @Test
    @DisplayName("Debe lanzar DuplicateResourceException cuando la matrícula ya existe (RN4)")
    void matricular_duplicada_lanzaExcepcion() {
        MatriculaCreateDTO dto = new MatriculaCreateDTO(1L, 1L, 1L, 1L);
        when(matriculaRepository.existsByAlumno_IdAlumnoAndAsignatura_IdAsignaturaAndAnioAcademico_IdAnio(1L, 1L, 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> matriculaService.matricular(dto));
    }

    @Test
    @DisplayName("Debe encontrar una matrícula por su ID")
    void buscarPorId_existente_retornaDTO() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(new Matricula()));
        when(matriculaMapper.toResponseDTO(any())).thenReturn(mock(MatriculaResponseDTO.class));

        MatriculaResponseDTO result = matriculaService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al buscar matrícula inexistente")
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> matriculaService.buscarPorId(1L));
    }

    @Test
    @DisplayName("Debe retornar las matrículas de un alumno")
    void buscarPorAlumno_conMatriculas_retornaLista() {
        when(matriculaRepository.findByAlumno_IdAlumno(1L)).thenReturn(Collections.singletonList(new Matricula()));
        when(matriculaMapper.toResponseDTO(any())).thenReturn(mock(MatriculaResponseDTO.class));

        List<MatriculaResponseDTO> result = matriculaService.buscarPorAlumno(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe eliminar una matrícula correctamente")
    void eliminar_existente_eliminaMatricula() {
        when(matriculaRepository.existsById(1L)).thenReturn(true);

        matriculaService.eliminar(1L);

        verify(matriculaRepository).deleteById(1L);
    }
}
