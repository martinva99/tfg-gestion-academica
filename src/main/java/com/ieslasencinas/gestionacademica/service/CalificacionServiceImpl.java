package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.CalificacionCreateDTO;
import com.ieslasencinas.gestionacademica.dto.CalificacionResponseDTO;
import com.ieslasencinas.gestionacademica.dto.CalificacionUpdateDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoEvaluacion;
import com.ieslasencinas.gestionacademica.exception.BusinessRuleException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.CalificacionMapper;
import com.ieslasencinas.gestionacademica.repository.CalificacionRepository;
import com.ieslasencinas.gestionacademica.repository.DocenteRepository;
import com.ieslasencinas.gestionacademica.repository.EvaluacionRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalificacionServiceImpl implements CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final MatriculaRepository matriculaRepository;
    private final DocenteRepository docenteRepository;
    private final CalificacionMapper calificacionMapper;
    private final NotificacionService notificacionService;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public CalificacionResponseDTO registrar(CalificacionCreateDTO dto) {
        Evaluacion evaluacion = evaluacionRepository.findById(dto.idEvaluacion()).orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada"));

        if (evaluacion.getEstado() == EstadoEvaluacion.CERRADA) {
            throw new BusinessRuleException("No se pueden registrar calificaciones en una evaluación CERRADA");
        }

        Matricula matricula = matriculaRepository.findById(dto.idMatricula()).orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada"));
        Docente docente = docenteRepository.findById(dto.idDocente()).orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        if (currentUserService.tieneRol("ROLE_DOCENTE")) {
            if (!docente.getUsuario().getEmail().equals(currentUserService.getEmailActual())) {
                throw new BusinessRuleException("No puede calificar alumnos que no le corresponden");
            }
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setEvaluacion(evaluacion);
        calificacion.setMatricula(matricula);
        calificacion.setDocente(docente);
        calificacion.setNota(dto.nota());
        calificacion.setObservaciones(dto.observaciones());

        if (calificacionRepository.findByMatricula_IdMatriculaAndEvaluacion_IdEvaluacion(calificacion.getMatricula().getIdMatricula(), calificacion.getEvaluacion().getIdEvaluacion()).isPresent()) {
            calificacionRepository.findByMatricula_IdMatriculaAndEvaluacion_IdEvaluacion(calificacion.getMatricula().getIdMatricula(), calificacion.getEvaluacion().getIdEvaluacion()).get().setNota(calificacion.getNota());
            calificacionRepository.findByMatricula_IdMatriculaAndEvaluacion_IdEvaluacion(calificacion.getMatricula().getIdMatricula(), calificacion.getEvaluacion().getIdEvaluacion()).get().setObservaciones(calificacion.getObservaciones());
        } else {
            calificacion = calificacionRepository.save(calificacion);
        }

        notificacionService.notificarAlumnoYTutores(matricula.getAlumno(), "Nueva calificación registrada en " + matricula.getAsignatura().getNombre() + ": " + dto.nota());

        return calificacionMapper.toResponseDTO(calificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> buscarPorEvaluacion(Long idEvaluacion) {
        return calificacionRepository.findByEvaluacion_IdEvaluacion(idEvaluacion).stream().map(calificacionMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> buscarPorMatricula(Long idMatricula) {
        return calificacionRepository.findByMatricula_IdMatricula(idMatricula).stream().map(calificacionMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CalificacionResponseDTO actualizar(Long idCalificacion, CalificacionUpdateDTO dto) {
        Calificacion calificacion = calificacionRepository.findById(idCalificacion).orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada"));

        if (currentUserService.tieneRol("ROLE_DOCENTE")) {
            if (!calificacion.getDocente().getUsuario().getEmail().equals(currentUserService.getEmailActual())) {
                throw new BusinessRuleException("No puede modificar calificaciones que no le corresponden");
            }
        }

        if (calificacion.getEvaluacion().getEstado() == EstadoEvaluacion.CERRADA) {
            throw new BusinessRuleException("No se pueden modificar calificaciones de una evaluación CERRADA");
        }

        if (dto.nota() != null) calificacion.setNota(dto.nota());
        if (dto.observaciones() != null) calificacion.setObservaciones(dto.observaciones());

        return calificacionMapper.toResponseDTO(calificacionRepository.save(calificacion));
    }

    @Override
    @Transactional(readOnly = true)
    public double calcularMedia(Long idMatricula) {
        List<Calificacion> calificaciones = calificacionRepository.findByMatricula_IdMatricula(idMatricula);
        if (calificaciones.isEmpty()) return 0.0;
        return calificaciones.stream().mapToInt(Calificacion::getNota).average().orElse(0.0);
    }

    @Override
    @Transactional
    public void registrarCalificacionesLote(List<CalificacionCreateDTO> dtos) {
        for (CalificacionCreateDTO dto : dtos) {
            registrar(dto);
        }
    }
}
