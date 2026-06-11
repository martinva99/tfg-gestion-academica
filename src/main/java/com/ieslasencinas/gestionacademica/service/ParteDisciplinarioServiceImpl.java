package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.ParteCreateDTO;
import com.ieslasencinas.gestionacademica.dto.ParteResponseDTO;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.EstadoParte;
import com.ieslasencinas.gestionacademica.exception.BusinessRuleException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.ParteDisciplinarioMapper;
import com.ieslasencinas.gestionacademica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParteDisciplinarioServiceImpl implements ParteDisciplinarioService {

    private final ParteDisciplinarioRepository parteDisciplinarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final DocenteRepository docenteRepository;
    private final ParteDisciplinarioMapper parteDisciplinarioMapper;
    private final NotificacionService notificacionService;

    private final CurrentUserService currentUserService;
    private final SesionHorariaRepository sesionHorariaRepository;
    private final MatriculaRepository matriculaRepository;

    @Override
    @Transactional
    public ParteResponseDTO crear(ParteCreateDTO dto) {

        Matricula matricula = matriculaRepository.findById(dto.idMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada"));
        Alumno alumno = matricula.getAlumno();


        Usuario usuarioActual = currentUserService.getUsuarioActual();
        Docente docente = docenteRepository.findByUsuario_IdUsuario(usuarioActual.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));


        if (currentUserService.tieneRol("ROLE_DOCENTE")) {
            List<SesionHoraria> sesionesDocente = sesionHorariaRepository.findByDocente_IdDocente(docente.getIdDocente());
            List<Matricula> matriculasAlumno = matriculaRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());

            boolean esSuAlumno = sesionesDocente.stream().anyMatch(sesion ->
                    matriculasAlumno.stream().anyMatch(m ->
                            m.getAsignatura().getIdAsignatura().equals(sesion.getAsignatura().getIdAsignatura()) &&
                                    m.getGrupo().getIdGrupo().equals(sesion.getGrupo().getIdGrupo())
                    )
            );

            if (!esSuAlumno) {
                throw new BusinessRuleException("No puede gestionar partes de alumnos a los que no imparte clase");
            }
        }


        ParteDisciplinario parte = new ParteDisciplinario();
        parte.setAlumno(alumno);
        parte.setDocente(docente);
        parte.setFecha(dto.fecha());
        parte.setTipo(dto.tipo());
        parte.setDescripcion(dto.descripcion());


        ParteDisciplinario guardado = parteDisciplinarioRepository.save(parte);


        String msg = "Se ha registrado un parte disciplinario de tipo " + dto.tipo() + " para el alumno " + alumno.getUsuario().getNombre();
        notificacionService.notificarAlumnoYTutores(alumno, msg);

        return parteDisciplinarioMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ParteResponseDTO buscarPorId(Long idParte) {
        return parteDisciplinarioRepository.findById(idParte)
                .map(parteDisciplinarioMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Parte no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParteResponseDTO> buscarPorAlumno(Long idAlumno) {
        return parteDisciplinarioRepository.findByAlumno_IdAlumno(idAlumno).stream()
                .map(parteDisciplinarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParteResponseDTO actualizarEstado(Long idParte, EstadoParte nuevoEstado) {
        ParteDisciplinario parte = parteDisciplinarioRepository.findById(idParte)
                .orElseThrow(() -> new ResourceNotFoundException("Parte no encontrado"));


        return parteDisciplinarioMapper.toResponseDTO(parteDisciplinarioRepository.save(parte));
    }
}
