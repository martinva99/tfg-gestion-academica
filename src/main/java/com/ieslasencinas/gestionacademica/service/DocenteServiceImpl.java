package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.exception.DuplicateResourceException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.CalificacionMapper;
import com.ieslasencinas.gestionacademica.mapper.DocenteMapper;
import com.ieslasencinas.gestionacademica.mapper.GrupoAsignaturaMapper;
import com.ieslasencinas.gestionacademica.mapper.MatriculaMapper;
import com.ieslasencinas.gestionacademica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final SesionHorariaRepository sesionHorariaRepository;
    private final CalificacionRepository calificacionRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final ParteDisciplinarioRepository parteDisciplinarioRepository;
    private final GrupoAsignaturaMapper grupoAsignaturaMapper;
    private final DocenteMapper docenteMapper;
    private final MatriculaMapper matriculaMapper;
    private final MatriculaRepository matriculaRepository;
    private final CalificacionService calificacionService;
    private final CalificacionMapper calificacionMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public void asignarPerfilDocente(DocenteRegistroDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + dto.idUsuario()));

        if (docenteRepository.existsByUsuario_IdUsuario(dto.idUsuario())) {
            throw new DuplicateResourceException("El usuario ya tiene un perfil de docente");
        }

        Docente docente = new Docente();
        docente.setUsuario(usuario);
        docente.setEspecialidad(dto.especialidad());
        docenteRepository.save(docente);
    }

    @Override
    @Transactional(readOnly = true)
    public DocenteResponseDTO buscarPorId(Long idDocente) {
        return docenteRepository.findById(idDocente)
                .map(docenteMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + idDocente));
    }

    @Override
    @Transactional(readOnly = true)
    public DocenteResponseDTO buscarPorIdUsuario(Long idUsuario) {
        return docenteRepository.findByUsuario_IdUsuario(idUsuario)
                .map(docenteMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe perfil docente para el usuario: " + idUsuario));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteResponseDTO> buscarTodos() {
        return docenteRepository.findAll().stream()
                .map(docenteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> obtenerAlumnosPorSesion(Long idSesion) {
        SesionHoraria sesion = sesionHorariaRepository.findById(idSesion)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));

        List<Matricula> matriculas = matriculaRepository.findByGrupo_IdGrupoAndAsignatura_IdAsignatura(
                sesion.getGrupo().getIdGrupo(),
                sesion.getAsignatura().getIdAsignatura()
        );

        return matriculas.stream()
                .map(matriculaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoAsignaturaDTO> obtenerGruposYAsignaturas(Long idUsuario) {
        Docente docente = docenteRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        List<SesionHoraria> sesiones = sesionHorariaRepository.findByDocente_IdDocente(docente.getIdDocente());

        return sesiones.stream()
                .map(grupoAsignaturaMapper::toDTO)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> obtenerCalificacionesPorAsignaturaYGrupo(Long idAsignatura, Long idGrupo, Integer trimestre) {


        Evaluacion evaluacion = evaluacionRepository.findByGrupo_IdGrupoAndTrimestre(idGrupo, trimestre)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada para este trimestre"));

        Usuario usuario = currentUserService.getUsuarioActual();
        Docente docente = docenteRepository.findByUsuario_IdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));


        List<Matricula> matriculas = matriculaRepository.findByGrupo_IdGrupoAndAsignatura_IdAsignatura(idGrupo, idAsignatura);


        return matriculas.stream().map(m -> {
            Optional<Calificacion> califOpt = calificacionRepository
                    .findByMatricula_IdMatriculaAndEvaluacion_IdEvaluacion(m.getIdMatricula(), evaluacion.getIdEvaluacion());

            if (califOpt.isPresent()) {
                return calificacionMapper.toResponseDTO(califOpt.get());
            } else {

                return new CalificacionResponseDTO(
                        null,
                        m.getIdMatricula(),
                        evaluacion.getIdEvaluacion(),
                        docente.getIdDocente(),
                        null,
                        "",
                        evaluacion.getTrimestre(),
                        m.getAsignatura().getNombre(),
                        m.getAlumno().getUsuario().getNombre() + " " + m.getAlumno().getUsuario().getApellidos(),
                        docente.getUsuario().getNombre() + " " + docente.getUsuario().getApellidos(),
                        evaluacion.getEstado().name(),
                        m.getAnioAcademico().getCursoEscolar()
                );
            }
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParteResponseDTO> obtenerPartesPorDocente(Long idUsuario) {

        Docente docente = docenteRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));


        List<ParteDisciplinario> partes = parteDisciplinarioRepository.findByDocente_IdDocente(docente.getIdDocente());


        return partes.stream().map(p -> new ParteResponseDTO(
                p.getIdParte(),
                p.getFecha(),
                p.getTipo(),
                p.getDescripcion(),
                p.getEstado(),
                p.getAlumno().getUsuario().getNombre() + " " + p.getAlumno().getUsuario().getApellidos(),
                docente.getUsuario().getNombre() + " " + docente.getUsuario().getApellidos()
        )).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteResponseDTO> obtenerDocentesDisponibles(Long idFranja) {
        return docenteRepository.findDocentesDisponibles(idFranja).stream()
                .map(d -> new DocenteResponseDTO(
                        d.getIdDocente(),
                        d.getUsuario().getNombre(),
                        d.getUsuario().getApellidos(),
                        d.getUsuario().getEmail(),
                        d.getEspecialidad()
                ))
                .collect(Collectors.toList());
    }
}