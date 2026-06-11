import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const DashboardView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    const userInfo = auth.getUserInfo();

    const opcionesFecha = { weekday: "long", day: "numeric", month: "long" };
    const fechaHoy = new Intl.DateTimeFormat("es-ES", opcionesFecha).format(
      new Date(),
    );
    const diaSemana = new Intl.DateTimeFormat("es-ES", { weekday: "long" })
      .format(new Date())
      .toUpperCase();

    let dashboardData = null;
    let htmlHorarios = "";

    try {
      if (rolActivo === "ROLE_TUTOR_LEGAL") {
        dashboardData = await fetchAPI("/tutores-legales/resumen");
        if (dashboardData.hijos && dashboardData.hijos.length > 0) {
          htmlHorarios = dashboardData.hijos
            .map((hijo) =>
              generarCajaHorario(
                `Horario ${diaSemana} ${hijo.nombre}:`,
                hijo.horarioHoy,
              ),
            )
            .join("");
        }
      } else {
        dashboardData = await fetchAPI("/dashboard");
        if (rolActivo === "ROLE_ALUMNO" || rolActivo === "ROLE_DOCENTE") {
          htmlHorarios = generarCajaHorario(
            `Horario ${diaSemana}:`,
            dashboardData.horarioDiario,
          );
        }
      }

      const noLeidas = dashboardData.notificacionesNoLeidas || 0;
      const textoNotif =
        noLeidas > 0
          ? `Tienes <span style="font-weight:bold;">${noLeidas}</span> notificaciones sin leer`
          : `No hay notificaciones sin leer`;

      return `
                <div>
                    <h1 style="font-weight: 700; font-size: 24px; color: #000000; text-align: center; margin-bottom: 40px;">
                        Hola, ${userInfo.nombre}. Resumen de hoy, ${fechaHoy}:
                    </h1>

                    <!-- CAJA DE NOTIFICACIONES -->
                    <div style="background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; max-width: 950px; height: 90px; margin: 0 auto 40px auto; display: flex; align-items: center; padding: 0 20px; justify-content: space-between;">
                        <div style="display: flex; align-items: center;">
                            <span style="font-size: 24px; margin-right: 15px;">🔔</span>
                            <span style="font-size: 20px; color: #000000;">${textoNotif}</span>
                        </div>
                        <button onclick="window.location.hash='#/notificaciones'" style="width: 250px; height: 50px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 20px; border: none; border-radius: 20px; cursor: pointer;">
                            Ir a notificaciones
                        </button>
                    </div>

                    <!-- CAJAS DE HORARIOS (Dinámicas) -->
                    ${htmlHorarios}
                </div>
            `;
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar el dashboard: ${error.message}</div>`;
    }
  },

  init: () => {},
};

function generarCajaHorario(titulo, sesiones) {
  let listaSesionesHtml =
    '<p style="font-size: 20px; color: #757575; margin-left: 20px;">No hay clases programadas para hoy.</p>';

  if (sesiones && sesiones.length > 0) {
    const sesionesUnicas = [];
    const idsVistos = new Set();

    sesiones.forEach((sesion) => {
      if (!idsVistos.has(sesion.idSesion)) {
        idsVistos.add(sesion.idSesion);
        sesionesUnicas.push(sesion);
      }
    });

    sesionesUnicas.sort((a, b) => a.horaInicio.localeCompare(b.horaInicio));

    listaSesionesHtml = sesionesUnicas
      .map((sesion, index) => {
        const hora = sesion.horaInicio.substring(0, 5);
        return `
                <p style="font-size: 20px; color: #000000; margin-left: 20px; margin-bottom: 10px;">
                    <span style="font-weight: 700;">${index + 1}ª</span> (${hora}) - ${sesion.nombreAsignatura}
                </p>
            `;
      })
      .join("");
  }

  return `
        <div style="background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; max-width: 950px; min-height: 200px; margin: 0 auto 40px auto; padding: 20px; position: relative; padding-bottom: 80px;">
            <h2 style="font-weight: 700; font-size: 20px; color: #000000; margin-bottom: 20px;">${titulo}</h2>
            
            ${listaSesionesHtml}

            <button onclick="window.location.hash='#/horario'" style="position: absolute; bottom: 20px; right: 20px; width: 250px; height: 50px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 20px; border: none; border-radius: 20px; cursor: pointer;">
                Ir a horario
            </button>
        </div>
    `;
}
