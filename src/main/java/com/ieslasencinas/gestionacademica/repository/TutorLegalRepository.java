package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.TutorLegal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorLegalRepository extends JpaRepository<TutorLegal, Long> {
    Optional<TutorLegal> findByUsuario_IdUsuario(Long idUsuario);

    boolean existsByUsuario_IdUsuario(Long idUsuario);
}
