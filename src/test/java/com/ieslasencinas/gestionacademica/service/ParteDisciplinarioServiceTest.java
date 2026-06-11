package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.ParteCreateDTO;
import com.ieslasencinas.gestionacademica.dto.ParteResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoParte;
import com.ieslasencinas.gestionacademica.entity.enums.TipoParte;
import com.ieslasencinas.gestionacademica.mapper.ParteDisciplinarioMapper;
import com.ieslasencinas.gestionacademica.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Parte Disciplinario")
class ParteDisciplinarioServiceTest {

    @Mock
    private ParteDisciplinarioRepository parteDisciplinarioRepository;
    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private DocenteRepository docenteRepository;
    @Mock
    private ParteDisciplinarioMapper parteDisciplinarioMapper;
    @Mock
    private NotificacionService notificacionService;
    @Mock
    private CurrentUserService currentUserService;
    @Mock
    private SesionHorariaRepository sesionHorariaRepository;
    @Mock
    private MatriculaRepository matriculaRepository;
    @InjectMocks
    private ParteDisciplinarioServiceImpl parteDisciplinarioService;

    @Test
    @DisplayName("Debe registrar un parte disciplinario correctamente")
    void crear_datosValidos_creaParte() {
        ParteCreateDTO dto = new ParteCreateDTO(1L, LocalDate.now(), TipoParte.LEVE, "Falta de respeto");
        Alumno alumno = new Alumno();
        alumno.setUsuario(new Usuario());
        alumno.setTutoresLegales(Collections.emptySet());
        Docente docente = new Docente();

        Matricula matricula = new Matricula();
        matricula.setAlumno(alumno);

        Usuario usuarioActual = new Usuario();
        usuarioActual.setIdUsuario(1L);

        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(currentUserService.getUsuarioActual()).thenReturn(usuarioActual);
        when(docenteRepository.findByUsuario_IdUsuario(1L)).thenReturn(Optional.of(docente));
        when(parteDisciplinarioRepository.save(any())).thenReturn(new ParteDisciplinario());
        when(parteDisciplinarioMapper.toResponseDTO(any())).thenReturn(mock(ParteResponseDTO.class));

        ParteResponseDTO result = parteDisciplinarioService.crear(dto);

        assertNotNull(result);
        verify(parteDisciplinarioRepository).save(any());
    }

    @Test
    @DisplayName("Debe retornar los partes de un alumno")
    void buscarPorAlumno_conPartes_retornaLista() {
        when(parteDisciplinarioRepository.findByAlumno_IdAlumno(1L)).thenReturn(Collections.singletonList(new ParteDisciplinario()));
        when(parteDisciplinarioMapper.toResponseDTO(any())).thenReturn(mock(ParteResponseDTO.class));

        List<ParteResponseDTO> result = parteDisciplinarioService.buscarPorAlumno(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe actualizar el estado de un parte")
    void actualizarEstado_existente_cambiaEstado() {
        ParteDisciplinario parte = new ParteDisciplinario();
        when(parteDisciplinarioRepository.findById(1L)).thenReturn(Optional.of(parte));
        when(parteDisciplinarioRepository.save(any())).thenReturn(parte);
        when(parteDisciplinarioMapper.toResponseDTO(any())).thenReturn(mock(ParteResponseDTO.class));

        ParteResponseDTO result = parteDisciplinarioService.actualizarEstado(1L, EstadoParte.FIRMADO);

        assertNotNull(result);
        verify(parteDisciplinarioRepository).save(parte);
    }
}
