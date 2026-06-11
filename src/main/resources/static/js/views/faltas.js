import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const FaltasView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    let htmlContenido = "";

    try {
      if (rolActivo === "ROLE_ALUMNO") {
        const faltas = await fetchAPI("/alumnos/asistencia");
        htmlContenido = generarListaFaltas(faltas);
      } else if (rolActivo === "ROLE_TUTOR_LEGAL") {
        const hijosAsistencia = await fetchAPI("/tutores-legales/asistencia");

        if (hijosAsistencia && hijosAsistencia.length > 0) {
          htmlContenido = hijosAsistencia
            .map((hijo) => {
              const tituloHijo = `<h2 style="font-weight: 700; font-size: 20px; margin-top: 30px; margin-bottom: 15px; width: 100%; max-width: 950px; text-align: left;">Faltas de ${hijo.nombreAlumno}</h2>`;
              return tituloHijo + generarListaFaltas(hijo.faltas);
            })
            .join("");
        } else {
          htmlContenido =
            '<p style="font-size: 20px;">No hay alumnos vinculados.</p>';
        }
      } else {
        htmlContenido =
          '<p style="font-size: 20px;">No tienes acceso a esta sección.</p>';
      }

      return `
                <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
                    <h1 style="font-weight: 700; font-size: 24px; color: #000000; margin-bottom: 40px;">
                        Mis faltas de asistencia
                    </h1>
                    
                    <div style="width: 100%; display: flex; flex-direction: column; align-items: center;">
                        ${htmlContenido}
                    </div>
                </div>
            `;
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar las faltas: ${error.message}</div>`;
    }
  },

  init: () => {},
};

function generarListaFaltas(faltas) {
  if (!faltas || faltas.length === 0) {
    return '<p style="font-size: 20px; color: #757575; margin-bottom: 20px;">No hay faltas de asistencia registradas.</p>';
  }

  return faltas
    .map((falta) => {
      const fechaObj = new Date(falta.fecha);
      const fechaStr = fechaObj.toLocaleDateString("es-ES");

      const justificadaStr = falta.justificada ? "SÍ" : "NO";
      const tipoStr =
        falta.tipo.charAt(0).toUpperCase() + falta.tipo.slice(1).toLowerCase();

      return `
            <div style="width: 100%; max-width: 950px; height: 90px; background-color: #FFFFFF; border: 1px solid #000000; margin-bottom: 10px; display: flex; align-items: center; padding: 0 20px;">
                <span style="font-weight: 400; font-size: 20px; color: #000000;">
                    ${tipoStr}: ${falta.nombreAlumno}, ${falta.nombreGrupo}, ${falta.nombreAsignatura}, ${fechaStr}. JUSTIFICADA: ${justificadaStr}
                </span>
            </div>
        `;
    })
    .join("");
}
