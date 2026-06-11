package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.AnioAcademicoDTO;
import com.ieslasencinas.gestionacademica.entity.AnioAcademico;
import org.springframework.stereotype.Component;


@Component
public class AnioAcademicoMapper {

    public AnioAcademicoDTO toDTO(AnioAcademico entity) {
        if (entity == null) return null;

        return new AnioAcademicoDTO(
                entity.getIdAnio(),
                entity.getCursoEscolar(),
                entity.getFechaInicio(),
                entity.getFechaFin()
        );
    }

    public AnioAcademico toEntity(AnioAcademicoDTO dto) {
        if (dto == null) return null;

        AnioAcademico entity = new AnioAcademico();
        entity.setIdAnio(dto.idAnio());
        entity.setCursoEscolar(dto.cursoEscolar());
        entity.setFechaInicio(dto.fechaInicio());
        entity.setFechaFin(dto.fechaFin());
        return entity;
    }
}
