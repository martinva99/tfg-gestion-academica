package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AlumnoExpedienteDTO;
import com.ieslasencinas.gestionacademica.entity.Alumno;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.repository.AlumnoRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de Expediente")
class ExpedienteServiceTest {

    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private CalificacionService calificacionService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CurrentUserService currentUserService;
    @InjectMocks
    private ExpedienteServiceImpl expedienteService;

    @Test
    @DisplayName("Debe generar el expediente completo de un alumno")
    void obtenerExpediente_conDatos_retornaDTO() {
        Alumno alumno = new Alumno();
        Usuario usuario = new Usuario();
        usuario.setNombre("Test");
        usuario.setApellidos("User");
        alumno.setUsuario(usuario);

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(matriculaRepository.findByAlumno_IdAlumno(1L)).thenReturn(Collections.emptyList());


        AlumnoExpedienteDTO result = expedienteService.obtenerExpediente(1L);

        assertNotNull(result);

        assertTrue(result.nombreCompleto().contains("Test"));
    }
}
