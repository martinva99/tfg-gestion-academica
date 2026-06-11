package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.ParteDisciplinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParteDisciplinarioRepository extends JpaRepository<ParteDisciplinario, Long> {
    List<ParteDisciplinario> findByAlumno_IdAlumno(Long idAlumno);

    List<ParteDisciplinario> findByDocente_IdDocente(Long idDocente);
}
