package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.FranjaHorariaDTO;
import com.ieslasencinas.gestionacademica.entity.FranjaHoraria;
import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;
import com.ieslasencinas.gestionacademica.mapper.FranjaHorariaMapper;
import com.ieslasencinas.gestionacademica.repository.FranjaHorariaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Franja Horaria")
class FranjaHorariaServiceTest {

    @Mock
    private FranjaHorariaRepository franjaHorariaRepository;
    @Mock
    private FranjaHorariaMapper franjaHorariaMapper;
    @InjectMocks
    private FranjaHorariaServiceImpl franjaHorariaService;

    @Test
    @DisplayName("Debe crear una franja horaria correctamente")
    void crear_datosValidos_creaFranja() {
        FranjaHorariaDTO dto = new FranjaHorariaDTO(null, DiaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(9, 0));
        when(franjaHorariaMapper.toEntity(any())).thenReturn(new FranjaHoraria());
        when(franjaHorariaRepository.save(any())).thenReturn(new FranjaHoraria());
        when(franjaHorariaMapper.toDTO(any())).thenReturn(new FranjaHorariaDTO(1L, DiaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(9, 0)));

        FranjaHorariaDTO result = franjaHorariaService.crear(dto);

        assertNotNull(result);
        verify(franjaHorariaRepository).save(any());
    }

    @Test
    @DisplayName("Debe encontrar una franja horaria por su ID")
    void buscarPorId_existente_retornaDTO() {
        when(franjaHorariaRepository.findById(1L)).thenReturn(Optional.of(new FranjaHoraria()));
        when(franjaHorariaMapper.toDTO(any())).thenReturn(mock(FranjaHorariaDTO.class));

        FranjaHorariaDTO result = franjaHorariaService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe retornar todas las franjas horarias")
    void buscarTodos_retornaLista() {
        when(franjaHorariaRepository.findAll()).thenReturn(Collections.singletonList(new FranjaHoraria()));
        when(franjaHorariaMapper.toDTO(any())).thenReturn(mock(FranjaHorariaDTO.class));

        List<FranjaHorariaDTO> result = franjaHorariaService.buscarTodos();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe actualizar una franja horaria correctamente")
    void actualizar_datosValidos_actualizaFranja() {
        FranjaHoraria franja = new FranjaHoraria();
        FranjaHorariaDTO dto = new FranjaHorariaDTO(1L, DiaSemana.MARTES, LocalTime.of(10, 0), LocalTime.of(11, 0));

        when(franjaHorariaRepository.findById(1L)).thenReturn(Optional.of(franja));
        when(franjaHorariaRepository.save(any())).thenReturn(franja);
        when(franjaHorariaMapper.toDTO(any())).thenReturn(dto);

        FranjaHorariaDTO result = franjaHorariaService.actualizar(1L, dto);

        assertEquals(DiaSemana.MARTES, result.diaSemana());
        verify(franjaHorariaRepository).save(franja);
    }

    @Test
    @DisplayName("Debe eliminar una franja horaria correctamente")
    void eliminar_existente_eliminaFranja() {
        when(franjaHorariaRepository.existsById(1L)).thenReturn(true);

        franjaHorariaService.eliminar(1L);

        verify(franjaHorariaRepository).deleteById(1L);
    }
}
