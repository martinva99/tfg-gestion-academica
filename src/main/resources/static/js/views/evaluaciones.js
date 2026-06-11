import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const EvaluacionesView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    if (rolActivo !== "ROLE_JEFATURA")
      return `<div class="alert alert-danger text-center mt-5">Acceso denegado.</div>`;

    try {
      const evaluaciones = await fetchAPI("/jefatura/evaluaciones");

      if (!evaluaciones || evaluaciones.length === 0) {
        return `
                    <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
                        <h1 style="font-weight: 700; font-size: 24px; margin-bottom: 40px;">Gestión de Evaluaciones</h1>
                        <p style="font-size: 20px;">No hay evaluaciones registradas en el sistema.</p>
                    </div>
                `;
      }

      const evaluacionesPorGrupo = {};
      evaluaciones.forEach((ev) => {
        if (!evaluacionesPorGrupo[ev.nombreGrupo]) {
          evaluacionesPorGrupo[ev.nombreGrupo] = [];
        }
        evaluacionesPorGrupo[ev.nombreGrupo].push(ev);
      });

      let htmlContenido = "";
      for (const [grupo, evs] of Object.entries(evaluacionesPorGrupo)) {
        htmlContenido += `
                    <div style="width: 950px; margin-bottom: 30px;">
                        <h2 style="font-weight: 700; font-size: 22px; margin-bottom: 15px; border-bottom: 2px solid #2C3E50; padding-bottom: 5px;">
                            Grupo: ${grupo}
                        </h2>
                        <div style="display: flex; flex-direction: column; gap: 10px;">
                            ${evs.map((ev) => generarFilaEvaluacion(ev)).join("")}
                        </div>
                    </div>
                `;
      }

      return `
                <div id="contenedor-evaluaciones" style="display: flex; flex-direction: column; align-items: center; width: 100%;">
                    <h1 style="font-weight: 700; font-size: 24px; color: #000000; margin-bottom: 40px;">Gestión de Evaluaciones</h1>
                    ${htmlContenido}
                </div>
            `;
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar las evaluaciones: ${error.message}</div>`;
    }
  },

  init: () => {
    const contenedorEvaluaciones = document.getElementById(
      "contenedor-evaluaciones",
    );
    if (!contenedorEvaluaciones) return;

    contenedorEvaluaciones.addEventListener("click", async (e) => {
      if (e.target.classList.contains("btn-cambiar-estado")) {
        const idEvaluacion = e.target.getAttribute("data-id");
        const estadoActual = e.target.getAttribute("data-estado");
        const nuevoEstado = estadoActual === "ABIERTA" ? "CERRADA" : "ABIERTA";

        const accion = nuevoEstado === "CERRADA" ? "CERRAR" : "ABRIR";
        if (!confirm(`¿Estás seguro de que deseas ${accion} esta evaluación?`))
          return;

        try {
          e.target.disabled = true;
          e.target.innerHTML = "Procesando...";

          await fetchAPI(`/jefatura/evaluaciones/${idEvaluacion}/estado`, {
            method: "PATCH",
            body: JSON.stringify({
              nuevoEstado: nuevoEstado,
            }),
          });

          window.dispatchEvent(new Event("hashchange"));
        } catch (error) {
          alert("Error al cambiar el estado: " + error.message);
          e.target.disabled = false;
          e.target.innerHTML = accion + " EVALUACIÓN";
        }
      }
    });
  },
};

function generarFilaEvaluacion(ev) {
  const isAbierta = ev.estado === "ABIERTA";

  const bgEstado = isAbierta ? "#D4EDDA" : "#F8D7DA";
  const colorEstado = isAbierta ? "#155724" : "#721C24";

  const bgBoton = isAbierta ? "#EC221F" : "#04A12E";
  const textoBoton = isAbierta ? "CERRAR EVALUACIÓN" : "ABRIR EVALUACIÓN";

  return `
        <div style="width: 100%; height: 70px; background-color: #FFFFFF; border: 1px solid #000000; border-radius: 8px; display: flex; justify-content: space-between; align-items: center; padding: 0 20px;">
            
            <div style="display: flex; align-items: center; gap: 20px;">
                <span style="font-weight: 700; font-size: 20px;">${ev.trimestre}º Trimestre</span>
                <span style="background-color: ${bgEstado}; color: ${colorEstado}; padding: 5px 15px; border-radius: 20px; font-weight: 700; font-size: 14px;">
                    ${ev.estado}
                </span>
            </div>

            <button class="btn-cambiar-estado" data-id="${ev.idEvaluacion}" data-estado="${ev.estado}" 
                    style="width: 200px; height: 40px; background-color: ${bgBoton}; color: #FFFFFF; font-weight: 700; font-size: 16px; border: none; border-radius: 8px; cursor: pointer; transition: opacity 0.2s;">
                ${textoBoton}
            </button>
            
        </div>
    `;
}
