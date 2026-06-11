package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.CentroDTO;
import com.ieslasencinas.gestionacademica.entity.Centro;
import org.springframework.stereotype.Component;


@Component
public class CentroMapper {

    public CentroDTO toDTO(Centro entity) {
        if (entity == null) return null;

        return new CentroDTO(
                entity.getIdCentro(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getTelefono()
        );
    }

    public Centro toEntity(CentroDTO dto) {
        if (dto == null) return null;

        Centro entity = new Centro();
        entity.setIdCentro(dto.idCentro());
        entity.setNombre(dto.nombre());
        entity.setDireccion(dto.direccion());
        entity.setTelefono(dto.telefono());
        return entity;
    }
}
