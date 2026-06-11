package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.HorarioSemanalDTO;
import com.ieslasencinas.gestionacademica.dto.SesionHorariaResponseDTO;
import com.ieslasencinas.gestionacademica.entity.Alumno;
import com.ieslasencinas.gestionacademica.entity.Docente;
import com.ieslasencinas.gestionacademica.entity.Grupo;
import com.ieslasencinas.gestionacademica.entity.Matricula;
import com.ieslasencinas.gestionacademica.entity.SesionHoraria;
import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.SesionHorariaMapper;
import com.ieslasencinas.gestionacademica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioServiceImpl implements HorarioService {

    private final SesionHorariaRepository sesionHorariaRepository;
    private final AlumnoRepository alumnoRepository;
    private final DocenteRepository docenteRepository;
    private final GrupoRepository grupoRepository;
    private final MatriculaRepository matriculaRepository;
    private final SesionHorariaMapper sesionHorariaMapper;

    @Override
    @Transactional(readOnly = true)
    public HorarioSemanalDTO obtenerHorarioSemanalGrupo(Long idGrupo) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));

        List<SesionHoraria> sesiones = sesionHorariaRepository.findByGrupo_IdGrupo(idGrupo);
        Map<DiaSemana, List<SesionHorariaResponseDTO>> agrupado = groupSessionsByDay(sesiones);

        return new HorarioSemanalDTO("Horario del grupo " + grupo.getNombre(), agrupado);
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioSemanalDTO obtenerHorarioSemanalDocente(Long idDocente) {
        Docente docente = docenteRepository.findById(idDocente)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        List<SesionHoraria> sesiones = sesionHorariaRepository.findByDocente_IdDocente(idDocente);
        Map<DiaSemana, List<SesionHorariaResponseDTO>> agrupado = groupSessionsByDay(sesiones);

        return new HorarioSemanalDTO("Horario del docente " + docente.getUsuario().getNombre(), agrupado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionHorariaResponseDTO> obtenerHorarioDiario(Long idUsuario, DiaSemana dia) {

        Optional<Alumno> alumno = alumnoRepository.findByUsuario_IdUsuario(idUsuario);
        if (alumno.isPresent()) {
            List<Matricula> matriculas = matriculaRepository.findByAlumno_IdAlumno(alumno.get().getIdAlumno());
            List<SesionHorariaResponseDTO> todas = new ArrayList<>();
            for (Matricula m : matriculas) {
                todas.addAll(sesionHorariaRepository.findByGrupo_IdGrupo(m.getGrupo().getIdGrupo()).stream()
                        .filter(s -> s.getFranjaHoraria().getDiaSemana() == dia)
                        .map(sesionHorariaMapper::toResponseDTO)
                        .collect(Collectors.toList()));
            }
            return todas;
        }


        Optional<Docente> docente = docenteRepository.findByUsuario_IdUsuario(idUsuario);
        if (docente.isPresent()) {
            return sesionHorariaRepository.findByDocente_IdDocente(docente.get().getIdDocente()).stream()
                    .filter(s -> s.getFranjaHoraria().getDiaSemana() == dia)
                    .map(sesionHorariaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionHorariaResponseDTO> obtenerHorarioSemanal(Long idUsuario) {

        Optional<Alumno> alumno = alumnoRepository.findByUsuario_IdUsuario(idUsuario);
        if (alumno.isPresent()) {
            List<Matricula> matriculas = matriculaRepository.findByAlumno_IdAlumno(alumno.get().getIdAlumno());
            List<SesionHorariaResponseDTO> todas = new ArrayList<>();
            for (Matricula m : matriculas) {
                todas.addAll(sesionHorariaRepository.findByGrupo_IdGrupo(m.getGrupo().getIdGrupo()).stream()
                        .map(sesionHorariaMapper::toResponseDTO)
                        .collect(Collectors.toList()));
            }
            return todas;
        }


        Optional<Docente> docente = docenteRepository.findByUsuario_IdUsuario(idUsuario);
        if (docente.isPresent()) {
            return sesionHorariaRepository.findByDocente_IdDocente(docente.get().getIdDocente()).stream()
                    .map(sesionHorariaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private Map<DiaSemana, List<SesionHorariaResponseDTO>> groupSessionsByDay(List<SesionHoraria> sesiones) {
        return sesiones.stream()
                .map(sesionHorariaMapper::toResponseDTO)
                .collect(Collectors.groupingBy(SesionHorariaResponseDTO::diaSemana));
    }
}
