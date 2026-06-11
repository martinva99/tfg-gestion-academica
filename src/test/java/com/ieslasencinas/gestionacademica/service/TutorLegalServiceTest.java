package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.TutorLegalRegistroDTO;
import com.ieslasencinas.gestionacademica.dto.TutorLegalResponseDTO;
import com.ieslasencinas.gestionacademica.entity.TutorLegal;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.mapper.TutorLegalMapper;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.TutorLegalRepository;
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
@DisplayName("Tests del servicio de Tutor Legal")
class TutorLegalServiceTest {

    @Mock
    private TutorLegalRepository tutorLegalRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TutorLegalMapper tutorLegalMapper;
    @Mock
    private HorarioService horarioService;
    @Mock
    private NotificacionService notificacionService;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private FaltaAsistenciaService faltaAsistenciaService;
    @Mock
    private CurrentUserService currentUserService;
    @InjectMocks
    private TutorLegalServiceImpl tutorLegalService;

    @Test
    @DisplayName("Debe crear un perfil de tutor legal correctamente")
    void crearPerfilTutor_datosValidos_creaPerfil() {
        TutorLegalRegistroDTO dto = new TutorLegalRegistroDTO(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(tutorLegalRepository.existsByUsuario_IdUsuario(1L)).thenReturn(false);
        when(tutorLegalRepository.save(any(TutorLegal.class))).thenReturn(new TutorLegal());
        when(tutorLegalMapper.toResponseDTO(any())).thenReturn(mock(TutorLegalResponseDTO.class));

        TutorLegalResponseDTO result = tutorLegalService.crearPerfilTutor(dto);

        assertNotNull(result);
        verify(tutorLegalRepository).save(any(TutorLegal.class));
    }

    @Test
    @DisplayName("Debe encontrar un tutor legal por su ID")
    void buscarPorId_existente_retornaDTO() {
        when(tutorLegalRepository.findById(1L)).thenReturn(Optional.of(new TutorLegal()));
        when(tutorLegalMapper.toResponseDTO(any())).thenReturn(mock(TutorLegalResponseDTO.class));

        TutorLegalResponseDTO result = tutorLegalService.buscarPorId(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe retornar la lista de todos los tutores legales")
    void buscarTodos_retornaLista() {
        when(tutorLegalRepository.findAll()).thenReturn(Collections.singletonList(new TutorLegal()));
        when(tutorLegalMapper.toResponseDTO(any())).thenReturn(mock(TutorLegalResponseDTO.class));

        List<TutorLegalResponseDTO> result = tutorLegalService.buscarTodos();

        assertEquals(1, result.size());
    }
}
