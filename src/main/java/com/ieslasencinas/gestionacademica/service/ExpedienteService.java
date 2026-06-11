package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.AlumnoExpedienteDTO;


public interface ExpedienteService {
    AlumnoExpedienteDTO obtenerExpediente(Long idAlumno);

    AlumnoExpedienteDTO obtenerExpedienteAutenticado();
}
