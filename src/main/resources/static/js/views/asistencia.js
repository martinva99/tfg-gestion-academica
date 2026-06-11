import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const AsistenciaView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    if (rolActivo !== "ROLE_DOCENTE")
      return `<div class="alert alert-danger text-center mt-5">Acceso denegado.</div>`;

    return `
            <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
                <h1 style="font-weight: 700; font-size: 24px; color: #000000; margin-bottom: 30px;">Control de Asistencia</h1>
                
                <div style="width: 950px; background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; padding: 20px; margin-bottom: 30px;">
                    <label style="font-weight: 700; font-size: 18px; margin-bottom: 8px; display: block;">Selecciona la sesión (Semana actual):</label>
                    <select id="select-sesion" style="width: 100%; height: 48px; border-radius: 8px; border: 1px solid #757575; font-size: 18px; padding: 0 10px;">
                        <option value="">Cargando tu horario...</option>
                    </select>
                </div>

                <div id="contenedor-tabla" style="width: 950px; display: none; flex-direction: column;">
                    <table style="width: 100%; border-collapse: collapse; border: 1px solid #000000;">
                        <thead>
                            <tr style="background-color: #CED4DA; height: 40px; border-bottom: 1px solid #000000;">
                                <th style="width: 700px; border-right: 1px solid #000000; padding-left: 15px; text-align: left; font-weight: 700; font-size: 24px;">Alumno/a</th>
                                <th style="width: 125px; border-right: 1px solid #000000; text-align: center; font-weight: 700; font-size: 24px;">Falta</th>
                                <th style="width: 125px; text-align: center; font-weight: 700; font-size: 24px;">Retraso</th>
                            </tr>
                        </thead>
                        <tbody id="tabla-asistencia"></tbody>
                    </table>

                    <div style="display: flex; justify-content: flex-end; margin-top: 20px;">
                        <button id="btn-guardar-asistencia" style="width: 200px; height: 50px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 24px; border: none; border-radius: 20px; cursor: pointer; display: flex; justify-content: center; align-items: center;">
                            GUARDAR <span style="margin-left: 10px;">💾</span>
                        </button>
                    </div>
                </div>
            </div>
        `;
  },

  init: async () => {
    const selectSesion = document.getElementById("select-sesion");
    const contenedorTabla = document.getElementById("contenedor-tabla");
    const tabla = document.getElementById("tabla-asistencia");
    const btnGuardar = document.getElementById("btn-guardar-asistencia");

    if (!selectSesion) return;

    const diasSemana = [
      "LUNES",
      "MARTES",
      "MIERCOLES",
      "JUEVES",
      "VIERNES",
      "SABADO",
      "DOMINGO",
    ];
    const jsDay = new Date().getDay();
    const hoyIndex = jsDay === 0 ? 6 : jsDay - 1;

    let sesionesValidas = [];

    try {
      const horarioCompleto = await fetchAPI("/docentes/horario");

      sesionesValidas = horarioCompleto.filter((sesion) => {
        const indexDia = diasSemana.indexOf(sesion.diaSemana);
        return indexDia <= hoyIndex;
      });

      sesionesValidas.sort((a, b) => {
        const indexA = diasSemana.indexOf(a.diaSemana);
        const indexB = diasSemana.indexOf(b.diaSemana);
        if (indexA !== indexB) return indexA - indexB;
        return a.horaInicio.localeCompare(b.horaInicio);
      });

      if (sesionesValidas.length > 0) {
        selectSesion.innerHTML =
          '<option value="">-- Selecciona una sesión --</option>' +
          sesionesValidas
            .map((s) => {
              const hora = s.horaInicio.substring(0, 5);
              return `<option value="${s.idSesion}">${s.diaSemana} (${hora}) - ${s.nombreAsignatura} ${s.nombreGrupo}</option>`;
            })
            .join("");
      } else {
        selectSesion.innerHTML =
          '<option value="">No has tenido clases aún esta semana</option>';
        selectSesion.disabled = true;
      }
    } catch (error) {
      selectSesion.innerHTML =
        '<option value="">Error al cargar el horario</option>';
    }

    selectSesion.addEventListener("change", async () => {
      const idSesion = selectSesion.value;
      if (!idSesion) {
        contenedorTabla.style.display = "none";
        return;
      }

      const sesionSeleccionada = sesionesValidas.find(
        (s) => s.idSesion == idSesion,
      );
      const indexDiaSesion = diasSemana.indexOf(sesionSeleccionada.diaSemana);
      const diffDias = hoyIndex - indexDiaSesion;

      const fechaSesion = new Date();
      fechaSesion.setDate(fechaSesion.getDate() - diffDias);
      const fechaSesionStr = fechaSesion.toISOString().split("T")[0];

      btnGuardar.setAttribute("data-fecha", fechaSesionStr);
      btnGuardar.setAttribute("data-sesion", idSesion);

      try {
        const matriculas = await fetchAPI(
          `/docentes/sesiones/${idSesion}/alumnos`,
        );
        const faltasRegistradas = await fetchAPI(
          `/docentes/asistencia/sesion/${idSesion}`,
        );

        if (!matriculas || matriculas.length === 0) {
          alert("No hay alumnos matriculados en este grupo.");
          contenedorTabla.style.display = "none";
          return;
        }

        tabla.innerHTML = matriculas
          .map((matricula, index) => {
            const registroPrevio = faltasRegistradas.find(
              (f) =>
                f.nombreAlumno === matricula.nombreAlumno &&
                f.fecha === fechaSesionStr,
            );
            const isFalta =
              registroPrevio && registroPrevio.tipo === "FALTA"
                ? "checked"
                : "";
            const isRetraso =
              registroPrevio && registroPrevio.tipo === "RETRASO"
                ? "checked"
                : "";

            const bgColor = index % 2 === 0 ? "#F8F9FA" : "#E9ECEF";

            return `
                        <tr style="background-color: ${bgColor}; height: 40px;" data-idmatricula="${matricula.idMatricula}">
                            <td style="border-right: 1px solid #000000; padding-left: 15px; font-size: 24px;">${matricula.nombreAlumno}</td>
                            <td style="border-right: 1px solid #000000; text-align: center;">
                                <input type="checkbox" class="check-falta" style="width: 20px; height: 20px; cursor: pointer;" ${isFalta}>
                            </td>
                            <td style="text-align: center;">
                                <input type="checkbox" class="check-retraso" style="width: 20px; height: 20px; cursor: pointer;" ${isRetraso}>
                            </td>
                        </tr>
                    `;
          })
          .join("");

        contenedorTabla.style.display = "flex";
      } catch (error) {
        alert("Error al cargar los datos de la sesión: " + error.message);
      }
    });

    tabla.addEventListener("change", (e) => {
      if (e.target.tagName === "INPUT" && e.target.type === "checkbox") {
        const fila = e.target.closest("tr");
        const checkFalta = fila.querySelector(".check-falta");
        const checkRetraso = fila.querySelector(".check-retraso");

        if (e.target.classList.contains("check-falta") && e.target.checked)
          checkRetraso.checked = false;
        else if (
          e.target.classList.contains("check-retraso") &&
          e.target.checked
        )
          checkFalta.checked = false;
      }
    });

    btnGuardar.addEventListener("click", async () => {
      const idSesion = btnGuardar.getAttribute("data-sesion");
      const fechaCalculada = btnGuardar.getAttribute("data-fecha");
      const filas = tabla.querySelectorAll("tr");
      const registros = [];

      filas.forEach((fila) => {
        const idMatricula = fila.getAttribute("data-idmatricula");
        const isFalta = fila.querySelector(".check-falta").checked;
        const isRetraso = fila.querySelector(".check-retraso").checked;

        if (isFalta || isRetraso) {
          registros.push({
            idMatricula: parseInt(idMatricula),
            idSesion: parseInt(idSesion),
            tipo: isFalta ? "FALTA" : "RETRASO",
            fecha: fechaCalculada,
          });
        }
      });

      try {
        btnGuardar.disabled = true;
        btnGuardar.innerHTML = "GUARDANDO...";

        await fetchAPI("/faltas/lote", {
          method: "POST",
          body: JSON.stringify(registros),
        });

        alert("Asistencia guardada correctamente.");
      } catch (error) {
        alert("Error al guardar: " + error.message);
      } finally {
        btnGuardar.disabled = false;
        btnGuardar.innerHTML =
          'GUARDAR <span style="margin-left: 10px;">💾</span>';
      }
    });
  },
};
