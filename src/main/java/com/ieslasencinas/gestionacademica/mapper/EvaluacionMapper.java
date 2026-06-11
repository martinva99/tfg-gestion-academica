package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.EvaluacionResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Evaluacion;
import org.springframework.stereotype.Component;


@Component
public class EvaluacionMapper {

    public EvaluacionResponseDTO toResponseDTO(Evaluacion evaluacion) {
        if (evaluacion == null) return null;

        return new EvaluacionResponseDTO(
                evaluacion.getIdEvaluacion(),
                evaluacion.getTrimestre(),
                evaluacion.getEstado(),
                evaluacion.getGrupo().getNombre()
        );
    }
}
