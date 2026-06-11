package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.entity.Matricula;
import com.ieslasencinas.gestionacademica.entity.TutorLegal;
import com.ieslasencinas.gestionacademica.entity.Usuario;
import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;
import com.ieslasencinas.gestionacademica.exception.DuplicateResourceException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.TutorLegalMapper;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.TutorLegalRepository;
import com.ieslasencinas.gestionacademica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorLegalServiceImpl implements TutorLegalService {

    private final TutorLegalRepository tutorLegalRepository;
    private final UsuarioRepository usuarioRepository;
    private final TutorLegalMapper tutorLegalMapper;

    private final HorarioService horarioService;
    private final NotificacionService notificacionService;
    private final MatriculaRepository matriculaRepository;
    private final FaltaAsistenciaService faltaAsistenciaService;
    private final CurrentUserService currentUserService;

    @Autowired
    @Lazy
    private ExpedienteService expedienteService;

    @Autowired
    @Lazy
    private ParteDisciplinarioService parteDisciplinarioService;

    @Override
    @Transactional
    public TutorLegalResponseDTO crearPerfilTutor(TutorLegalRegistroDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.idUsuario()));

        if (tutorLegalRepository.existsByUsuario_IdUsuario(dto.idUsuario())) {
            throw new DuplicateResourceException("El usuario ya tiene un perfil de tutor legal");
        }

        TutorLegal tutor = new TutorLegal();
        tutor.setUsuario(usuario);
        return tutorLegalMapper.toResponseDTO(tutorLegalRepository.save(tutor));
    }

    @Override
    @Transactional(readOnly = true)
    public TutorLegalResponseDTO buscarPorId(Long idTutor) {
        return tutorLegalRepository.findById(idTutor)
                .map(tutorLegalMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor legal no encontrado con ID: " + idTutor));
    }

    @Override
    @Transactional(readOnly = true)
    public TutorLegalResponseDTO buscarPorIdUsuario(Long idUsuario) {
        return tutorLegalRepository.findByUsuario_IdUsuario(idUsuario)
                .map(tutorLegalMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe perfil de tutor legal para el usuario: " + idUsuario));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorLegalResponseDTO> buscarTodos() {
        return tutorLegalRepository.findAll().stream()
                .map(tutorLegalMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TutorDashboardDTO obtenerResumenDashboard() {
        Usuario usuario = currentUserService.getUsuarioActual();

        TutorLegal tutor = tutorLegalRepository.findByUsuario_IdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de tutor no encontrado"));

        List<HijoResumenDTO> hijosResumen = tutor.getAlumnos().stream().map(alumno -> {
           List<SesionHorariaResponseDTO> horarioHoy = horarioService.obtenerHorarioDiario(
                    alumno.getUsuario().getIdUsuario(),
                    DiaSemana.values()[LocalDate.now().getDayOfWeek().getValue() - 1]
            );

            List<Matricula> matriculas = matriculaRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());
            List<AsistenciaResumenDTO> asistencia = matriculas.stream()
                    .map(m -> faltaAsistenciaService.calcularResumenAsistencia(m.getIdMatricula()))
                    .collect(Collectors.toList());

            return new HijoResumenDTO(
                    alumno.getIdAlumno(),
                    alumno.getUsuario().getNombre(),
                    alumno.getUsuario().getApellidos(),
                    horarioHoy,
                    asistencia
            );
        }).collect(Collectors.toList());

        List<NotificacionResponseDTO> notificaciones = notificacionService.buscarPorUsuario(usuario.getIdUsuario());
        long noLeidas = notificacionService.contarNoLeidas(usuario.getIdUsuario());

        return new TutorDashboardDTO(hijosResumen, notificaciones, noLeidas);
    }

    private TutorLegal getTutorActual() {
        Usuario usuario = currentUserService.getUsuarioActual();
        return tutorLegalRepository.findByUsuario_IdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de tutor no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorAgregadosDTO.HijoHorarioDTO> obtenerHorariosAgrupados() {
        return getTutorActual().getAlumnos().stream().map(alumno -> {
            List<SesionHorariaResponseDTO> horario = horarioService.obtenerHorarioSemanal(alumno.getUsuario().getIdUsuario());
            return new TutorAgregadosDTO.HijoHorarioDTO(alumno.getIdAlumno(), alumno.getUsuario().getNombre(), horario);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorAgregadosDTO.HijoExpedienteDTO> obtenerExpedientesAgrupados() {
        return getTutorActual().getAlumnos().stream().map(alumno -> {
            AlumnoExpedienteDTO expediente = expedienteService.obtenerExpediente(alumno.getIdAlumno());
            return new TutorAgregadosDTO.HijoExpedienteDTO(alumno.getIdAlumno(), alumno.getUsuario().getNombre(), expediente);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorAgregadosDTO.HijoAsistenciaDTO> obtenerAsistenciaAgrupada() {
        return getTutorActual().getAlumnos().stream().map(alumno -> {
            List<Matricula> matriculas = matriculaRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());


            List<FaltaAsistenciaResponseDTO> faltas = new ArrayList<>();
            for (Matricula m : matriculas) {
                faltas.addAll(faltaAsistenciaService.buscarPorMatricula(m.getIdMatricula()));
            }


            AsistenciaResumenDTO resumen = null;
            if (!matriculas.isEmpty()) {
                resumen = faltaAsistenciaService.calcularResumenAsistencia(matriculas.get(0).getIdMatricula());
            }

            return new TutorAgregadosDTO.HijoAsistenciaDTO(alumno.getIdAlumno(), alumno.getUsuario().getNombre(), faltas, resumen);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorAgregadosDTO.HijoParteDTO> obtenerPartesAgrupados() {
        return getTutorActual().getAlumnos().stream().map(alumno -> {
            List<ParteResponseDTO> partes = parteDisciplinarioService.buscarPorAlumno(alumno.getIdAlumno());
            return new TutorAgregadosDTO.HijoParteDTO(alumno.getIdAlumno(), alumno.getUsuario().getNombre(), partes);
        }).collect(Collectors.toList());
    }
}
