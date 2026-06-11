package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.EvaluacionEstadoDTO;
import com.ieslasencinas.gestionacademica.dto.EvaluacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoEvaluacion;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.EvaluacionMapper;
import com.ieslasencinas.gestionacademica.repository.EvaluacionRepository;
import com.ieslasencinas.gestionacademica.repository.GrupoRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.SesionHorariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final GrupoRepository grupoRepository;
    private final MatriculaRepository matriculaRepository;
    private final EvaluacionMapper evaluacionMapper;
    private final NotificacionService notificacionService;

    @Override
    @Transactional(readOnly = true)
    public EvaluacionResponseDTO buscarPorId(Long idEvaluacion) {
        return evaluacionRepository.findById(idEvaluacion)
                .map(evaluacionMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> buscarPorGrupo(Long idGrupo) {
        return evaluacionRepository.findByGrupo_IdGrupo(idGrupo).stream()
                .map(evaluacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> buscarTodas() {
        return evaluacionRepository.findAll().stream()
                .map(evaluacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private final SesionHorariaRepository sesionHorariaRepository;

    @Override
    @Transactional
    public EvaluacionResponseDTO cambiarEstado(Long idEvaluacion, EvaluacionEstadoDTO dto) {
        Evaluacion evaluacion = evaluacionRepository.findById(idEvaluacion)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada"));

        EstadoEvaluacion anterior = evaluacion.getEstado();
        evaluacion.setEstado(dto.nuevoEstado());
        Evaluacion guardada = evaluacionRepository.save(evaluacion);

        if (anterior != dto.nuevoEstado()) {
            String accion = (dto.nuevoEstado() == EstadoEvaluacion.ABIERTA) ? "abierta" : "cerrada";
            String mensaje = "La evaluación del trimestre " + evaluacion.getTrimestre() + " ha sido " + accion + " para el grupo " + evaluacion.getGrupo().getNombre();


            List<Docente> docentesAfectados = sesionHorariaRepository.findByGrupo_IdGrupo(evaluacion.getGrupo().getIdGrupo()).stream()
                    .map(SesionHoraria::getDocente)
                    .distinct()
                    .collect(Collectors.toList());

            notificacionService.notificarDocentes(docentesAfectados, mensaje);
        }

        return evaluacionMapper.toResponseDTO(guardada);
    }
}
