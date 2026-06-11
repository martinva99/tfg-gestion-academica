package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.*;
import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.exception.BusinessRuleException;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.FaltaAsistenciaMapper;
import com.ieslasencinas.gestionacademica.repository.FaltaAsistenciaRepository;
import com.ieslasencinas.gestionacademica.repository.MatriculaRepository;
import com.ieslasencinas.gestionacademica.repository.SesionHorariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ieslasencinas.gestionacademica.entity.enums.TipoFalta.FALTA;


@Service
@RequiredArgsConstructor
public class FaltaAsistenciaServiceImpl implements FaltaAsistenciaService {

    private final FaltaAsistenciaRepository faltaAsistenciaRepository;
    private final MatriculaRepository matriculaRepository;
    private final SesionHorariaRepository sesionHorariaRepository;
    private final FaltaAsistenciaMapper faltaAsistenciaMapper;
    private final NotificacionService notificacionService;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public FaltaAsistenciaResponseDTO registrarFalta(FaltaAsistenciaCreateDTO dto) {

        if (dto.fecha().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("No se pueden registrar faltas en fechas futuras");
        }


        FaltaAsistencia faltaExistente = faltaAsistenciaRepository.findByMatricula_IdMatriculaAndSesionHoraria_IdSesionAndFecha(
                dto.idMatricula(), dto.idSesion(), dto.fecha()).orElse(null);

        Matricula matricula = matriculaRepository.findById(dto.idMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada"));
        SesionHoraria sesion = sesionHorariaRepository.findById(dto.idSesion())
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));

        if (currentUserService.tieneRol("ROLE_DOCENTE")) {
            if (!sesion.getDocente().getUsuario().getEmail().equals(currentUserService.getEmailActual())) {
                throw new BusinessRuleException("No puede registrar faltas en sesiones que no imparte");
            }
        }

        FaltaAsistencia falta;
        if (faltaExistente != null) {
            falta = faltaExistente;
            falta.setTipo(dto.tipo());
        } else {
            falta = new FaltaAsistencia();
            falta.setMatricula(matricula);
            falta.setSesionHoraria(sesion);
            falta.setFecha(dto.fecha());
            falta.setTipo(dto.tipo());
            falta.setJustificada(false);
        }

        FaltaAsistencia guardada = faltaAsistenciaRepository.save(falta);


        String tipoFalta = dto.tipo() == FALTA ? "falta de asistencia" : "retraso";
        String msg = "Se ha registrado un " + tipoFalta + " en la sesión de " + matricula.getAsignatura().getNombre() + " del " + dto.fecha();
        notificacionService.notificarAlumnoYTutores(matricula.getAlumno(), msg);


        verificarUmbralesYNotificar(matricula);

        return faltaAsistenciaMapper.toResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaltaAsistenciaResponseDTO> buscarPorMatricula(Long idMatricula) {
        return faltaAsistenciaRepository.findByMatricula_IdMatricula(idMatricula).stream()
                .map(faltaAsistenciaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FaltaAsistenciaResponseDTO justificarFalta(Long idFalta, FaltaAsistenciaJustificarDTO dto) {
        FaltaAsistencia falta = faltaAsistenciaRepository.findById(idFalta)
                .orElseThrow(() -> new ResourceNotFoundException("Falta no encontrada"));

        if (currentUserService.tieneRol("ROLE_DOCENTE")) {
            if (!falta.getSesionHoraria().getDocente().getUsuario().getEmail().equals(currentUserService.getEmailActual())) {
                throw new BusinessRuleException("No puede justificar faltas de sesiones que no imparte");
            }
        }

        falta.setJustificada(true);
        falta.setObservaciones(dto.observaciones());

        return faltaAsistenciaMapper.toResponseDTO(faltaAsistenciaRepository.save(falta));
    }

    @Override
    @Transactional(readOnly = true)
    public AsistenciaResumenDTO calcularResumenAsistencia(Long idMatricula) {
        Matricula matricula = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada"));

        double totalHoras = calcularTotalHorasAsignatura(matricula);
        long faltasInjustificadas = faltaAsistenciaRepository.countByMatricula_IdMatriculaAndJustificadaFalseAndTipo(
                idMatricula, FALTA);

        double porcentaje = (faltasInjustificadas / totalHoras) * 100;
        List<Integer> umbralesSuperados = new ArrayList<>();
        if (porcentaje >= 15) umbralesSuperados.add(15);
        if (porcentaje >= 10) umbralesSuperados.add(10);
        if (porcentaje >= 5) umbralesSuperados.add(5);

        return new AsistenciaResumenDTO(
                matricula.getAsignatura().getNombre(),
                totalHoras,
                (double) faltasInjustificadas,
                porcentaje,
                umbralesSuperados
        );
    }

    private double calcularTotalHorasAsignatura(Matricula matricula) {
        AnioAcademico anio = matricula.getAnioAcademico();
        long semanas = ChronoUnit.WEEKS.between(anio.getFechaInicio(), anio.getFechaFin());
        return semanas * matricula.getAsignatura().getHorasSemanales();
    }

    private void verificarUmbralesYNotificar(Matricula matricula) {
        AsistenciaResumenDTO resumen = calcularResumenAsistencia(matricula.getIdMatricula());
        double porcentaje = resumen.porcentajeAusencias();
        Long idUsuarioAlumno = matricula.getAlumno().getUsuario().getIdUsuario();

        if (porcentaje >= 15) {
            String msg = "URGENTE: El alumno " + matricula.getAlumno().getUsuario().getNombre() +
                    " ha superado el 15% de faltas en " + matricula.getAsignatura().getNombre();
            notificacionService.notificarAlumnoYTutores(matricula.getAlumno(), msg);

            notificacionService.notificarUsuariosPorRol("ROLE_JEFATURA", msg);
        } else if (porcentaje >= 10) {
            notificacionService.notificarAlumnoYTutores(matricula.getAlumno(), "Alerta: El alumno ha alcanzado el 10% de faltas en " + matricula.getAsignatura().getNombre());
        } else if (porcentaje >= 5) {
            notificacionService.notificarAlumnoYTutores(matricula.getAlumno(), "Aviso: El alumno ha alcanzado el 5% de faltas en " + matricula.getAsignatura().getNombre());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaltaAsistenciaResponseDTO> buscarPorSesion(Long idSesion) {
        return faltaAsistenciaRepository.findBySesionHoraria_IdSesion(idSesion).stream()
                .map(faltaAsistenciaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void registrarFaltasLote(List<FaltaAsistenciaCreateDTO> dtos) {
        for (FaltaAsistenciaCreateDTO dto : dtos) {
            registrarFalta(dto);
        }
    }
}
