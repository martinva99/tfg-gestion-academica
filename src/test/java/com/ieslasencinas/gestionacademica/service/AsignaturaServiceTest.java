package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AsignaturaDTO;
import com.ieslasencinas.gestionacademica.entity.Asignatura;
import com.ieslasencinas.gestionacademica.mapper.AsignaturaMapper;
import com.ieslasencinas.gestionacademica.repository.AsignaturaRepository;
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
@DisplayName("Tests del servicio de Asignatura")
class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository asignaturaRepository;
    @Mock
    private AsignaturaMapper asignaturaMapper;
    @InjectMocks
    private AsignaturaServiceImpl asignaturaService;

    @Test
    @DisplayName("Debe crear una asignatura correctamente")
    void crear_datosValidos_creaAsignatura() {
        AsignaturaDTO dto = new AsignaturaDTO(null, "Matematicas", "Calculo y Algebra", 4.0);
        when(asignaturaRepository.save(any())).thenReturn(new Asignatura());

        when(asignaturaMapper.toDTO(any())).thenReturn(dto);

        AsignaturaDTO result = asignaturaService.crear(dto);

        assertNotNull(result);
        verify(asignaturaRepository).save(any());
    }

    @Test
    @DisplayName("Debe encontrar una asignatura por su ID")
    void buscarPorId_existente_retornaDTO() {
        AsignaturaDTO dto = new AsignaturaDTO(1L, "Matematicas", "Calculo y Algebra", 4.0);
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(new Asignatura()));

        when(asignaturaMapper.toDTO(any())).thenReturn(dto);

        AsignaturaDTO result = asignaturaService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.idAsignatura());
    }
}
