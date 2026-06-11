package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.CalificacionCreateDTO;
import com.ieslasencinas.gestionacademica.dto.CalificacionResponseDTO;
import com.ieslasencinas.gestionacademica.dto.CalificacionUpdateDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoEvaluacion;
import com.ieslasencinas.gestionacademica.exception.BusinessRuleException;
import com.ieslasencinas.gestionacademica.mapper.CalificacionMapper;
import com.ieslasencinas.gestionacademica.repository.CalificacionRepository;
import com.ieslasencinas.gestionacademica.repository.DocenteRepository;
import com.ieslasencinas.gestionacademica.repository.EvaluacionRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
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
@DisplayName("Tests del servicio de Calificación")
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository calificacionRepository;
    @Mock
    private EvaluacionRepository evaluacionRepository;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private DocenteRepository docenteRepository;
    @Mock
    private CalificacionMapper calificacionMapper;
    @Mock
    private NotificacionService notificacionService;
    @Mock
    private CurrentUserService currentUserService;
    @InjectMocks
    private CalificacionServiceImpl calificacionService;

    @Test
    @DisplayName("Debe registrar una calificación cuando la evaluación está ABIERTA")
    void registrar_evaluacionAbierta_creaCalificacion() {
        CalificacionCreateDTO dto = new CalificacionCreateDTO(1L, 1L, 1L, 8, "Buena nota");
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEstado(EstadoEvaluacion.ABIERTA);
        Matricula matricula = new Matricula();
        Alumno alumno = new Alumno();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        alumno.setUsuario(usuario);
        matricula.setAlumno(alumno);
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Matemáticas");
        matricula.setAsignatura(asignatura);
        Docente docente = new Docente();

        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(new Calificacion());
        when(calificacionMapper.toResponseDTO(any(Calificacion.class)))
                .thenReturn(new CalificacionResponseDTO(1L, 1L, 1L, 1L, 8, "Buena nota", 1, "Matemáticas", "Alumno Test", "Docente Test", "ABIERTA", "2023/2024"));

        CalificacionResponseDTO result = calificacionService.registrar(dto);

        assertNotNull(result);
        verify(calificacionRepository).save(any(Calificacion.class));
        verify(notificacionService).notificarAlumnoYTutores(any(), anyString());
    }

    @Test
    @DisplayName("Debe lanzar BusinessRuleException al registrar calificación con evaluación CERRADA (RN3)")
    void registrar_evaluacionCerrada_lanzaExcepcion() {
        CalificacionCreateDTO dto = new CalificacionCreateDTO(1L, 1L, 1L, 8, "Buena nota");
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEstado(EstadoEvaluacion.CERRADA);

        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        assertThrows(BusinessRuleException.class, () -> calificacionService.registrar(dto));
        verify(calificacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar las calificaciones de una evaluación")
    void buscarPorEvaluacion_conCalificaciones_retornaLista() {
        when(calificacionRepository.findByEvaluacion_IdEvaluacion(1L)).thenReturn(Collections.singletonList(new Calificacion()));
        when(calificacionMapper.toResponseDTO(any())).thenReturn(mock(CalificacionResponseDTO.class));

        List<CalificacionResponseDTO> result = calificacionService.buscarPorEvaluacion(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe actualizar una calificación cuando la evaluación está ABIERTA")
    void actualizar_evaluacionAbierta_actualizaCalificacion() {
        CalificacionUpdateDTO dto = new CalificacionUpdateDTO(9, "Mejorado");
        Calificacion calificacion = new Calificacion();
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEstado(EstadoEvaluacion.ABIERTA);
        calificacion.setEvaluacion(evaluacion);

        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));
        when(calificacionRepository.save(any())).thenReturn(calificacion);
        when(calificacionMapper.toResponseDTO(any())).thenReturn(mock(CalificacionResponseDTO.class));

        calificacionService.actualizar(1L, dto);

        verify(calificacionRepository).save(calificacion);
        assertEquals(9, calificacion.getNota());
    }

    @Test
    @DisplayName("Debe lanzar BusinessRuleException al actualizar calificación con evaluación CERRADA (RN3)")
    void actualizar_evaluacionCerrada_lanzaExcepcion() {
        CalificacionUpdateDTO dto = new CalificacionUpdateDTO(9, "Mejorado");
        Calificacion calificacion = new Calificacion();
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEstado(EstadoEvaluacion.CERRADA);
        calificacion.setEvaluacion(evaluacion);

        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(calificacion));

        assertThrows(BusinessRuleException.class, () -> calificacionService.actualizar(1L, dto));
    }

    @Test
    @DisplayName("Debe calcular la media de calificaciones de una matrícula correctamente")
    void calcularMedia_conCalificaciones_retornaMediaCorrecta() {
        Calificacion c1 = new Calificacion();
        c1.setNota(8);
        Calificacion c2 = new Calificacion();
        c2.setNota(6);
        when(calificacionRepository.findByMatricula_IdMatricula(1L)).thenReturn(List.of(c1, c2));

        double media = calificacionService.calcularMedia(1L);

        assertEquals(7.0, media);
    }

    @Test
    @DisplayName("Debe retornar las calificaciones de una matrícula")
    void buscarPorMatricula_conCalificaciones_retornaLista() {
        when(calificacionRepository.findByMatricula_IdMatricula(1L)).thenReturn(Collections.singletonList(new Calificacion()));
        when(calificacionMapper.toResponseDTO(any())).thenReturn(mock(CalificacionResponseDTO.class));

        List<CalificacionResponseDTO> result = calificacionService.buscarPorMatricula(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe registrar un lote de calificaciones")
    void registrarCalificacionesLote_registraTodas() {
        CalificacionCreateDTO dto1 = new CalificacionCreateDTO(1L, 1L, 1L, 8, "Buena nota");
        CalificacionCreateDTO dto2 = new CalificacionCreateDTO(1L, 2L, 1L, 7, "Buena nota");

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEstado(EstadoEvaluacion.ABIERTA);
        Matricula matricula = new Matricula();
        Alumno alumno = new Alumno();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        alumno.setUsuario(usuario);
        matricula.setAlumno(alumno);
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Matemáticas");
        matricula.setAsignatura(asignatura);
        Docente docente = new Docente();

        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(matriculaRepository.findById(anyLong())).thenReturn(Optional.of(matricula));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(new Calificacion());
        when(calificacionMapper.toResponseDTO(any(Calificacion.class))).thenReturn(mock(CalificacionResponseDTO.class));

        calificacionService.registrarCalificacionesLote(List.of(dto1, dto2));

        verify(calificacionRepository, times(2)).save(any(Calificacion.class));
    }
}
