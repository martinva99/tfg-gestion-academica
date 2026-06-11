import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const HorariosJefaturaView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    if (rolActivo !== "ROLE_JEFATURA")
      return `<div class="alert alert-danger text-center mt-5">Acceso denegado.</div>`;

    try {
      const grupos = await fetchAPI("/grupos");
      const opcionesGrupos = grupos
        .map(
          (g) =>
            `<option value="${g.idGrupo}">${g.nombre} (${g.cursoEscolar})</option>`,
        )
        .join("");

      return `
                <div id="contenedor-horarios" style="display: flex; flex-direction: column; align-items: center; width: 100%; position: relative;">
                    
                    <div id="selector-view" style="width: 100%; display: flex; flex-direction: column; align-items: center;">
                        <h1 style="font-weight: 700; font-size: 24px; margin-bottom: 40px;">Gestión de Horarios</h1>
                        <div style="width: 600px; background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; padding: 20px; display: flex; gap: 20px; align-items: flex-end;">
                            <div style="flex-grow: 1;">
                                <label style="font-weight: 700; font-size: 18px; margin-bottom: 8px; display: block;">Seleccione un Grupo:</label>
                                <select id="select-grupo" style="width: 100%; height: 48px; border-radius: 8px; border: 1px solid #757575; font-size: 18px; padding: 0 10px;">
                                    <option value="">-- Seleccionar --</option>
                                    ${opcionesGrupos}
                                </select>
                            </div>
                            <button id="btn-cargar-horario" style="width: 150px; height: 48px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 8px; cursor: pointer;">
                                CARGAR
                            </button>
                        </div>
                    </div>

                    <div id="horario-view" style="width: 100%; display: none; flex-direction: column; align-items: center;">
                        <h1 id="titulo-horario" style="font-weight: 700; font-size: 24px; margin-bottom: 40px;">Gestión de horarios - Grupo</h1>
                        
                        <div id="tabla-container" style="width: 100%; display: flex; justify-content: center;">
                            <!-- La tabla se inyecta aquí -->
                        </div>

                        <div style="width: 950px; display: flex; justify-content: flex-end; margin-top: 30px;">
                            <button id="btn-guardar-horario" style="width: 200px; height: 50px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 24px; border: none; border-radius: 20px; cursor: pointer; display: flex; justify-content: center; align-items: center;">
                                GUARDAR <span style="margin-left: 10px;">💾</span>
                            </button>
                        </div>
                    </div>

                    <div id="modal-overlay" style="display: none; position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0, 0, 0, 0.4); backdrop-filter: blur(5px); z-index: 999; justify-content: center; align-items: center;">
                        <div style="width: 400px; height: 350px; background-color: #FFFFFF; border-radius: 8px; padding: 20px; display: flex; flex-direction: column; position: relative; box-shadow: 0 4px 15px rgba(0,0,0,0.2);">
                            
                            <button id="btn-cerrar-modal" style="position: absolute; top: 10px; right: 15px; background: none; border: none; font-size: 24px; cursor: pointer; color: #757575;">✖</button>
                            
                            <h2 style="font-weight: 700; font-size: 24px; text-align: center; margin-top: 10px; margin-bottom: 5px;">Asignar sesión</h2>
                            <p id="modal-subtitulo" style="font-weight: 400; font-size: 18px; text-align: center; color: #333333; margin-bottom: 25px;">Lunes, 8:30 - 9:25</p>

                            <select id="modal-asignatura" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; font-size: 16px; padding: 0 10px; margin-bottom: 15px;">
                                <option value="">Seleccione asignatura...</option>
                            </select>

                            <select id="modal-docente" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; font-size: 16px; padding: 0 10px; margin-bottom: auto;">
                                <option value="">Seleccione docente disponible...</option>
                            </select>

                            <input type="text" id="modal-aula" placeholder="Ej: Aula 101" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; font-size: 16px; padding: 0 10px; margin-bottom: auto;">

                            <div style="display: flex; justify-content: flex-end;">
                                <button id="btn-hecho-modal" disabled style="width: 120px; height: 40px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 10px; cursor: not-allowed; opacity: 0.5; display: flex; justify-content: center; align-items: center;">
                                    HECHO <span style="margin-left: 8px;">✔️</span>
                                </button>
                            </div>
                        </div>
                    </div>

                </div>
            `;
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar la vista: ${error.message}</div>`;
    }
  },

  init: () => {
    const contenedor = document.getElementById("contenedor-horarios");
    if (!contenedor) return;

    const btnCargar = document.getElementById("btn-cargar-horario");
    const selectGrupo = document.getElementById("select-grupo");
    const selectorView = document.getElementById("selector-view");
    const horarioView = document.getElementById("horario-view");
    const tituloHorario = document.getElementById("titulo-horario");
    const tablaContainer = document.getElementById("tabla-container");
    const btnGuardar = document.getElementById("btn-guardar-horario");

    const modalOverlay = document.getElementById("modal-overlay");
    const btnCerrarModal = document.getElementById("btn-cerrar-modal");
    const modalSubtitulo = document.getElementById("modal-subtitulo");
    const selectAsignatura = document.getElementById("modal-asignatura");
    const selectDocente = document.getElementById("modal-docente");
    const inputAula = document.getElementById("modal-aula");
    const btnHecho = document.getElementById("btn-hecho-modal");

    let grupoActual = null;
    let celdaActual = null;
    let sesionesNuevas = [];

    btnCargar.addEventListener("click", async () => {
      const idGrupo = selectGrupo.value;
      const nombreGrupo = selectGrupo.options[selectGrupo.selectedIndex].text;
      if (!idGrupo) return;

      try {
        btnCargar.innerHTML = "CARGANDO...";
        grupoActual = idGrupo;

        const horario = await fetchAPI(`/jefatura/horarios/grupo/${idGrupo}`);
        const franjasDB = await fetchAPI("/franjas-horarias");

        selectorView.style.display = "none";
        horarioView.style.display = "flex";
        tituloHorario.innerText = `Gestión de horarios - Grupo ${nombreGrupo}`;

        tablaContainer.innerHTML = generarTablaInteractiva(horario, franjasDB);
      } catch (error) {
        alert("Error al cargar el horario: " + error.message);
      } finally {
        btnCargar.innerHTML = "CARGAR";
      }
    });

    tablaContainer.addEventListener("click", async (e) => {
      const td = e.target.closest(".celda-interactiva");
      if (!td) return;

      celdaActual = td;
      const dia = td.getAttribute("data-dia");
      const hora = td.getAttribute("data-hora");
      const idFranja = td.getAttribute("data-idfranja");

      modalSubtitulo.innerText = `${dia}, ${hora}`;
      modalOverlay.style.display = "flex";

      try {
        const asignaturas = await fetchAPI(
          `/grupos/${grupoActual}/asignaturas`,
        );
        selectAsignatura.innerHTML =
          '<option value="">Seleccione asignatura...</option>' +
          asignaturas
            .map(
              (a) => `<option value="${a.idAsignatura}">${a.nombre}</option>`,
            )
            .join("");
      } catch (e) {
        console.error(e);
      }

      try {
        const docentes = await fetchAPI(
          `/docentes/disponibles?idFranja=${idFranja}`,
        );
        selectDocente.innerHTML =
          '<option value="">Seleccione docente disponible...</option>' +
          docentes
            .map(
              (d) =>
                `<option value="${d.idDocente}">${d.nombre} ${d.apellidos}</option>`,
            )
            .join("");
      } catch (e) {
        console.error(e);
      }
    });

    const validarModal = () => {
      if (selectAsignatura.value && selectDocente.value) {
        btnHecho.disabled = false;
        btnHecho.style.cursor = "pointer";
        btnHecho.style.opacity = "1";
      } else {
        btnHecho.disabled = true;
        btnHecho.style.cursor = "not-allowed";
        btnHecho.style.opacity = "0.5";
      }
    };

    selectAsignatura.addEventListener("change", validarModal);
    selectDocente.addEventListener("change", validarModal);

    btnCerrarModal.addEventListener("click", () => {
      modalOverlay.style.display = "none";
      selectAsignatura.value = "";
      selectDocente.value = "";
      inputAula.value = "";
      validarModal();
    });

    btnHecho.addEventListener("click", () => {
      const idAsignatura = selectAsignatura.value;
      const nombreAsignatura =
        selectAsignatura.options[selectAsignatura.selectedIndex].text;
      const idDocente = selectDocente.value;
      const nombreDocente =
        selectDocente.options[selectDocente.selectedIndex].text;
      const idFranja = celdaActual.getAttribute("data-idfranja");
      const aula = inputAula.value.trim();
      sesionesNuevas.push({
        idGrupo: parseInt(grupoActual),
        idAsignatura: parseInt(idAsignatura),
        idDocente: parseInt(idDocente),
        idFranja: parseInt(idFranja),
        aula: aula,
      });

      const colorFondo = getColorAsignatura(nombreAsignatura);

      celdaActual.removeAttribute("onmouseover");
      celdaActual.removeAttribute("onmouseout");

      celdaActual.style.backgroundColor = colorFondo;

      celdaActual.innerHTML = `
                <div style="font-weight: bold; font-size: 20px; color: #000; text-align: center; width: 100%;">${nombreAsignatura}</div>
                <div style="font-weight: bold; font-size: 18px; color: #000; text-align: center; width: 100%;">${nombreDocente}</div>
            `;

      btnCerrarModal.click();
    });

    btnGuardar.addEventListener("click", async () => {
      if (sesionesNuevas.length === 0) {
        alert("No has hecho ningún cambio en el horario.");
        return;
      }

      try {
        btnGuardar.disabled = true;
        btnGuardar.innerHTML = "GUARDANDO...";

        await fetchAPI("/jefatura/horarios/lote", {
          method: "POST",
          body: JSON.stringify(sesionesNuevas),
        });

        alert("Horario actualizado correctamente.");
        sesionesNuevas = [];
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

function getColorAsignatura(nombre) {
  let hash = 0;
  for (let i = 0; i < nombre.length; i++)
    hash = nombre.charCodeAt(i) + ((hash << 5) - hash);
  const r = (hash & 0xff0000) >> 16,
    g = (hash & 0x00ff00) >> 8,
    b = hash & 0x0000ff;
  return `rgb(${Math.floor((r + 255) / 2)}, ${Math.floor((g + 255) / 2)}, ${Math.floor((b + 255) / 2)})`;
}

function generarTablaInteractiva(horarioDTO, franjasDB) {
  const dias = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"];
  const franjasEstaticas = [
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

  if (horarioDTO && horarioDTO.sesionesPorDia) {
    for (const [diaStr, sesiones] of Object.entries(
      horarioDTO.sesionesPorDia,
    )) {
      const col = dias.indexOf(diaStr);
      if (col !== -1) {
        sesiones.forEach((sesion) => {
          const fila = franjasEstaticas.findIndex((f) =>
            sesion.horaInicio.startsWith(f.start),
          );
          if (fila !== -1) matriz[fila][col] = sesion;
        });
      }
    }
  }

  let filasHtml = "";
  matriz.forEach((fila, indexFila) => {
    const esRecreo1 = indexFila === 2;
    const esRecreo2 = indexFila === 5;
    const estiloFila =
      esRecreo1 || esRecreo2 ? "border-bottom: 3px solid #04A12E;" : "";

    let celdasHtml = `<td style="border: 1px solid #000; width: 158px; height: 90px; font-weight: bold; font-size: 20px; text-align: center; background-color: #CED4DA;">${franjasEstaticas[indexFila].label}</td>`;

    fila.forEach((sesion, indexCol) => {
      const diaActual = dias[indexCol];
      const franjaActual = franjasEstaticas[indexFila];

      const franjaReal = franjasDB.find(
        (f) =>
          f.diaSemana === diaActual &&
          f.horaInicio.startsWith(franjaActual.start),
      );
      const idFranjaReal = franjaReal ? franjaReal.idFranja : "";

      if (sesion) {
        const colorFondo = getColorAsignatura(sesion.nombreAsignatura);
        celdasHtml += `
                    <td class="celda-interactiva" data-dia="${diaActual}" data-hora="${franjaActual.label}" data-idfranja="${idFranjaReal}" 
                        style="border: 1px solid #000; width: 158px; height: 90px; background-color: ${colorFondo}; text-align: center; vertical-align: middle; cursor: pointer; transition: opacity 0.2s;">
                        <div style="font-weight: bold; font-size: 20px; color: #000;">${sesion.nombreAsignatura}</div>
                        <div style="font-weight: bold; font-size: 18px; color: #000;">${sesion.nombreDocente}</div>
                    </td>`;
      } else {
        celdasHtml += `
                    <td class="celda-interactiva" data-dia="${diaActual}" data-hora="${franjaActual.label}" data-idfranja="${idFranjaReal}" 
                        style="border: 1px solid #000; width: 158px; height: 90px; background-color: #FFFFFF; cursor: pointer; transition: background-color 0.2s;"
                        onmouseover="this.style.backgroundColor='#F0F8FF'" onmouseout="this.style.backgroundColor='#FFFFFF'">
                        <div style="color: #757575; font-size: 14px; text-align: center;">+ Asignar</div>
                    </td>`;
      }
    });

    filasHtml += `<tr style="${estiloFila}">${celdasHtml}</tr>`;
  });

  return `<table style="border-collapse: collapse; background-color: #FFFFFF; box-shadow: 0px 4px 10px rgba(0,0,0,0.05);">
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
                <tbody>${filasHtml}</tbody>
            </table>`;
}
