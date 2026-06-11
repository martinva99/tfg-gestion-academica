package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.entity.Alumno;
import com.ieslasencinas.gestionacademica.entity.Matricula;
import com.ieslasencinas.gestionacademica.entity.TutorLegal;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.exception.DuplicateResourceException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.AlumnoMapper;
import com.ieslasencinas.gestionacademica.repository.AlumnoRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.TutorLegalRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorLegalRepository tutorLegalRepository;
    private final AlumnoMapper alumnoMapper;
    private final CurrentUserService currentUserService;

    @Lazy
    @Autowired
    private HorarioService horarioService;

    @Lazy
    @Autowired
    private ExpedienteService expedienteService;

    @Lazy
    @Autowired
    private FaltaAsistenciaService faltaAsistenciaService;

    @Lazy
    @Autowired
    private ParteDisciplinarioService parteDisciplinarioService;

    @Lazy
    @Autowired
    private NotificacionService notificacionService;

    @Lazy
    @Autowired
    private MatriculaRepository matriculaRepository;

    @Override
    @Transactional
    public AlumnoResponseDTO crearPerfilAlumno(AlumnoRegistroDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.idUsuario()));

        if (alumnoRepository.existsByUsuario_IdUsuario(dto.idUsuario())) {
            throw new DuplicateResourceException("El usuario ya tiene un perfil de alumno");
        }

        Alumno alumno = new Alumno();
        alumno.setUsuario(usuario);
        alumno.setFechaNacimiento(dto.fechaNacimiento());
        return alumnoMapper.toResponseDTO(alumnoRepository.save(alumno));
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponseDTO buscarPorId(Long idAlumno) {
        return alumnoRepository.findById(idAlumno)
                .map(alumnoMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado con ID: " + idAlumno));
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponseDTO buscarPorEmail(String email) {
        return alumnoRepository.findByUsuario_Email(email)
                .map(alumnoMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado con email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoExpedienteDTO buscarPorIdExpediente(Long idAlumno) {
        return expedienteService.obtenerExpediente(idAlumno);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> buscarTodos() {
        return alumnoRepository.findAll().stream()
                .map(alumnoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void vincularTutorLegal(VinculacionAlumnoTutorDTO dto) {
        Alumno alumno = alumnoRepository.findById(dto.idAlumno())
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado"));
        TutorLegal tutor = tutorLegalRepository.findById(dto.idTutorLegal())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor no encontrado"));

        alumno.getTutoresLegales().add(tutor);
        tutor.getAlumnos().add(alumno);

        alumnoRepository.save(alumno);
        tutorLegalRepository.save(tutor);
    }

    @Override
    @Transactional
    public void desvincularTutorLegal(VinculacionAlumnoTutorDTO dto) {
        Alumno alumno = alumnoRepository.findById(dto.idAlumno())
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado"));
        TutorLegal tutor = tutorLegalRepository.findById(dto.idTutorLegal())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor no encontrado"));

        alumno.getTutoresLegales().remove(tutor);
        tutor.getAlumnos().remove(alumno);

        alumnoRepository.save(alumno);
        tutorLegalRepository.save(tutor);
    }

    private Alumno getAlumnoActual() {
        Usuario usuario = currentUserService.getUsuarioActual();
        return alumnoRepository.findByUsuario_IdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró perfil de Alumno para el usuario actual"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionHorariaResponseDTO> obtenerMiHorario() {
        return horarioService.obtenerHorarioSemanal(currentUserService.getUsuarioActual().getIdUsuario());
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoExpedienteDTO obtenerMiExpediente() {
        return expedienteService.obtenerExpedienteAutenticado();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaltaAsistenciaResponseDTO> obtenerMiAsistencia() {
        Alumno alumno = getAlumnoActual();
        List<FaltaAsistenciaResponseDTO> todas = new ArrayList<>();
        List<Matricula> matriculas = matriculaRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());
        for (Matricula m : matriculas) {
            todas.addAll(faltaAsistenciaService.buscarPorMatricula(m.getIdMatricula()));
        }
        return todas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParteResponseDTO> obtenerMisPartes() {
        Alumno alumno = getAlumnoActual();
        return parteDisciplinarioService.buscarPorAlumno(alumno.getIdAlumno());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> obtenerMisNotificaciones() {
        return notificacionService.buscarPorUsuario(currentUserService.getUsuarioActual().getIdUsuario());
    }
}
