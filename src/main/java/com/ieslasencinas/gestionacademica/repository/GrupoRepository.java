package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByAnioAcademico_IdAnio(Long idAnio);

    Optional<Grupo> findByNombreAndAnioAcademico_IdAnio(String nombre, Long idAnio);

    List<Grupo> findByCentro_IdCentro(Long idCentro);
}
