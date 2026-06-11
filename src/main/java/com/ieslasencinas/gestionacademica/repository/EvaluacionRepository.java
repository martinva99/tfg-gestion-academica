package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByGrupo_IdGrupo(Long idGrupo);

    Optional<Evaluacion> findByGrupo_IdGrupoAndTrimestre(Long idGrupo, Integer trimestre);

    Optional<Evaluacion> findByTrimestre(Integer trimestre);
}
