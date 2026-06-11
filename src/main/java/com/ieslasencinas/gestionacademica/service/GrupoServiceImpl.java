package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.GrupoCreateDTO;
import com.ieslasencinas.gestionacademica.dto.GrupoResponseDTO;
import com.ieslasencinas.gestionacademica.entity.AnioAcademico;
import com.ieslasencinas.gestionacademica.entity.Asignatura;
import com.ieslasencinas.gestionacademica.entity.Grupo;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.GrupoMapper;
import com.ieslasencinas.gestionacademica.repository.AnioAcademicoRepository;
import com.ieslasencinas.gestionacademica.repository.AsignaturaRepository;
import com.ieslasencinas.gestionacademica.repository.GrupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrupoServiceImpl implements GrupoService {

    private final GrupoRepository grupoRepository;
    private final AnioAcademicoRepository anioAcademicoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final GrupoMapper grupoMapper;

    @Override
    @Transactional
    public GrupoResponseDTO crear(GrupoCreateDTO dto) {
        AnioAcademico anio = anioAcademicoRepository.findById(dto.idAnio())
                .orElseThrow(() -> new ResourceNotFoundException("Año académico no encontrado"));

        Grupo grupo = new Grupo();
        grupo.setNombre(dto.nombre());
        grupo.setAnioAcademico(anio);

        return grupoMapper.toResponseDTO(grupoRepository.save(grupo));
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponseDTO buscarPorId(Long idGrupo) {
        return grupoRepository.findById(idGrupo)
                .map(grupoMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> buscarTodos() {
        return grupoRepository.findAll().stream()
                .map(grupoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> buscarPorAnioAcademico(Long idAnio) {
        return grupoRepository.findByAnioAcademico_IdAnio(idAnio).stream()
                .map(grupoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GrupoResponseDTO actualizar(Long idGrupo, GrupoCreateDTO dto) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));

        AnioAcademico anio = anioAcademicoRepository.findById(dto.idAnio())
                .orElseThrow(() -> new ResourceNotFoundException("Año académico no encontrado"));

        grupo.setNombre(dto.nombre());
        grupo.setAnioAcademico(anio);

        return grupoMapper.toResponseDTO(grupoRepository.save(grupo));
    }

    @Override
    @Transactional
    public void asignarAsignatura(Long idGrupo, Long idAsignatura) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));

        grupo.getAsignaturas().add(asignatura);
        grupoRepository.save(grupo);
    }

    @Override
    @Transactional
    public void desasignarAsignatura(Long idGrupo, Long idAsignatura) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));

        grupo.getAsignaturas().remove(asignatura);
        grupoRepository.save(grupo);
    }
}
