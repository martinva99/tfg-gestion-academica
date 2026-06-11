package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.SesionHorariaCreateDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaUpdateDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.exception.DuplicateResourceException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.SesionHorariaMapper;
import com.ieslasencinas.gestionacademica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SesionHorariaServiceImpl implements SesionHorariaService {

    private final SesionHorariaRepository sesionHorariaRepository;
    private final GrupoRepository grupoRepository;
    private final DocenteRepository docenteRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final FranjaHorariaRepository franjaHorariaRepository;
    private final SesionHorariaMapper sesionHorariaMapper;

    @Override
    @Transactional
    public SesionHorariaResponseDTO crear(SesionHorariaCreateDTO dto) {

        if (sesionHorariaRepository.existsByDocente_IdDocenteAndFranjaHoraria_IdFranja(dto.idDocente(), dto.idFranja())) {
            throw new DuplicateResourceException("El docente ya tiene una sesión en esta franja horaria");
        }
        if (sesionHorariaRepository.existsByGrupo_IdGrupoAndFranjaHoraria_IdFranja(dto.idGrupo(), dto.idFranja())) {
            throw new DuplicateResourceException("El grupo ya tiene una sesión en esta franja horaria");
        }
        if (dto.aula() != null && !dto.aula().isBlank() &&
                sesionHorariaRepository.existsByAulaAndFranjaHoraria_IdFranja(dto.aula(), dto.idFranja())) {
            throw new DuplicateResourceException("El aula ya está ocupada en esta franja horaria");
        }

        Grupo grupo = grupoRepository.findById(dto.idGrupo())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado con ID: " + dto.idGrupo()));
        Docente docente = docenteRepository.findById(dto.idDocente())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + dto.idDocente()));
        Asignatura asignatura = asignaturaRepository.findById(dto.idAsignatura())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con ID: " + dto.idAsignatura()));
        FranjaHoraria franja = franjaHorariaRepository.findById(dto.idFranja())
                .orElseThrow(() -> new ResourceNotFoundException("Franja horaria no encontrada con ID: " + dto.idFranja()));

        SesionHoraria sesion = new SesionHoraria();
        sesion.setGrupo(grupo);
        sesion.setDocente(docente);
        sesion.setAsignatura(asignatura);
        sesion.setFranjaHoraria(franja);
        sesion.setAula(dto.aula());

        return sesionHorariaMapper.toResponseDTO(sesionHorariaRepository.save(sesion));
    }

    @Override
    @Transactional(readOnly = true)
    public SesionHorariaResponseDTO buscarPorId(Long idSesion) {
        return sesionHorariaRepository.findById(idSesion)
                .map(sesionHorariaMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión horaria no encontrada con ID: " + idSesion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionHorariaResponseDTO> buscarPorGrupo(Long idGrupo) {
        return sesionHorariaRepository.findByGrupo_IdGrupo(idGrupo).stream()
                .map(sesionHorariaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionHorariaResponseDTO> buscarPorDocente(Long idDocente) {
        return sesionHorariaRepository.findByDocente_IdDocente(idDocente).stream()
                .map(sesionHorariaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SesionHorariaResponseDTO actualizar(Long idSesion, SesionHorariaUpdateDTO dto) {
        SesionHoraria sesion = sesionHorariaRepository.findById(idSesion)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión horaria no encontrada con ID: " + idSesion));


        if (sesionHorariaRepository.existsByDocente_IdDocenteAndFranjaHoraria_IdFranjaAndIdSesionNot(dto.idDocente(), dto.idFranja(), idSesion)) {
            throw new DuplicateResourceException("El docente ya tiene otra sesión en esta franja horaria");
        }
        if (sesionHorariaRepository.existsByGrupo_IdGrupoAndFranjaHoraria_IdFranjaAndIdSesionNot(dto.idGrupo(), dto.idFranja(), idSesion)) {
            throw new DuplicateResourceException("El grupo ya tiene otra sesión en esta franja horaria");
        }
        if (dto.aula() != null && !dto.aula().isBlank() &&
                sesionHorariaRepository.existsByAulaAndFranjaHoraria_IdFranjaAndIdSesionNot(dto.aula(), dto.idFranja(), idSesion)) {
            throw new DuplicateResourceException("El aula ya está ocupada por otra sesión en esta franja horaria");
        }

        Grupo grupo = grupoRepository.findById(dto.idGrupo())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
        Docente docente = docenteRepository.findById(dto.idDocente())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
        Asignatura asignatura = asignaturaRepository.findById(dto.idAsignatura())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));
        FranjaHoraria franja = franjaHorariaRepository.findById(dto.idFranja())
                .orElseThrow(() -> new ResourceNotFoundException("Franja horaria no encontrada"));

        sesion.setGrupo(grupo);
        sesion.setDocente(docente);
        sesion.setAsignatura(asignatura);
        sesion.setFranjaHoraria(franja);
        sesion.setAula(dto.aula());

        return sesionHorariaMapper.toResponseDTO(sesionHorariaRepository.save(sesion));
    }

    @Override
    @Transactional
    public void eliminar(Long idSesion) {
        if (!sesionHorariaRepository.existsById(idSesion)) {
            throw new ResourceNotFoundException("Sesión horaria no encontrada con ID: " + idSesion);
        }
        sesionHorariaRepository.deleteById(idSesion);
    }

    @Override
    @Transactional
    public void crearLote(List<SesionHorariaCreateDTO> dtos) {
        for (SesionHorariaCreateDTO dto : dtos) {
            crear(dto);
        }
    }
}
