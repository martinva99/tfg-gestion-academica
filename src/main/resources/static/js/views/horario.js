import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const HorarioView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    let htmlContenido = "";

    try {
      if (rolActivo === "ROLE_ALUMNO") {
        const sesiones = await fetchAPI("/alumnos/horario");
        htmlContenido = generarTablaHorario("Horario", sesiones, rolActivo);
      } else if (rolActivo === "ROLE_DOCENTE") {
        const sesiones = await fetchAPI("/docentes/horario");
        htmlContenido = generarTablaHorario("Horario", sesiones, rolActivo);
      } else if (rolActivo === "ROLE_TUTOR_LEGAL") {
        const hijosHorarios = await fetchAPI("/tutores-legales/horarios");
        if (hijosHorarios && hijosHorarios.length > 0) {
          htmlContenido = hijosHorarios
            .map((hijo) =>
              generarTablaHorario(
                `Horario ${hijo.nombreAlumno}`,
                hijo.horario,
                rolActivo,
              ),
            )
            .join("<br><br>");
        } else {
          htmlContenido =
            '<p class="text-center">No hay alumnos vinculados.</p>';
        }
      } else {
        htmlContenido =
          '<p class="text-center">Tu perfil no tiene horario asignado.</p>';
      }

      return `
                <div style="display: flex; flex-direction: column; align-items: center;">
                    ${htmlContenido}
                </div>
            `;
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar el horario: ${error.message}</div>`;
    }
  },

  init: () => {},
};

function getColorAsignatura(nombre) {
  let hash = 0;
  for (let i = 0; i < nombre.length; i++) {
    hash = nombre.charCodeAt(i) + ((hash << 5) - hash);
  }
  const r = (hash & 0xff0000) >> 16;
  const g = (hash & 0x00ff00) >> 8;
  const b = hash & 0x0000ff;
  return `rgb(${Math.floor((r + 255) / 2)}, ${Math.floor((g + 255) / 2)}, ${Math.floor((b + 255) / 2)})`;
}

function generarTablaHorario(titulo, sesiones, rol) {
  const dias = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"];
  const franjas = [
    { label: "8:30-9:25", start: "08:30" },
    { label: "9:25-10:15", start: "09:25" },
    { label: "10:15-11:10", start: "10:15" },
    { label: "11:35-12:30", start: "11:35" },
    { label: "12:30-13:25", start: "12:30" },
    { label: "13:25-14:20", start: "13:25" },
    { label: "14:30-15:25", start: "14:30" },
  ];

  const matriz = Array(7)
    .fill(null)
    .map(() => Array(5).fill(null));

  if (sesiones) {
    sesiones.forEach((sesion) => {
      const col = dias.indexOf(sesion.diaSemana);
      const fila = franjas.findIndex((f) =>
        sesion.horaInicio.startsWith(f.start),
      );
      if (col !== -1 && fila !== -1) {
        matriz[fila][col] = sesion;
      }
    });
  }

  let filasHtml = "";
  matriz.forEach((fila, indexFila) => {
    const esRecreo1 = indexFila === 2;
    const esRecreo2 = indexFila === 5;
    const estiloFila =
      esRecreo1 || esRecreo2 ? "border-bottom: 3px solid #04A12E;" : "";

    let celdasHtml = `<td style="border: 1px solid #000; width: 158px; height: 90px; font-weight: bold; font-size: 20px; text-align: center; background-color: #CED4DA;">${franjas[indexFila].label}</td>`;

    fila.forEach((sesion) => {
      if (sesion) {
        const colorFondo = getColorAsignatura(sesion.nombreAsignatura);
        let contenido = `<div style="font-weight: bold; font-size: 20px; color: #000;">${sesion.nombreAsignatura}</div>`;

        if (rol === "ROLE_DOCENTE") {
          contenido += `<div style="font-weight: bold; font-size: 20px; color: #000;">${sesion.nombreGrupo}</div>`;
        }

        contenido += `<div style="font-size: 18px; color: #000;">${sesion.aula}</div>`;

        celdasHtml += `<td style="border: 1px solid #000; width: 158px; height: 90px; background-color: ${colorFondo}; text-align: center; vertical-align: middle;">${contenido}</td>`;
      } else {
        celdasHtml += `<td style="border: 1px solid #000; width: 158px; height: 90px; background-color: #FFFFFF;"></td>`;
      }
    });

    filasHtml += `<tr style="${estiloFila}">${celdasHtml}</tr>`;
  });

  return `
        <div style="margin-bottom: 40px; width: 100%; display: flex; flex-direction: column; align-items: center;">
            <h1 style="font-weight: 700; font-size: 24px; color: #000000; margin-bottom: 40px;">${titulo}</h1>
            <table style="border-collapse: collapse; background-color: #FFFFFF; box-shadow: 0px 4px 10px rgba(0,0,0,0.05);">
                <thead>
                    <tr style="background-color: #CED4DA; height: 90px;">
                        <th style="border: 1px solid #000; width: 158px; font-weight: bold; font-size: 24px; text-align: center;">Hora</th>
                        <th style="border: 1px solid #000; width: 158px; font-weight: bold; font-size: 24px; text-align: center;">Lunes</th>
                        <th style="border: 1px solid #000; width: 158px; font-weight: bold; font-size: 24px; text-align: center;">Martes</th>
                        <th style="border: 1px solid #000; width: 158px; font-weight: bold; font-size: 24px; text-align: center;">Miércoles</th>
                        <th style="border: 1px solid #000; width: 158px; font-weight: bold; font-size: 24px; text-align: center;">Jueves</th>
                        <th style="border: 1px solid #000; width: 158px; font-weight: bold; font-size: 24px; text-align: center;">Viernes</th>
                    </tr>
                </thead>
                <tbody>
                    ${filasHtml}
                </tbody>
            </table>
        </div>
    `;
}
