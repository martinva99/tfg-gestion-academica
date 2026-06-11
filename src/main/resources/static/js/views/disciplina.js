import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const DisciplinaView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    let htmlContenido = "";
    let htmlFormulario = "";

    try {
      if (rolActivo === "ROLE_DOCENTE") {
        htmlFormulario = `
                    <div style="width: 950px; background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; padding: 20px; margin-bottom: 40px; display: flex; flex-direction: column; gap: 20px;">
                        <h2 style="font-weight: 700; font-size: 20px; margin: 0;">Registrar nuevo parte disciplinario</h2>
                        
                        <div style="display: flex; gap: 20px;">
                            <div style="flex: 1;">
                                <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Asignatura y Grupo:</label>
                                <select id="select-grupo" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; font-size: 16px; padding: 0 10px;">
                                    <option value="">Cargando grupos...</option>
                                </select>
                            </div>
                            <div style="flex: 1;">
                                <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Alumno/a:</label>
                                <select id="select-alumno" disabled style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; font-size: 16px; padding: 0 10px; background-color: #E9ECEF;">
                                    <option value="">Seleccione primero un grupo</option>
                                </select>
                            </div>
                            <div style="flex: 1;">
                                <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Tipo de Parte:</label>
                                <select id="select-tipo" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; font-size: 16px; padding: 0 10px;">
                                    <option value="LEVE">LEVE</option>
                                    <option value="GRAVE">GRAVE</option>
                                </select>
                            </div>
                        </div>

                        <div>
                            <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Descripción de los hechos:</label>
                            <textarea id="input-descripcion" rows="3" style="width: 100%; border-radius: 8px; border: 1px solid #757575; font-size: 16px; padding: 10px; resize: none;" placeholder="Describa lo ocurrido..."></textarea>
                        </div>

                        <div style="display: flex; justify-content: flex-end;">
                            <button id="btn-guardar-parte" style="width: 250px; height: 45px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 8px; cursor: pointer;">
                                GUARDAR PARTE 💾
                            </button>
                        </div>
                    </div>
                `;

        const misPartes = await fetchAPI("/docentes/partes");
        htmlContenido =
          `<h2 style="font-weight: 700; font-size: 20px; width: 950px; text-align: left; margin-bottom: 15px;">Últimos partes registrados por ti</h2>` +
          generarListaPartes(misPartes);
      } else if (rolActivo === "ROLE_ALUMNO") {
        const partes = await fetchAPI("/alumnos/partes");
        htmlContenido = generarListaPartes(partes);
      } else if (rolActivo === "ROLE_TUTOR_LEGAL") {
        const hijosPartes = await fetchAPI("/tutores-legales/partes");
        if (hijosPartes && hijosPartes.length > 0) {
          htmlContenido = hijosPartes
            .map((hijo) => {
              return (
                `<h2 style="font-weight: 700; font-size: 20px; margin-top: 30px; margin-bottom: 15px; width: 950px; text-align: left;">Partes de ${hijo.nombreAlumno}</h2>` +
                generarListaPartes(hijo.partes)
              );
            })
            .join("");
        } else {
          htmlContenido =
            '<p style="font-size: 20px;">No hay alumnos vinculados.</p>';
        }
      }

      return `
                <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
                    <h1 style="font-weight: 700; font-size: 24px; color: #000000; margin-bottom: 40px;">Disciplina</h1>
                    ${htmlFormulario}
                    <div style="width: 100%; display: flex; flex-direction: column; align-items: center;">
                        ${htmlContenido}
                    </div>
                </div>
            `;
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar la sección de disciplina: ${error.message}</div>`;
    }
  },

  init: async () => {
    const rolActivo = auth.getRolActivo();
    if (rolActivo !== "ROLE_DOCENTE") return;

    const selectGrupo = document.getElementById("select-grupo");
    const selectAlumno = document.getElementById("select-alumno");
    const selectTipo = document.getElementById("select-tipo");
    const inputDesc = document.getElementById("input-descripcion");
    const btnGuardar = document.getElementById("btn-guardar-parte");

    try {
      const horario = await fetchAPI("/docentes/horario");
      if (horario && horario.length > 0) {
        const gruposUnicos = [];
        const opcionesHtml = horario
          .filter((s) => {
            const clave = `${s.nombreAsignatura}-${s.nombreGrupo}`;
            if (!gruposUnicos.includes(clave)) {
              gruposUnicos.push(clave);
              return true;
            }
            return false;
          })
          .map(
            (s) =>
              `<option value="${s.idSesion}">${s.nombreAsignatura} - ${s.nombreGrupo}</option>`,
          )
          .join("");

        selectGrupo.innerHTML =
          '<option value="">Seleccione un grupo...</option>' + opcionesHtml;
      } else {
        selectGrupo.innerHTML =
          '<option value="">No tienes asignaturas asignadas</option>';
      }
    } catch (error) {
      console.error(error);
    }

    selectGrupo.addEventListener("change", async () => {
      const idSesion = selectGrupo.value;
      if (!idSesion) {
        selectAlumno.innerHTML =
          '<option value="">Seleccione primero un grupo</option>';
        selectAlumno.disabled = true;
        selectAlumno.style.backgroundColor = "#E9ECEF";
        return;
      }

      try {
        selectAlumno.innerHTML =
          '<option value="">Cargando alumnos...</option>';
        const alumnos = await fetchAPI(
          `/docentes/sesiones/${idSesion}/alumnos`,
        );

        selectAlumno.innerHTML =
          '<option value="">Seleccione un alumno...</option>' +
          alumnos
            .map(
              (a) =>
                `<option value="${a.idMatricula}">${a.nombreAlumno}</option>`,
            )
            .join("");

        selectAlumno.disabled = false;
        selectAlumno.style.backgroundColor = "#FFFFFF";
      } catch (error) {
        selectAlumno.innerHTML =
          '<option value="">Error al cargar alumnos</option>';
      }
    });

    btnGuardar.addEventListener("click", async () => {
      const idMatricula = selectAlumno.value;
      const tipo = selectTipo.value;
      const descripcion = inputDesc.value.trim();

      if (!idMatricula || !descripcion) {
        alert("Por favor, seleccione un alumno y escriba una descripción.");
        return;
      }

      try {
        btnGuardar.disabled = true;
        btnGuardar.innerHTML = "GUARDANDO...";

        await fetchAPI("/partes", {
          method: "POST",
          body: JSON.stringify({
            idMatricula: parseInt(idMatricula),
            tipo: tipo,
            descripcion: descripcion,
            fecha: new Date().toISOString().split("T")[0],
          }),
        });

        alert("Parte disciplinario registrado correctamente.");
        window.dispatchEvent(new Event("hashchange"));
      } catch (error) {
        alert("Error al guardar el parte: " + error.message);
        btnGuardar.disabled = false;
        btnGuardar.innerHTML = "GUARDAR PARTE 💾";
      }
    });
  },
};

function generarListaPartes(partes) {
  if (!partes || partes.length === 0)
    return '<p style="font-size: 20px; color: #757575;">No hay partes disciplinarios registrados.</p>';
  partes.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));

  return partes
    .map((parte) => {
      const fechaStr = new Date(parte.fecha).toLocaleDateString("es-ES");
      let colorTipo = "#000000";
      if (parte.tipo === "GRAVE") colorTipo = "#EC221F";
      if (parte.tipo === "LEVE") colorTipo = "#F39C12";

      let bgEstado = "#E9ECEF",
        colorEstado = "#495057";
      if (parte.estado === "RESUELTO") {
        bgEstado = "#D4EDDA";
        colorEstado = "#155724";
      } else if (parte.estado === "PENDIENTE") {
        bgEstado = "#FFF3CD";
        colorEstado = "#856404";
      }

      return `
            <div style="width: 100%; max-width: 950px; min-height: 90px; background-color: #FFFFFF; border: 1px solid #000000; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center; padding: 15px 20px;">
                <div style="display: flex; flex-direction: column; justify-content: center; width: 25%;">
                    <span style="font-weight: 700; font-size: 20px; color: ${colorTipo}; margin-bottom: 5px;">PARTE ${parte.tipo}</span>
                    <span style="font-weight: 400; font-size: 16px; color: #757575;">${fechaStr} | A: ${parte.nombreAlumno}</span>
                </div>
                <div style="display: flex; flex-direction: column; justify-content: center; width: 55%; padding: 0 20px;">
                    <span style="font-weight: 400; font-size: 18px; color: #333333; font-style: italic;">"${parte.descripcion}"</span>
                </div>
                <div style="display: flex; flex-direction: column; align-items: flex-end; width: 20%;">
                    <div style="background-color: ${bgEstado}; color: ${colorEstado}; padding: 5px 15px; border-radius: 20px; font-weight: 700; font-size: 16px;">${parte.estado}</div>
                </div>
            </div>
        `;
    })
    .join("");
}
