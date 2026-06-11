package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.SesionHorariaCreateDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaUpdateDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.exception.DuplicateResourceException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
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
@DisplayName("Tests del servicio de Sesión Horaria")
class SesionHorariaServiceTest {

    @Mock
    private SesionHorariaRepository sesionHorariaRepository;
    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private DocenteRepository docenteRepository;
    @Mock
    private AsignaturaRepository asignaturaRepository;
    @Mock
    private FranjaHorariaRepository franjaHorariaRepository;
    @Mock
    private SesionHorariaMapper sesionHorariaMapper;
    @InjectMocks
    private SesionHorariaServiceImpl sesionHorariaService;

    @Test
    @DisplayName("Debe crear una sesión horaria cuando no hay conflicto de horario")
    void crear_sinConflicto_creaSesion() {
        SesionHorariaCreateDTO dto = new SesionHorariaCreateDTO(1L, 1L, 1L, 1L, "Aula 101");

        when(sesionHorariaRepository.existsByDocente_IdDocenteAndFranjaHoraria_IdFranja(1L, 1L)).thenReturn(false);
        when(sesionHorariaRepository.existsByGrupo_IdGrupoAndFranjaHoraria_IdFranja(1L, 1L)).thenReturn(false);
        when(sesionHorariaRepository.existsByAulaAndFranjaHoraria_IdFranja("Aula 101", 1L)).thenReturn(false);

        when(grupoRepository.findById(1L)).thenReturn(Optional.of(new Grupo()));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(new Docente()));
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(new Asignatura()));
        when(franjaHorariaRepository.findById(1L)).thenReturn(Optional.of(new FranjaHoraria()));

        when(sesionHorariaRepository.save(any())).thenReturn(new SesionHoraria());
        when(sesionHorariaMapper.toResponseDTO(any())).thenReturn(mock(SesionHorariaResponseDTO.class));

        SesionHorariaResponseDTO result = sesionHorariaService.crear(dto);

        assertNotNull(result);
        verify(sesionHorariaRepository).save(any());
    }

    @Test
    @DisplayName("Debe lanzar DuplicateResourceException cuando el docente ya tiene sesión en esa franja (RN4)")
    void crear_conflictoDocente_lanzaExcepcion() {
        SesionHorariaCreateDTO dto = new SesionHorariaCreateDTO(1L, 1L, 1L, 1L, "Aula 101");
        when(sesionHorariaRepository.existsByDocente_IdDocenteAndFranjaHoraria_IdFranja(1L, 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> sesionHorariaService.crear(dto));
    }

    @Test
    @DisplayName("Debe lanzar DuplicateResourceException cuando el aula está ocupada (RN4)")
    void crear_conflictoAula_lanzaExcepcion() {
        SesionHorariaCreateDTO dto = new SesionHorariaCreateDTO(1L, 1L, 1L, 1L, "Aula 101");
        when(sesionHorariaRepository.existsByDocente_IdDocenteAndFranjaHoraria_IdFranja(1L, 1L)).thenReturn(false);
        when(sesionHorariaRepository.existsByGrupo_IdGrupoAndFranjaHoraria_IdFranja(1L, 1L)).thenReturn(false);
        when(sesionHorariaRepository.existsByAulaAndFranjaHoraria_IdFranja("Aula 101", 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> sesionHorariaService.crear(dto));
    }

    @Test
    @DisplayName("Debe encontrar una sesión horaria por su ID")
    void buscarPorId_existente_retornaDTO() {
        when(sesionHorariaRepository.findById(1L)).thenReturn(Optional.of(new SesionHoraria()));
        when(sesionHorariaMapper.toResponseDTO(any())).thenReturn(mock(SesionHorariaResponseDTO.class));

        SesionHorariaResponseDTO result = sesionHorariaService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe retornar las sesiones horarias de un grupo")
    void buscarPorGrupo_conSesiones_retornaLista() {
        when(sesionHorariaRepository.findByGrupo_IdGrupo(1L)).thenReturn(Collections.singletonList(new SesionHoraria()));
        when(sesionHorariaMapper.toResponseDTO(any())).thenReturn(mock(SesionHorariaResponseDTO.class));

        List<SesionHorariaResponseDTO> result = sesionHorariaService.buscarPorGrupo(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe retornar las sesiones horarias de un docente")
    void buscarPorDocente_conSesiones_retornaLista() {
        when(sesionHorariaRepository.findByDocente_IdDocente(1L)).thenReturn(Collections.singletonList(new SesionHoraria()));
        when(sesionHorariaMapper.toResponseDTO(any())).thenReturn(mock(SesionHorariaResponseDTO.class));

        List<SesionHorariaResponseDTO> result = sesionHorariaService.buscarPorDocente(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe actualizar una sesión horaria correctamente")
    void actualizar_datosValidos_actualiza() {
        Long idSesion = 1L;
        SesionHorariaUpdateDTO dto =
                new SesionHorariaUpdateDTO(1L, 1L, 1L, 1L, "Aula 202");

        SesionHoraria sesion = new SesionHoraria();
        sesion.setIdSesion(idSesion);

        when(sesionHorariaRepository.findById(idSesion)).thenReturn(Optional.of(sesion));
        when(sesionHorariaRepository.existsByDocente_IdDocenteAndFranjaHoraria_IdFranjaAndIdSesionNot(1L, 1L, idSesion)).thenReturn(false);
        when(sesionHorariaRepository.existsByGrupo_IdGrupoAndFranjaHoraria_IdFranjaAndIdSesionNot(1L, 1L, idSesion)).thenReturn(false);
        when(sesionHorariaRepository.existsByAulaAndFranjaHoraria_IdFranjaAndIdSesionNot("Aula 202", 1L, idSesion)).thenReturn(false);

        when(grupoRepository.findById(1L)).thenReturn(Optional.of(new Grupo()));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(new Docente()));
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(new Asignatura()));
        when(franjaHorariaRepository.findById(1L)).thenReturn(Optional.of(new FranjaHoraria()));

        when(sesionHorariaRepository.save(any())).thenReturn(sesion);
        when(sesionHorariaMapper.toResponseDTO(any())).thenReturn(mock(SesionHorariaResponseDTO.class));

        SesionHorariaResponseDTO result = sesionHorariaService.actualizar(idSesion, dto);

        assertNotNull(result);
        verify(sesionHorariaRepository).save(sesion);
    }
}
