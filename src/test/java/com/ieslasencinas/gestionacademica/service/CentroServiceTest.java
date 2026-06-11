package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.CentroDTO;
import com.ieslasencinas.gestionacademica.entity.Centro;
import com.ieslasencinas.gestionacademica.mapper.CentroMapper;
import com.ieslasencinas.gestionacademica.repository.CentroRepository;
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
@DisplayName("Tests del servicio de Centro")
class CentroServiceTest {

    @Mock
    private CentroRepository centroRepository;
    @Mock
    private CentroMapper centroMapper;
    @InjectMocks
    private CentroServiceImpl centroService;

    @Test
    @DisplayName("Debe crear un centro correctamente")
    void crear_datosValidos_creaCentro() {
        CentroDTO dto = new CentroDTO(null, "IES Las Encinas", "Calle Falsa 123", "912345678");
        when(centroRepository.save(any())).thenReturn(new Centro());
        when(centroMapper.toDTO(any())).thenReturn(dto);

        CentroDTO result = centroService.crear(dto);

        assertNotNull(result);
        verify(centroRepository).save(any());
    }

    @Test
    @DisplayName("Debe encontrar un centro por su ID")
    void buscarPorId_existente_retornaDTO() {
        CentroDTO dto = new CentroDTO(1L, "IES Las Encinas", "Calle Falsa 123", "912345678");
        when(centroRepository.findById(1L)).thenReturn(Optional.of(new Centro()));

        when(centroMapper.toDTO(any())).thenReturn(dto);

        CentroDTO result = centroService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.idCentro());
    }
}
