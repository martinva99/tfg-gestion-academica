package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.entity.Alumno;
import com.ieslasencinas.gestionacademica.entity.TutorLegal;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.mapper.AlumnoMapper;
import com.ieslasencinas.gestionacademica.repository.AlumnoRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.TutorLegalRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Alumno")
class AlumnoServiceTest {

    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TutorLegalRepository tutorLegalRepository;
    @Mock
    private AlumnoMapper alumnoMapper;
    @Mock
    private CurrentUserService currentUserService;
    @Mock
    private HorarioService horarioService;
    @Mock
    private ExpedienteService expedienteService;
    @Mock
    private FaltaAsistenciaService faltaAsistenciaService;
    @Mock
    private ParteDisciplinarioService parteDisciplinarioService;
    @Mock
    private NotificacionService notificacionService;
    @Mock
    private MatriculaRepository matriculaRepository;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(alumnoService, "horarioService", horarioService);
        ReflectionTestUtils.setField(alumnoService, "expedienteService", expedienteService);
        ReflectionTestUtils.setField(alumnoService, "faltaAsistenciaService", faltaAsistenciaService);
        ReflectionTestUtils.setField(alumnoService, "parteDisciplinarioService", parteDisciplinarioService);
        ReflectionTestUtils.setField(alumnoService, "notificacionService", notificacionService);
        ReflectionTestUtils.setField(alumnoService, "matriculaRepository", matriculaRepository);
    }

    @Test
    @DisplayName("Debe crear un perfil de alumno correctamente")
    void crearPerfilAlumno_datosValidos_creaPerfil() {
        AlumnoRegistroDTO dto = new AlumnoRegistroDTO(1L, LocalDate.of(2005, 5, 15));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(alumnoRepository.existsByUsuario_IdUsuario(1L)).thenReturn(false);
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(new Alumno());
        when(alumnoMapper.toResponseDTO(any())).thenReturn(mock(AlumnoResponseDTO.class));

        AlumnoResponseDTO result = alumnoService.crearPerfilAlumno(dto);

        assertNotNull(result);
        verify(alumnoRepository).save(any(Alumno.class));
    }

    @Test
    @DisplayName("Debe encontrar un alumno por su ID")
    void buscarPorId_existente_retornaDTO() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(new Alumno()));
        when(alumnoMapper.toResponseDTO(any())).thenReturn(mock(AlumnoResponseDTO.class));

        AlumnoResponseDTO result = alumnoService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe retornar la lista de todos los alumnos")
    void buscarTodos_retornaLista() {
        when(alumnoRepository.findAll()).thenReturn(Collections.singletonList(new Alumno()));
        when(alumnoMapper.toResponseDTO(any())).thenReturn(mock(AlumnoResponseDTO.class));

        List<AlumnoResponseDTO> result = alumnoService.buscarTodos();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe vincular un tutor legal a un alumno")
    void vincularTutorLegal_datosValidos_vincula() {
        VinculacionAlumnoTutorDTO dto = new VinculacionAlumnoTutorDTO(1L, 1L);
        Alumno alumno = new Alumno();
        alumno.setTutoresLegales(new HashSet<>());
        TutorLegal tutor = new TutorLegal();
        tutor.setAlumnos(new HashSet<>());

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(tutorLegalRepository.findById(1L)).thenReturn(Optional.of(tutor));

        alumnoService.vincularTutorLegal(dto);

        assertTrue(alumno.getTutoresLegales().contains(tutor));
        assertTrue(tutor.getAlumnos().contains(alumno));
        verify(alumnoRepository).save(alumno);
        verify(tutorLegalRepository).save(tutor);
    }

    @Test
    @DisplayName("Debe obtener el horario del alumno autenticado")
    void obtenerMiHorario_usuarioAutenticado_retornaHorario() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(currentUserService.getUsuarioActual()).thenReturn(usuario);
        when(horarioService.obtenerHorarioSemanal(1L)).thenReturn(Collections.emptyList());

        List<SesionHorariaResponseDTO> result = alumnoService.obtenerMiHorario();

        assertNotNull(result);
        verify(horarioService).obtenerHorarioSemanal(1L);
    }

    @Test
    @DisplayName("Debe obtener el expediente del alumno autenticado")
    void obtenerMiExpediente_usuarioAutenticado_retornaExpediente() {
        when(expedienteService.obtenerExpedienteAutenticado()).thenReturn(mock(AlumnoExpedienteDTO.class));

        AlumnoExpedienteDTO result = alumnoService.obtenerMiExpediente();

        assertNotNull(result);
        verify(expedienteService).obtenerExpedienteAutenticado();
    }

    @Test
    @DisplayName("Debe obtener las notificaciones del alumno autenticado")
    void obtenerMisNotificaciones_usuarioAutenticado_retornaNotificaciones() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(currentUserService.getUsuarioActual()).thenReturn(usuario);
        when(notificacionService.buscarPorUsuario(1L)).thenReturn(Collections.emptyList());

        List<NotificacionResponseDTO> result = alumnoService.obtenerMisNotificaciones();

        assertNotNull(result);
        verify(notificacionService).buscarPorUsuario(1L);
    }
}
