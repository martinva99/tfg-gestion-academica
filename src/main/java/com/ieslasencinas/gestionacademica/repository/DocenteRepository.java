package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    boolean existsByUsuario_IdUsuario(Long idUsuario);

    java.util.Optional<Docente> findByUsuario_IdUsuario(Long idUsuario);

    @Query("SELECT d FROM Docente d WHERE d.idDocente NOT IN " +
            "(SELECT s.docente.idDocente FROM SesionHoraria s WHERE s.franjaHoraria.idFranja = :idFranja)")
    List<Docente> findDocentesDisponibles(@Param("idFranja") Long idFranja);
}