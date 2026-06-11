package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AlumnoExpedienteDTO;
import com.ieslasencinas.gestionacademica.dto.CalificacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Alumno;
import com.ieslasencinas.gestionacademica.entity.Matricula;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoEvaluacion;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.repository.AlumnoRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpedienteServiceImpl implements ExpedienteService {

    private final AlumnoRepository alumnoRepository;
    private final MatriculaRepository matriculaRepository;
    private final CalificacionService calificacionService;
    private final UsuarioRepository usuarioRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public AlumnoExpedienteDTO obtenerExpediente(Long idAlumno) {
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado"));

        List<Matricula> matriculas = matriculaRepository.findByAlumno_IdAlumno(idAlumno);

        List<CalificacionResponseDTO> calificaciones = new ArrayList<>();

        for (Matricula m : matriculas) {

            List<CalificacionResponseDTO> notasPublicadas = calificacionService.buscarPorMatricula(m.getIdMatricula()).stream()
                    .filter(c -> EstadoEvaluacion.CERRADA.name().equals(c.estadoEvaluacion()))
                    .collect(Collectors.toList());

            calificaciones.addAll(notasPublicadas);
        }


        calificaciones.sort(java.util.Comparator.comparing(CalificacionResponseDTO::anioAcademico)
                .thenComparing(CalificacionResponseDTO::nombreAsignatura));

        return new AlumnoExpedienteDTO(
                idAlumno,
                alumno.getUsuario().getNombre() + " " + alumno.getUsuario().getApellidos(),
                calificaciones
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoExpedienteDTO obtenerExpedienteAutenticado() {
        Usuario usuario = currentUserService.getUsuarioActual();

        Alumno alumno = alumnoRepository.findByUsuario_IdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el perfil de alumno asociado"));

        return obtenerExpediente(alumno.getIdAlumno());
    }
}
