package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AsignaturaDTO;
import com.ieslasencinas.gestionacademica.entity.Asignatura;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.AsignaturaMapper;
import com.ieslasencinas.gestionacademica.repository.AsignaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsignaturaServiceImpl implements AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    private final AsignaturaMapper asignaturaMapper;

    @Override
    @Transactional
    public AsignaturaDTO crear(AsignaturaDTO dto) {
        Asignatura asignatura = asignaturaMapper.toEntity(dto);
        return asignaturaMapper.toDTO(asignaturaRepository.save(asignatura));
    }

    @Override
    @Transactional(readOnly = true)
    public AsignaturaDTO buscarPorId(Long idAsignatura) {
        return asignaturaRepository.findById(idAsignatura)
                .map(asignaturaMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignaturaDTO> buscarTodos() {
        return asignaturaRepository.findAll().stream()
                .map(asignaturaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AsignaturaDTO actualizar(Long idAsignatura, AsignaturaDTO dto) {
        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));

        asignatura.setNombre(dto.nombre());
        asignatura.setHorasSemanales(dto.horasSemanales());

        return asignaturaMapper.toDTO(asignaturaRepository.save(asignatura));
    }

    @Override
    @Transactional
    public void eliminar(Long idAsignatura) {
        if (!asignaturaRepository.existsById(idAsignatura)) {
            throw new ResourceNotFoundException("Asignatura no encontrada");
        }
        asignaturaRepository.deleteById(idAsignatura);
    }
}
