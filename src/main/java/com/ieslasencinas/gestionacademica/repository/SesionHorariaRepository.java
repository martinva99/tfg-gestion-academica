package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.SesionHoraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SesionHorariaRepository extends JpaRepository<SesionHoraria, Long> {

    boolean existsByDocente_IdDocenteAndFranjaHoraria_IdFranja(Long idDocente, Long idFranja);

    boolean existsByDocente_IdDocenteAndFranjaHoraria_IdFranjaAndIdSesionNot(Long idDocente, Long idFranja, Long idSesion);


    boolean existsByGrupo_IdGrupoAndFranjaHoraria_IdFranja(Long idGrupo, Long idFranja);

    boolean existsByGrupo_IdGrupoAndFranjaHoraria_IdFranjaAndIdSesionNot(Long idGrupo, Long idFranja, Long idSesion);


    boolean existsByAulaAndFranjaHoraria_IdFranja(String aula, Long idFranja);

    boolean existsByAulaAndFranjaHoraria_IdFranjaAndIdSesionNot(String aula, Long idFranja, Long idSesion);

    List<SesionHoraria> findByGrupo_IdGrupo(Long idGrupo);

    List<SesionHoraria> findByDocente_IdDocente(Long idDocente);
}
