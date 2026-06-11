package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.GrupoCreateDTO;
import com.ieslasencinas.gestionacademica.dto.GrupoResponseDTO;
import com.ieslasencinas.gestionacademica.entity.AnioAcademico;
import com.ieslasencinas.gestionacademica.entity.Asignatura;
import com.ieslasencinas.gestionacademica.entity.Grupo;
import com.ieslasencinas.gestionacademica.mapper.GrupoMapper;
import com.ieslasencinas.gestionacademica.repository.AnioAcademicoRepository;
import com.ieslasencinas.gestionacademica.repository.AsignaturaRepository;
import com.ieslasencinas.gestionacademica.repository.GrupoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Grupo")
class GrupoServiceTest {

    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private AnioAcademicoRepository anioAcademicoRepository;
    @Mock
    private AsignaturaRepository asignaturaRepository;
    @Mock
    private GrupoMapper grupoMapper;
    @InjectMocks
    private GrupoServiceImpl grupoService;

    @Test
    @DisplayName("Debe crear un grupo correctamente")
    void crear_datosValidos_creaGrupo() {
        GrupoCreateDTO dto = new GrupoCreateDTO("1DAM", 1L, 1L, new HashSet<>());
        when(anioAcademicoRepository.findById(1L)).thenReturn(Optional.of(new AnioAcademico()));
        when(grupoRepository.save(any())).thenReturn(new Grupo());
        when(grupoMapper.toResponseDTO(any())).thenReturn(mock(GrupoResponseDTO.class));

        GrupoResponseDTO result = grupoService.crear(dto);

        assertNotNull(result);
        verify(grupoRepository).save(any());
    }

    @Test
    @DisplayName("Debe asignar una asignatura a un grupo")
    void asignarAsignatura_existente_agregaAsignatura() {
        Grupo grupo = new Grupo();
        grupo.setAsignaturas(new HashSet<>());
        Asignatura asignatura = new Asignatura();

        when(grupoRepository.findById(1L)).thenReturn(Optional.of(grupo));
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asignatura));

        grupoService.asignarAsignatura(1L, 1L);

        assertTrue(grupo.getAsignaturas().contains(asignatura));
        verify(grupoRepository).save(grupo);
    }
}
