package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.AsignaturaDTO;
import com.ieslasencinas.gestionacademica.entity.Asignatura;
import org.springframework.stereotype.Component;


@Component
public class AsignaturaMapper {

    public AsignaturaDTO toDTO(Asignatura entity) {
        if (entity == null) return null;

        return new AsignaturaDTO(
                entity.getIdAsignatura(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getHorasSemanales()
        );
    }

    public Asignatura toEntity(AsignaturaDTO dto) {
        if (dto == null) return null;

        Asignatura entity = new Asignatura();
        entity.setIdAsignatura(dto.idAsignatura());
        entity.setNombre(dto.nombre());
        entity.setDescripcion(dto.descripcion());
        entity.setHorasSemanales(dto.horasSemanales());
        return entity;
    }
}
