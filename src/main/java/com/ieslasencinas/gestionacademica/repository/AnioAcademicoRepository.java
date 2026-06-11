package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.AnioAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnioAcademicoRepository extends JpaRepository<AnioAcademico, Long> {
    Optional<AnioAcademico> findByCursoEscolar(String cursoEscolar);
}
