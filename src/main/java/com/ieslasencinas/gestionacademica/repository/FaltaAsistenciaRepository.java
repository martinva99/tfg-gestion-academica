package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.FaltaAsistencia;
import com.ieslasencinas.gestionacademica.entity.enums.TipoFalta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FaltaAsistenciaRepository extends JpaRepository<FaltaAsistencia, Long> {
    List<FaltaAsistencia> findByMatricula_IdMatricula(Long idMatricula);

    Optional<FaltaAsistencia> findByMatricula_IdMatriculaAndSesionHoraria_IdSesionAndFecha(
            Long idMatricula, Long idSesion, LocalDate fecha);

    long countByMatricula_IdMatriculaAndJustificadaFalseAndTipo(
            Long idMatricula, TipoFalta tipo);

    List<FaltaAsistencia> findByMatricula_Alumno_IdAlumno(Long idAlumno);

    List<FaltaAsistencia> findBySesionHoraria_IdSesion(Long idSesion);
}
