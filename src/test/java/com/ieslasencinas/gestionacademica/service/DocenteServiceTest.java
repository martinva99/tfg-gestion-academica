package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.DocenteRegistroDTO;
import com.ieslasencinas.gestionacademica.dto.DocenteResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Docente;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.mapper.DocenteMapper;
import com.ieslasencinas.gestionacademica.repository.DocenteRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
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
@DisplayName("Tests del servicio de Docente")
class DocenteServiceTest {

    @Mock
    private DocenteRepository docenteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private DocenteMapper docenteMapper;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private CalificacionService calificacionService;
    @InjectMocks
    private DocenteServiceImpl docenteService;

    @Test
    @DisplayName("Debe asignar un perfil de docente correctamente")
    void asignarPerfilDocente_datosValidos_asignaPerfil() {
        DocenteRegistroDTO dto = new DocenteRegistroDTO(1L, "Informática");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(docenteRepository.existsByUsuario_IdUsuario(1L)).thenReturn(false);

        docenteService.asignarPerfilDocente(dto);

        verify(docenteRepository).save(any(Docente.class));
    }

    @Test
    @DisplayName("Debe encontrar un docente por su ID")
    void buscarPorId_existente_retornaDTO() {
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(new Docente()));
        when(docenteMapper.toResponseDTO(any())).thenReturn(mock(DocenteResponseDTO.class));

        DocenteResponseDTO result = docenteService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe retornar la lista de todos los docentes")
    void buscarTodos_retornaLista() {
        when(docenteRepository.findAll()).thenReturn(Collections.singletonList(new Docente()));
        when(docenteMapper.toResponseDTO(any())).thenReturn(mock(DocenteResponseDTO.class));

        List<DocenteResponseDTO> result = docenteService.buscarTodos();

        assertEquals(1, result.size());
    }
}
