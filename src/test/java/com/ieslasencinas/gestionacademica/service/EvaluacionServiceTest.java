package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.EvaluacionEstadoDTO;
import com.ieslasencinas.gestionacademica.dto.EvaluacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoEvaluacion;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.EvaluacionMapper;
import com.ieslasencinas.gestionacademica.repository.EvaluacionRepository;
import com.ieslasencinas.gestionacademica.repository.GrupoRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.SesionHorariaRepository;
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
@DisplayName("Tests del servicio de Evaluación")
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;
    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private SesionHorariaRepository sesionHorariaRepository;
    @Mock
    private EvaluacionMapper evaluacionMapper;
    @Mock
    private NotificacionService notificacionService;
    @InjectMocks
    private EvaluacionServiceImpl evaluacionService;

    @Test
    @DisplayName("Debe encontrar una evaluación por su ID")
    void buscarPorId_existente_retornaDTO() {
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(new Evaluacion()));
        when(evaluacionMapper.toResponseDTO(any())).thenReturn(mock(EvaluacionResponseDTO.class));

        EvaluacionResponseDTO result = evaluacionService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando la evaluación no existe")
    void buscarPorId_noExistente_lanzaExcepcion() {
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> evaluacionService.buscarPorId(1L));
    }

    @Test
    @DisplayName("Debe retornar las evaluaciones de un grupo")
    void buscarPorGrupo_conEvaluaciones_retornaLista() {
        when(evaluacionRepository.findByGrupo_IdGrupo(1L)).thenReturn(Collections.singletonList(new Evaluacion()));
        when(evaluacionMapper.toResponseDTO(any())).thenReturn(mock(EvaluacionResponseDTO.class));

        List<EvaluacionResponseDTO> result = evaluacionService.buscarPorGrupo(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe cambiar el estado de una evaluación de ABIERTA a CERRADA y notificar (RN5)")
    void cambiarEstado_abiertaACerrada_actualizaEstadoYNotifica() {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setIdEvaluacion(1L);
        evaluacion.setEstado(EstadoEvaluacion.ABIERTA);
        evaluacion.setTrimestre(1);
        Grupo grupo = new Grupo();
        grupo.setIdGrupo(1L);
        evaluacion.setGrupo(grupo);

        EvaluacionEstadoDTO dto = new EvaluacionEstadoDTO(EstadoEvaluacion.CERRADA);

        Matricula matricula = new Matricula();
        Alumno alumno = new Alumno();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10L);
        alumno.setUsuario(usuario);
        matricula.setAlumno(alumno);
        matricula.setGrupo(grupo);

        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(evaluacionRepository.save(any())).thenReturn(evaluacion);
        when(evaluacionMapper.toResponseDTO(any())).thenReturn(mock(EvaluacionResponseDTO.class));
        when(sesionHorariaRepository.findByGrupo_IdGrupo(1L)).thenReturn(Collections.emptyList());

        EvaluacionResponseDTO result = evaluacionService.cambiarEstado(1L, dto);

        assertEquals(EstadoEvaluacion.CERRADA, evaluacion.getEstado());
        verify(evaluacionRepository).save(evaluacion);
        verify(notificacionService).notificarDocentes(any(), anyString());
    }

    @Test
    @DisplayName("Debe retornar todas las evaluaciones")
    void buscarTodas_retornaLista() {
        when(evaluacionRepository.findAll()).thenReturn(Collections.singletonList(new Evaluacion()));
        when(evaluacionMapper.toResponseDTO(any())).thenReturn(mock(EvaluacionResponseDTO.class));

        List<EvaluacionResponseDTO> result = evaluacionService.buscarTodas();

        assertEquals(1, result.size());
    }
}
