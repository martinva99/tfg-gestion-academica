package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.Asignatura;
import com.ieslasencinas.gestionacademica.entity.Grupo;
import com.ieslasencinas.gestionacademica.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    boolean existsByAlumno_IdAlumnoAndAsignatura_IdAsignaturaAndAnioAcademico_IdAnio(
            Long idAlumno, Long idAsignatura, Long idAnio);

    List<Matricula> findByAlumno_IdAlumno(Long idAlumno);

    List<Matricula> findByAlumno_IdAlumnoAndAnioAcademico_IdAnio(Long idAlumno, Long idAnio);

    List<Matricula> findByGrupo_IdGrupoAndAsignatura_IdAsignatura(Long idGrupo, Long idAsignatura);
}
