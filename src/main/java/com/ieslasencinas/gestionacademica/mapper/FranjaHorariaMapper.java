package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.FranjaHorariaDTO;
import com.ieslasencinas.gestionacademica.entity.FranjaHoraria;
import org.springframework.stereotype.Component;


@Component
public class FranjaHorariaMapper {

    public FranjaHorariaDTO toDTO(FranjaHoraria entity) {
        if (entity == null) return null;

        return new FranjaHorariaDTO(
                entity.getIdFranja(),
                entity.getDiaSemana(),
                entity.getHoraInicio(),
                entity.getHoraFin()
        );
    }

    public FranjaHoraria toEntity(FranjaHorariaDTO dto) {
        if (dto == null) return null;

        FranjaHoraria entity = new FranjaHoraria();
        entity.setIdFranja(dto.idFranja());
        entity.setDiaSemana(dto.diaSemana());
        entity.setHoraInicio(dto.horaInicio());
        entity.setHoraFin(dto.horaFin());
        return entity;
    }
}
