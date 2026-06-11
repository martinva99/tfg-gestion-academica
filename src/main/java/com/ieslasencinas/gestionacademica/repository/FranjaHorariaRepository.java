package com.ieslasencinas.gestionacademica.repository;

import com.ieslasencinas.gestionacademica.entity.FranjaHoraria;
import com.ieslasencinas.gestionacademica.entity.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FranjaHorariaRepository extends JpaRepository<FranjaHoraria, Long> {
    List<FranjaHoraria> findByDiaSemana(DiaSemana diaSemana);
}
