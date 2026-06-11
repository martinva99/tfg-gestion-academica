import { auth } from "../utils/auth.js";
import { fetchAPI } from "../api/apiClient.js";

export const Layout = {
  render: async (contentHtml, currentPath) => {
    const userInfo = auth.getUserInfo();
    const rolActivo = auth.getRolActivo();
    const rolFormateado = rolActivo
      ? rolActivo
          .replace("ROLE_", "")
          .replace(/_/g, " ")
          .toLowerCase()
          .replace(/\b\w/g, (l) => l.toUpperCase())
      : "";

    let noLeidas = 0;
    try {
      const endpoint =
        rolActivo === "ROLE_TUTOR_LEGAL"
          ? "/tutores-legales/resumen"
          : "/dashboard";
      const data = await fetchAPI(endpoint);
      noLeidas = data.notificacionesNoLeidas || 0;
    } catch (e) {
      console.error("Error al cargar notificaciones", e);
    }

    const isActive = (path) => (currentPath === path ? "active" : "");
    const badgeMenuHtml =
      noLeidas > 0 ? `<span class="sidebar-badge">(${noLeidas})</span>` : "";
    const badgeCampanaHtml =
      noLeidas > 0 ? `<div class="notification-badge"></div>` : "";
    const puntoRojoMenuHtml =
      noLeidas > 0
        ? `<span style="color:#EC221F; margin-left:5px;">●</span>`
        : "";

    let menuLinksHtml = "";

    if (rolActivo === "ROLE_ALUMNO" || rolActivo === "ROLE_TUTOR_LEGAL") {
      menuLinksHtml = `
                <a href="#/dashboard" class="sidebar-link ${isActive("/dashboard")}">Inicio</a>
                <a href="#/expediente" class="sidebar-link ${isActive("/expediente")}">Expediente</a>
                <a href="#/faltas" class="sidebar-link ${isActive("/faltas")}">Faltas</a>
                <a href="#/horario" class="sidebar-link ${isActive("/horario")}">Horario</a>
                <a href="#/disciplina" class="sidebar-link ${isActive("/disciplina")}">Disciplina</a>
            `;
    } else if (rolActivo === "ROLE_DOCENTE") {
      menuLinksHtml = `
                <a href="#/dashboard" class="sidebar-link ${isActive("/dashboard")}">Inicio</a>
                <a href="#/asistencia" class="sidebar-link ${isActive("/asistencia")}">Asistencia</a>
                <a href="#/calificaciones" class="sidebar-link ${isActive("/calificaciones")}">Calificaciones</a>
                <a href="#/horario" class="sidebar-link ${isActive("/horario")}">Horario</a>
                <a href="#/disciplina" class="sidebar-link ${isActive("/disciplina")}">Disciplina</a>
            `;
    } else if (rolActivo === "ROLE_SECRETARIA") {
      menuLinksHtml = `
                <a href="#/dashboard" class="sidebar-link ${isActive("/dashboard")}">Inicio</a>
                <a href="#/matriculas" class="sidebar-link ${isActive("/matriculas")}">Matrículas</a>
            `;
    } else if (rolActivo === "ROLE_JEFATURA") {
      menuLinksHtml = `
                <a href="#/dashboard" class="sidebar-link ${isActive("/dashboard")}">Inicio</a>
                <a href="#/evaluaciones" class="sidebar-link ${isActive("/evaluaciones")}">Evaluaciones</a>
                <a href="#/horarios-jefatura" class="sidebar-link ${isActive("/horarios-jefatura")}">Horarios</a>
            `;
    } else if (rolActivo === "ROLE_ADMINISTRADOR") {
      menuLinksHtml = `
                <a href="#/dashboard" class="sidebar-link ${isActive("/dashboard")}">Inicio</a>
                <a href="#/usuarios" class="sidebar-link ${isActive("/usuarios")}">Usuarios</a>
            `;
    }

    menuLinksHtml += `
            <a href="#/notificaciones" class="sidebar-link ${isActive("/notificaciones")}">
                Notificaciones ${puntoRojoMenuHtml} ${badgeMenuHtml}
            </a>
        `;

    return `
            <div class="app-layout">
                <aside class="sidebar">
                    <div style="height: 90px;"></div>
                    
                    ${menuLinksHtml}
                    
                    <div style="flex-grow: 1;"></div>
                    <a href="#" id="btn-logout" class="sidebar-link" style="margin-bottom: 20px;">Cerrar sesión</a>
                </aside>

                <div class="main-wrapper">
                    <header class="topbar">
                        <div class="user-info">
                            <h2 class="user-name">${userInfo.nombre} ${userInfo.apellidos}</h2>
                            <p class="user-role">${rolFormateado}</p>
                        </div>
                        <div class="notification-bell" onclick="window.location.hash='#/notificaciones'">
                            🔔 ${badgeCampanaHtml}
                        </div>
                    </header>

                    <main style="padding: 40px;">
                        ${contentHtml}
                    </main>
                </div>
            </div>
        `;
  },
};
