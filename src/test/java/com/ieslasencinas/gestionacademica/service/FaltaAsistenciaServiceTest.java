package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.FaltaAsistenciaCreateDTO;
import com.ieslasencinas.gestionacademica.dto.FaltaAsistenciaJustificarDTO;
import com.ieslasencinas.gestionacademica.dto.FaltaAsistenciaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.TipoFalta;
import com.ieslasencinas.gestionacademica.mapper.FaltaAsistenciaMapper;
import com.ieslasencinas.gestionacademica.repository.FaltaAsistenciaRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.SesionHorariaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Falta de Asistencia")
class FaltaAsistenciaServiceTest {

    @Mock
    private FaltaAsistenciaRepository faltaAsistenciaRepository;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private SesionHorariaRepository sesionHorariaRepository;
    @Mock
    private FaltaAsistenciaMapper faltaAsistenciaMapper;
    @Mock
    private NotificacionService notificacionService;
    @Mock
    private CurrentUserService currentUserService;
    @InjectMocks
    private FaltaAsistenciaServiceImpl faltaAsistenciaService;

    @Test
    @DisplayName("Debe registrar una falta de asistencia correctamente")
    void registrarFalta_datosValidos_creaFalta() {
        FaltaAsistenciaCreateDTO dto = new FaltaAsistenciaCreateDTO(1L, 1L, LocalDate.now(), TipoFalta.FALTA);

        AnioAcademico anio = new AnioAcademico();
        anio.setFechaInicio(LocalDate.now().minusWeeks(10));
        anio.setFechaFin(LocalDate.now());

        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Matemáticas");
        asignatura.setHorasSemanales(4.0);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Alumno Test");

        Alumno alumno = new Alumno();
        alumno.setUsuario(usuario);
        alumno.setTutoresLegales(Collections.emptySet());

        Matricula matricula = new Matricula();
        matricula.setIdMatricula(1L);
        matricula.setAlumno(alumno);
        matricula.setAsignatura(asignatura);
        matricula.setAnioAcademico(anio);

        when(faltaAsistenciaRepository.findByMatricula_IdMatriculaAndSesionHoraria_IdSesionAndFecha(any(), any(), any())).thenReturn(Optional.empty());
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(sesionHorariaRepository.findById(1L)).thenReturn(Optional.of(new SesionHoraria()));


        when(faltaAsistenciaRepository.countByMatricula_IdMatriculaAndJustificadaFalseAndTipo(eq(1L), eq(TipoFalta.FALTA))).thenReturn(2L);

        when(faltaAsistenciaRepository.save(any())).thenReturn(new FaltaAsistencia());
        when(faltaAsistenciaMapper.toResponseDTO(any())).thenReturn(mock(FaltaAsistenciaResponseDTO.class));

        FaltaAsistenciaResponseDTO result = faltaAsistenciaService.registrarFalta(dto);

        assertNotNull(result);
        verify(faltaAsistenciaRepository).save(any());
        verify(notificacionService, atLeastOnce()).notificarAlumnoYTutores(any(), anyString());
    }

    @Test
    @DisplayName("Debe actualizar el tipo de asistencia si ya existe un registro previo")
    void registrarFalta_asistenciaExistente_actualizaTipo() {
        FaltaAsistenciaCreateDTO dto = new FaltaAsistenciaCreateDTO(1L, 1L, LocalDate.now(), TipoFalta.RETRASO);
        FaltaAsistencia faltaExistente = new FaltaAsistencia();
        faltaExistente.setTipo(TipoFalta.FALTA);

        Matricula matricula = new Matricula();
        matricula.setIdMatricula(1L);
        Alumno alumno = new Alumno();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        alumno.setUsuario(usuario);
        matricula.setAlumno(alumno);
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre("Matemáticas");
        asignatura.setHorasSemanales(4.0);
        matricula.setAsignatura(asignatura);
        AnioAcademico anio = new AnioAcademico();
        anio.setFechaInicio(LocalDate.now().minusWeeks(10));
        anio.setFechaFin(LocalDate.now());
        matricula.setAnioAcademico(anio);

        SesionHoraria sesion = new SesionHoraria();
        Docente docente = new Docente();
        docente.setUsuario(usuario);
        sesion.setDocente(docente);

        when(faltaAsistenciaRepository.findByMatricula_IdMatriculaAndSesionHoraria_IdSesionAndFecha(
                anyLong(), anyLong(), any(LocalDate.class))).thenReturn(Optional.of(faltaExistente));
        when(matriculaRepository.findById(anyLong())).thenReturn(Optional.of(matricula));
        when(sesionHorariaRepository.findById(anyLong())).thenReturn(Optional.of(sesion));
        when(faltaAsistenciaRepository.save(any(FaltaAsistencia.class))).thenReturn(faltaExistente);
        when(faltaAsistenciaMapper.toResponseDTO(any())).thenReturn(mock(FaltaAsistenciaResponseDTO.class));

        faltaAsistenciaService.registrarFalta(dto);

        assertEquals(TipoFalta.RETRASO, faltaExistente.getTipo());
        verify(faltaAsistenciaRepository).save(faltaExistente);
    }

    @Test
    @DisplayName("Debe justificar una falta correctamente")
    void justificarFalta_existente_cambiaEstado() {
        FaltaAsistencia falta = new FaltaAsistencia();
        falta.setJustificada(false);
        FaltaAsistenciaJustificarDTO dto = new FaltaAsistenciaJustificarDTO(true, "Médico");

        when(faltaAsistenciaRepository.findById(1L)).thenReturn(Optional.of(falta));
        when(faltaAsistenciaRepository.save(any())).thenReturn(falta);
        when(faltaAsistenciaMapper.toResponseDTO(any())).thenReturn(mock(FaltaAsistenciaResponseDTO.class));

        faltaAsistenciaService.justificarFalta(1L, dto);

        assertTrue(falta.isJustificada());
        assertEquals("Médico", falta.getObservaciones());
    }
}
