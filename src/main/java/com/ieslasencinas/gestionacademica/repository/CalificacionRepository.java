package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByEvaluacion_IdEvaluacion(Long idEvaluacion);

    List<Calificacion> findByMatricula_IdMatricula(Long idMatricula);

    Optional<Calificacion> findByMatricula_IdMatriculaAndEvaluacion_IdEvaluacion(
            Long idMatricula, Long idEvaluacion);
}
