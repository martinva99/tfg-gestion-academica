package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.CentroDTO;
import com.ieslasencinas.gestionacademica.entity.Centro;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.CentroMapper;
import com.ieslasencinas.gestionacademica.repository.CentroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CentroServiceImpl implements CentroService {

    private final CentroRepository centroRepository;
    private final CentroMapper centroMapper;

    @Override
    @Transactional
    public CentroDTO crear(CentroDTO dto) {
        Centro centro = centroMapper.toEntity(dto);
        return centroMapper.toDTO(centroRepository.save(centro));
    }

    @Override
    @Transactional(readOnly = true)
    public CentroDTO buscarPorId(Long idCentro) {
        return centroRepository.findById(idCentro)
                .map(centroMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Centro no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroDTO> buscarTodos() {
        return centroRepository.findAll().stream()
                .map(centroMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CentroDTO actualizar(Long idCentro, CentroDTO dto) {
        Centro centro = centroRepository.findById(idCentro)
                .orElseThrow(() -> new ResourceNotFoundException("Centro no encontrado"));

        centro.setNombre(dto.nombre());
        centro.setDireccion(dto.direccion());

        return centroMapper.toDTO(centroRepository.save(centro));
    }

    @Override
    @Transactional
    public void eliminar(Long idCentro) {
        if (!centroRepository.existsById(idCentro)) {
            throw new ResourceNotFoundException("Centro no encontrado");
        }
        centroRepository.deleteById(idCentro);
    }
}
