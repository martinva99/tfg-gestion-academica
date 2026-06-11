// js/views/expediente.js
import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const ExpedienteView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    let htmlContenido = "";

    try {
      if (rolActivo === "ROLE_ALUMNO") {
        const expediente = await fetchAPI("/alumnos/expediente");
        htmlContenido = generarListaCalificaciones(expediente.calificaciones);
      } else if (rolActivo === "ROLE_TUTOR_LEGAL") {
        const hijosExpedientes = await fetchAPI("/tutores-legales/expedientes");

        if (hijosExpedientes && hijosExpedientes.length > 0) {
          htmlContenido = hijosExpedientes
            .map((hijo) => {
              const tituloHijo = `<h2 style="font-weight: 700; font-size: 20px; margin-top: 30px; margin-bottom: 15px; width: 100%; max-width: 950px; text-align: left;">Expediente de ${hijo.nombreAlumno}</h2>`;
              return (
                tituloHijo +
                generarListaCalificaciones(hijo.expediente.calificaciones)
              );
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
                        Expediente Académico
                    </h1>
                    
                    <div style="width: 100%; display: flex; flex-direction: column; align-items: center;">
                        ${htmlContenido}
                    </div>
                </div>
            `;
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar el expediente: ${error.message}</div>`;
    }
  },

  init: () => {},
};

function generarListaCalificaciones(calificaciones) {
  if (!calificaciones || calificaciones.length === 0) {
    return '<p style="font-size: 20px; color: #757575; margin-bottom: 20px;">No hay calificaciones publicadas.</p>';
  }

  calificaciones.sort((a, b) => {
    if (a.anioAcademico !== b.anioAcademico)
      return b.anioAcademico.localeCompare(a.anioAcademico);
    return b.trimestre - a.trimestre;
  });

  return calificaciones
    .map((calif) => {
      const colorNota = calif.nota < 5 ? "#EC221F" : "#04A12E";
      const observaciones = calif.observaciones
        ? ` - ${calif.observaciones}`
        : "";

      return `
            <div style="width: 100%; max-width: 950px; min-height: 90px; background-color: #FFFFFF; border: 1px solid #000000; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center; padding: 15px 20px;">
                
                <div style="display: flex; flex-direction: column; justify-content: center;">
                    <span style="font-weight: 700; font-size: 20px; color: #000000; margin-bottom: 5px;">
                        ${calif.nombreAsignatura}
                    </span>
                    <span style="font-weight: 400; font-size: 16px; color: #757575;">
                        Curso ${calif.anioAcademico} | Trimestre ${calif.trimestre} | Docente: ${calif.nombreDocente} ${observaciones}
                    </span>
                </div>
                
                <div style="display: flex; flex-direction: column; align-items: flex-end;">
                    <span style="font-weight: 400; font-size: 16px; color: #757575; margin-bottom: 5px;">Nota</span>
                    <span style="font-weight: 700; font-size: 28px; color: ${colorNota};">
                        ${calif.nota}
                    </span>
                </div>
                
            </div>
        `;
    })
    .join("");
}
