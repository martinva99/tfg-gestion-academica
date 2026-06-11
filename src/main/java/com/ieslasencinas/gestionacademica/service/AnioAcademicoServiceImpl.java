package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AnioAcademicoDTO;
import com.ieslasencinas.gestionacademica.entity.AnioAcademico;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.AnioAcademicoMapper;
import com.ieslasencinas.gestionacademica.repository.AnioAcademicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnioAcademicoServiceImpl implements AnioAcademicoService {

    private final AnioAcademicoRepository anioAcademicoRepository;
    private final AnioAcademicoMapper anioAcademicoMapper;

    @Override
    @Transactional
    public AnioAcademicoDTO crear(AnioAcademicoDTO dto) {
        AnioAcademico anio = anioAcademicoMapper.toEntity(dto);
        return anioAcademicoMapper.toDTO(anioAcademicoRepository.save(anio));
    }

    @Override
    @Transactional(readOnly = true)
    public AnioAcademicoDTO buscarPorId(Long idAnio) {
        return anioAcademicoRepository.findById(idAnio)
                .map(anioAcademicoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Año académico no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnioAcademicoDTO> buscarTodos() {
        return anioAcademicoRepository.findAll().stream()
                .map(anioAcademicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AnioAcademicoDTO actualizar(Long idAnio, AnioAcademicoDTO dto) {
        AnioAcademico anio = anioAcademicoRepository.findById(idAnio)
                .orElseThrow(() -> new ResourceNotFoundException("Año académico no encontrado"));

        anio.setCursoEscolar(dto.cursoEscolar());
        anio.setFechaInicio(dto.fechaInicio());
        anio.setFechaFin(dto.fechaFin());

        return anioAcademicoMapper.toDTO(anioAcademicoRepository.save(anio));
    }
}
