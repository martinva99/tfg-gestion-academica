import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const CalificacionesView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    if (rolActivo !== "ROLE_DOCENTE")
      return `<div class="alert alert-danger text-center mt-5">Acceso denegado.</div>`;

    return `
            <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
                <h1 style="font-weight: 700; font-size: 24px; color: #000000; margin-bottom: 30px;">Gestión de Calificaciones</h1>
                
                <div style="width: 950px; background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; padding: 20px; margin-bottom: 30px; display: flex; gap: 20px; align-items: flex-end;">
                    <div style="flex-grow: 1;">
                        <label style="font-weight: 700; font-size: 18px; margin-bottom: 8px; display: block;">Asignatura y Grupo:</label>
                        <select id="select-asignatura-grupo" style="width: 100%; height: 48px; border-radius: 8px; border: 1px solid #757575; font-size: 18px; padding: 0 10px;">
                            <option value="">Cargando tus grupos...</option>
                        </select>
                    </div>
                    <div style="width: 200px;">
                        <label style="font-weight: 700; font-size: 18px; margin-bottom: 8px; display: block;">Trimestre:</label>
                        <select id="select-trimestre" style="width: 100%; height: 48px; border-radius: 8px; border: 1px solid #757575; font-size: 18px; padding: 0 10px;">
                            <option value="1">1º Trimestre</option>
                            <option value="2">2º Trimestre</option>
                            <option value="3">3º Trimestre</option>
                        </select>
                    </div>
                    <button id="btn-cargar-alumnos" style="width: 150px; height: 48px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 8px; cursor: pointer;">
                        CARGAR
                    </button>
                </div>

                <div id="cartel-estado" style="width: 950px; display: none; padding: 15px; margin-bottom: 20px; border-radius: 8px; font-weight: bold; text-align: center; font-size: 18px;"></div>

                <div id="contenedor-tabla" style="width: 950px; display: none; flex-direction: column;">
                    <table style="width: 100%; border-collapse: collapse; border: 1px solid #000000;">
                        <thead>
                            <tr style="background-color: #CED4DA; height: 40px; border-bottom: 1px solid #000000;">
                                <th style="width: 450px; border-right: 1px solid #000000; padding-left: 15px; text-align: left; font-weight: 700; font-size: 20px;">Alumno/a</th>
                                <th style="width: 150px; border-right: 1px solid #000000; text-align: center; font-weight: 700; font-size: 20px;">Nota (0-10)</th>
                                <th style="width: 350px; text-align: center; font-weight: 700; font-size: 20px;">Observaciones</th>
                            </tr>
                        </thead>
                        <tbody id="tabla-calificaciones"></tbody>
                    </table>
                    <div style="display: flex; justify-content: flex-end; margin-top: 20px;">
                        <button id="btn-guardar-notas" style="width: 200px; height: 50px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 24px; border: none; border-radius: 20px; cursor: pointer;">
                            GUARDAR 💾
                        </button>
                    </div>
                </div>
            </div>
        `;
  },

  init: async () => {
    const selectAsignaturaGrupo = document.getElementById(
      "select-asignatura-grupo",
    );
    const selectTrimestre = document.getElementById("select-trimestre");
    const btnCargar = document.getElementById("btn-cargar-alumnos");
    const contenedorTabla = document.getElementById("contenedor-tabla");
    const tabla = document.getElementById("tabla-calificaciones");
    const btnGuardar = document.getElementById("btn-guardar-notas");
    const cartelEstado = document.getElementById("cartel-estado");

    if (!selectAsignaturaGrupo) return;

    try {
      const misGrupos = await fetchAPI("/docentes/mis-grupos");
      if (misGrupos && misGrupos.length > 0) {
        selectAsignaturaGrupo.innerHTML = misGrupos
          .map(
            (g) =>
              `<option value="${g.idAsignatura}-${g.idGrupo}">${g.nombreAsignatura} - ${g.nombreGrupo}</option>`,
          )
          .join("");
      } else {
        selectAsignaturaGrupo.innerHTML =
          '<option value="">No tienes asignaturas asignadas</option>';
        btnCargar.disabled = true;
      }
    } catch (error) {
      selectAsignaturaGrupo.innerHTML =
        '<option value="">Error al cargar grupos</option>';
    }

    btnCargar.addEventListener("click", async () => {
      const seleccion = selectAsignaturaGrupo.value;
      if (!seleccion) return;
      const [idAsignatura, idGrupo] = seleccion.split("-");
      const trimestre = selectTrimestre.value;

      try {
        btnCargar.innerHTML = "CARGANDO...";
        const calificaciones = await fetchAPI(
          `/docentes/calificaciones/asignatura/${idAsignatura}/grupo/${idGrupo}?trimestre=${trimestre}`,
        );

        if (!calificaciones || calificaciones.length === 0) {
          alert("No hay alumnos matriculados en este grupo.");
          contenedorTabla.style.display = "none";
          cartelEstado.style.display = "none";
          return;
        }

        const estadoEval = calificaciones[0].estadoEvaluacion;
        const isCerrada = estadoEval !== "ABIERTA";

        cartelEstado.style.display = "block";
        if (isCerrada) {
          cartelEstado.style.backgroundColor = "#F8D7DA";
          cartelEstado.style.color = "#721C24";
          cartelEstado.style.border = "1px solid #F5C6CB";
          cartelEstado.innerHTML = `⚠️ La evaluación del ${trimestre}º Trimestre está CERRADA. Solo lectura.`;
          btnGuardar.style.display = "none";
        } else {
          cartelEstado.style.backgroundColor = "#D4EDDA";
          cartelEstado.style.color = "#155724";
          cartelEstado.style.border = "1px solid #C3E6CB";
          cartelEstado.innerHTML = `✅ La evaluación del ${trimestre}º Trimestre está ABIERTA.`;
          btnGuardar.style.display = "block";
        }

        tabla.innerHTML = calificaciones
          .map((calif, index) => {
            const bgColor = index % 2 === 0 ? "#F8F9FA" : "#E9ECEF";
            const notaValue =
              calif.nota !== null && calif.nota !== undefined ? calif.nota : "";
            const obsValue = calif.observaciones || "";
            const disabledAttr = isCerrada ? "disabled" : "";

            return `
                        <tr style="background-color: ${bgColor}; height: 50px;" 
                            data-idmatricula="${calif.idMatricula}" 
                            data-idevaluacion="${calif.idEvaluacion}" 
                            data-iddocente="${calif.idDocente}">
                            
                            <td style="border-right: 1px solid #000000; padding-left: 15px; font-size: 20px;">${calif.nombreAlumno}</td>
                            <td style="border-right: 1px solid #000000; text-align: center;">
                                <input type="number" class="input-nota" min="0" max="10" value="${notaValue}" ${disabledAttr} style="width: 80px; height: 35px; text-align: center; font-size: 18px; transition: background-color 0.3s;">
                            </td>
                            <td style="text-align: center;">
                                <input type="text" class="input-obs" value="${obsValue}" placeholder="Opcional..." ${disabledAttr} style="width: 90%; height: 35px; padding: 0 10px; font-size: 16px;">
                            </td>
                        </tr>
                    `;
          })
          .join("");

        contenedorTabla.style.display = "flex";
      } catch (error) {
        alert("Error al cargar los alumnos: " + error.message);
        contenedorTabla.style.display = "none";
        cartelEstado.style.display = "none";
      } finally {
        btnCargar.innerHTML = "CARGAR";
      }
    });

    tabla.addEventListener("input", (e) => {
      if (e.target.classList.contains("input-nota")) {
        const valor = parseInt(e.target.value);
        const inputs = tabla.querySelectorAll(".input-nota");
        let hayErrores = false;

        inputs.forEach((input) => {
          const val = parseInt(input.value);
          if (input.value !== "" && (isNaN(val) || val < 0 || val > 10)) {
            input.style.backgroundColor = "#F8D7DA";
            input.style.border = "2px solid #DC3545";
            hayErrores = true;
          } else {
            input.style.backgroundColor = "#FFFFFF";
            input.style.border = "1px solid #757575";
          }
        });

        btnGuardar.disabled = hayErrores;
        btnGuardar.style.opacity = hayErrores ? "0.5" : "1";
      }
    });

    btnGuardar.addEventListener("click", async () => {
      const filas = tabla.querySelectorAll("tr");
      const notasAGuardar = [];

      filas.forEach((fila) => {
        const idMatricula = fila.getAttribute("data-idmatricula");
        const idEvaluacion = fila.getAttribute("data-idevaluacion");
        const idDocente = fila.getAttribute("data-iddocente");
        const notaInput = fila.querySelector(".input-nota").value;
        const obsInput = fila.querySelector(".input-obs").value;

        if (notaInput !== "") {
          notasAGuardar.push({
            idMatricula: parseInt(idMatricula),
            idEvaluacion: parseInt(idEvaluacion),
            idDocente: parseInt(idDocente),
            nota: parseInt(notaInput),
            observaciones: obsInput,
          });
        }
      });

      if (notasAGuardar.length === 0) {
        alert("No has introducido ninguna nota.");
        return;
      }

      try {
        btnGuardar.disabled = true;
        btnGuardar.innerHTML = "GUARDANDO...";

        await fetchAPI("/calificaciones/lote", {
          method: "POST",
          body: JSON.stringify(notasAGuardar),
        });

        alert("Calificaciones guardadas correctamente.");
      } catch (error) {
        alert("Error al guardar: " + error.message);
      } finally {
        btnGuardar.disabled = false;
        btnGuardar.innerHTML = "GUARDAR 💾";
      }
    });
  },
};
