package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AnioAcademicoDTO;
import com.ieslasencinas.gestionacademica.entity.AnioAcademico;
import com.ieslasencinas.gestionacademica.mapper.AnioAcademicoMapper;
import com.ieslasencinas.gestionacademica.repository.AnioAcademicoRepository;
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
@DisplayName("Tests del servicio de Año Académico")
class AnioAcademicoServiceTest {

    @Mock
    private AnioAcademicoRepository anioAcademicoRepository;
    @Mock
    private AnioAcademicoMapper anioAcademicoMapper;
    @InjectMocks
    private AnioAcademicoServiceImpl anioAcademicoService;

    @Test
    @DisplayName("Debe crear un año académico correctamente")
    void crear_datosValidos_creaAnio() {
        AnioAcademicoDTO dto = new AnioAcademicoDTO(null, "2023-2024", LocalDate.now(), LocalDate.now().plusYears(1));
        when(anioAcademicoMapper.toEntity(any())).thenReturn(new AnioAcademico());
        when(anioAcademicoRepository.save(any())).thenReturn(new AnioAcademico());
        when(anioAcademicoMapper.toDTO(any())).thenReturn(new AnioAcademicoDTO(1L, "2023-2024", LocalDate.now(), LocalDate.now().plusYears(1)));

        AnioAcademicoDTO result = anioAcademicoService.crear(dto);

        assertNotNull(result);
        verify(anioAcademicoRepository).save(any());
    }

    @Test
    @DisplayName("Debe encontrar un año académico por su ID")
    void buscarPorId_existente_retornaDTO() {
        when(anioAcademicoRepository.findById(1L)).thenReturn(Optional.of(new AnioAcademico()));
        when(anioAcademicoMapper.toDTO(any())).thenReturn(mock(AnioAcademicoDTO.class));

        AnioAcademicoDTO result = anioAcademicoService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe retornar la lista de todos los años académicos")
    void buscarTodos_retornaLista() {
        when(anioAcademicoRepository.findAll()).thenReturn(Collections.singletonList(new AnioAcademico()));
        when(anioAcademicoMapper.toDTO(any())).thenReturn(mock(AnioAcademicoDTO.class));

        List<AnioAcademicoDTO> result = anioAcademicoService.buscarTodos();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe actualizar un año académico correctamente")
    void actualizar_datosValidos_actualizaAnio() {
        AnioAcademico anio = new AnioAcademico();
        AnioAcademicoDTO dto = new AnioAcademicoDTO(1L, "2024-2025", LocalDate.now(), LocalDate.now().plusYears(1));

        when(anioAcademicoRepository.findById(1L)).thenReturn(Optional.of(anio));
        when(anioAcademicoRepository.save(any())).thenReturn(anio);
        when(anioAcademicoMapper.toDTO(any())).thenReturn(dto);

        AnioAcademicoDTO result = anioAcademicoService.actualizar(1L, dto);

        assertEquals("2024-2025", result.cursoEscolar());
        verify(anioAcademicoRepository).save(anio);
    }
}
