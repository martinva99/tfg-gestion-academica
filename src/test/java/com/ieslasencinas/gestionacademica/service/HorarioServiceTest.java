package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.HorarioSemanalDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;
import com.ieslasencinas.gestionacademica.mapper.SesionHorariaMapper;
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
@DisplayName("Tests del servicio de Horario")
class HorarioServiceTest {

    @Mock
    private SesionHorariaRepository sesionHorariaRepository;
    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private DocenteRepository docenteRepository;
    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private SesionHorariaMapper sesionHorariaMapper;
    @InjectMocks
    private HorarioServiceImpl horarioService;

    @Test
    @DisplayName("Debe retornar el horario semanal de un grupo")
    void obtenerHorarioSemanalGrupo_conDatos_retornaDTO() {
        Grupo grupo = new Grupo();
        grupo.setNombre("1DAM");
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(grupo));
        when(sesionHorariaRepository.findByGrupo_IdGrupo(1L)).thenReturn(Collections.emptyList());

        HorarioSemanalDTO result = horarioService.obtenerHorarioSemanalGrupo(1L);

        assertNotNull(result);
        assertTrue(result.titular().contains("1DAM"));
    }

    @Test
    @DisplayName("Debe retornar el horario del día para un alumno")
    void obtenerHorarioDiario_alumno_retornaLista() {
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(1L);
        when(alumnoRepository.findByUsuario_IdUsuario(1L)).thenReturn(Optional.of(alumno));
        when(matriculaRepository.findByAlumno_IdAlumno(1L)).thenReturn(Collections.emptyList());

        List<SesionHorariaResponseDTO> result = horarioService.obtenerHorarioDiario(1L, DiaSemana.LUNES);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
