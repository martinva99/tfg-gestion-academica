package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByUsuario_IdUsuario(Long idUsuario);

    Optional<Alumno> findByUsuario_Email(String email);

    boolean existsByUsuario_IdUsuario(Long idUsuario);
}
