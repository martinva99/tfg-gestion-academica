package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.DashboardDTO;


public interface DashboardService {
    DashboardDTO obtenerDashboard(Long idUsuario);

    DashboardDTO obtenerDashboardAutenticado();
}
