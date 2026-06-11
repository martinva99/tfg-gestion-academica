import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const MatriculasView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    if (rolActivo !== "ROLE_SECRETARIA")
      return `<div class="alert alert-danger text-center mt-5">Acceso denegado.</div>`;

    try {
      const grupos = await fetchAPI("/grupos");
      const opcionesGrupos = grupos
        .map(
          (g) =>
            `<option value="${g.idGrupo}" data-idanio="${g.anioAcademico ? g.anioAcademico.idAnio : 1}">${g.nombre} (${g.cursoEscolar})</option>`,
        )
        .join("");

      return `
                <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
                    <h1 style="font-weight: 700; font-size: 24px; margin-bottom: 40px;">Gestión de Matrículas</h1>
                    
                    <div style="display: flex; gap: 20px; margin-bottom: 40px;">
                        <button id="btn-flujo-nuevo" style="width: 300px; height: 60px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 8px; cursor: pointer; transition: opacity 0.2s;">
                            ➕ ALUMNO NUEVO
                        </button>
                        <button id="btn-flujo-existente" style="width: 300px; height: 60px; background-color: #FFFFFF; color: #2C3E50; font-weight: 700; font-size: 18px; border: 2px solid #2C3E50; border-radius: 8px; cursor: pointer; transition: background-color 0.2s;">
                            🔍 ALUMNO EXISTENTE
                        </button>
                    </div>

                    <div id="form-nuevo-alumno" style="display: none; width: 800px; background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; padding: 30px; flex-direction: column; gap: 20px;">
                        <h2 style="font-weight: 700; font-size: 20px; margin: 0 0 10px 0; border-bottom: 2px solid #2C3E50; padding-bottom: 10px;">1. Datos del Usuario</h2>
                        
                        <div style="display: flex; gap: 20px;">
                            <div style="flex: 1;">
                                <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Nombre:</label>
                                <input type="text" id="input-nombre" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; padding: 0 10px;">
                            </div>
                            <div style="flex: 1;">
                                <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Apellidos:</label>
                                <input type="text" id="input-apellidos" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; padding: 0 10px;">
                            </div>
                        </div>

                        <div style="display: flex; gap: 20px;">
                            <div style="flex: 1;">
                                <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Email:</label>
                                <input type="email" id="input-email" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; padding: 0 10px;">
                            </div>
                            <div style="flex: 1;">
                                <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Teléfono:</label>
                                <input type="text" id="input-telefono" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; padding: 0 10px;">
                            </div>
                        </div>

                        <div>
                            <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Contraseña (Mín. 8 caracteres):</label>
                            <input type="password" id="input-password" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; padding: 0 10px;">
                        </div>

                        <h2 style="font-weight: 700; font-size: 20px; margin: 20px 0 10px 0; border-bottom: 2px solid #2C3E50; padding-bottom: 10px;">2. Datos Académicos</h2>
                        
                        <div>
                            <label style="font-weight: 700; font-size: 16px; margin-bottom: 5px; display: block;">Fecha de Nacimiento:</label>
                            <input type="date" id="input-fecha-nac" style="width: 100%; height: 40px; border-radius: 8px; border: 1px solid #757575; padding: 0 10px;">
                        </div>

                        <div style="display: flex; justify-content: flex-end; margin-top: 20px;">
                            <button id="btn-crear-alumno" style="width: 200px; height: 50px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 20px; cursor: pointer;">
                                CREAR ALUMNO 👤
                            </button>
                        </div>
                    </div>

                    <div id="form-matricular" style="display: flex; width: 800px; background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; padding: 30px; flex-direction: column; gap: 20px;">
                        
                        <div>
                            <label style="font-weight: 700; font-size: 18px; margin-bottom: 8px; display: block;">1. Buscar Alumno por Email:</label>
                            <div style="display: flex; gap: 10px;">
                                <input type="email" id="search-email" placeholder="ejemplo@test.com" style="flex-grow: 1; height: 48px; border-radius: 8px; border: 1px solid #757575; font-size: 18px; padding: 0 10px;">
                                <button id="btn-buscar-alumno" style="width: 120px; height: 48px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 16px; border: none; border-radius: 8px; cursor: pointer;">
                                    BUSCAR
                                </button>
                            </div>
                            <div id="info-alumno-encontrado" style="margin-top: 10px; font-weight: bold; color: #04A12E; display: none;"></div>
                            <input type="hidden" id="hidden-id-alumno"> <!-- Aquí guardamos el ID cuando lo encontremos -->
                        </div>

                        <div>
                            <label style="font-weight: 700; font-size: 18px; margin-bottom: 8px; display: block;">2. Seleccione Grupo:</label>
                            <select id="select-grupo" disabled style="width: 100%; height: 48px; border-radius: 8px; border: 1px solid #757575; font-size: 18px; padding: 0 10px; background-color: #E9ECEF;">
                                <option value="">-- Seleccionar grupo --</option>
                                ${opcionesGrupos}
                            </select>
                        </div>

                        <div id="contenedor-asignaturas" style="display: none; flex-direction: column; margin-top: 10px;">
                            <label style="font-weight: 700; font-size: 18px; margin-bottom: 15px; display: block;">3. Seleccione Asignaturas:</label>
                            <div id="lista-asignaturas" style="display: flex; flex-direction: column; gap: 10px; background-color: #FFFFFF; padding: 15px; border: 1px solid #CED4DA; border-radius: 8px;">
                                <!-- Checkboxes generados por JS -->
                            </div>
                        </div>

                        <div style="display: flex; justify-content: flex-end; margin-top: 20px;">
                            <button id="btn-matricular" disabled style="width: 200px; height: 50px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 20px; border: none; border-radius: 20px; cursor: not-allowed; opacity: 0.5;">
                                MATRICULAR 📝
                            </button>
                        </div>
                    </div>
                </div>
            `;
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar la vista: ${error.message}</div>`;
    }
  },

  init: () => {
    const btnFlujoNuevo = document.getElementById("btn-flujo-nuevo");
    const btnFlujoExistente = document.getElementById("btn-flujo-existente");
    const formNuevo = document.getElementById("form-nuevo-alumno");
    const formMatricular = document.getElementById("form-matricular");

    const inputNombre = document.getElementById("input-nombre");
    const inputApellidos = document.getElementById("input-apellidos");
    const inputEmail = document.getElementById("input-email");
    const inputTelefono = document.getElementById("input-telefono");
    const inputPassword = document.getElementById("input-password");
    const inputFechaNac = document.getElementById("input-fecha-nac");
    const btnCrearAlumno = document.getElementById("btn-crear-alumno");

    const searchEmail = document.getElementById("search-email");
    const btnBuscar = document.getElementById("btn-buscar-alumno");
    const infoAlumno = document.getElementById("info-alumno-encontrado");
    const hiddenIdAlumno = document.getElementById("hidden-id-alumno");
    const selectGrupo = document.getElementById("select-grupo");
    const contenedorAsignaturas = document.getElementById(
      "contenedor-asignaturas",
    );
    const listaAsignaturas = document.getElementById("lista-asignaturas");
    const btnMatricular = document.getElementById("btn-matricular");

    if (!btnFlujoNuevo) return;

    btnFlujoNuevo.addEventListener("click", () => {
      formNuevo.style.display = "flex";
      formMatricular.style.display = "none";
      btnFlujoNuevo.style.backgroundColor = "#2C3E50";
      btnFlujoNuevo.style.color = "#FFFFFF";
      btnFlujoExistente.style.backgroundColor = "#FFFFFF";
      btnFlujoExistente.style.color = "#2C3E50";
    });

    btnFlujoExistente.addEventListener("click", () => {
      formNuevo.style.display = "none";
      formMatricular.style.display = "flex";
      btnFlujoExistente.style.backgroundColor = "#2C3E50";
      btnFlujoExistente.style.color = "#FFFFFF";
      btnFlujoNuevo.style.backgroundColor = "#FFFFFF";
      btnFlujoNuevo.style.color = "#2C3E50";
    });

    btnCrearAlumno.addEventListener("click", async () => {
      if (
        !inputNombre.value ||
        !inputApellidos.value ||
        !inputEmail.value ||
        !inputPassword.value ||
        !inputFechaNac.value
      ) {
        alert("Por favor, rellene todos los campos obligatorios.");
        return;
      }

      try {
        btnCrearAlumno.disabled = true;
        btnCrearAlumno.innerHTML = "CREANDO...";

        const usuarioCreado = await fetchAPI("/usuarios/registro", {
          method: "POST",
          body: JSON.stringify({
            nombre: inputNombre.value,
            apellidos: inputApellidos.value,
            email: inputEmail.value,
            password: inputPassword.value,
            telefonoContacto: inputTelefono.value,
          }),
        });

        await fetchAPI(
          `/usuarios/${usuarioCreado.idUsuario}/roles/ROLE_ALUMNO`,
          { method: "POST" },
        );

        await fetchAPI("/alumnos/asignar", {
          method: "POST",
          body: JSON.stringify({
            idUsuario: usuarioCreado.idUsuario,
            fechaNacimiento: inputFechaNac.value,
          }),
        });

        alert(
          "Alumno creado con éxito. Ahora puedes matricularlo en la otra pestaña.",
        );

        inputNombre.value = "";
        inputApellidos.value = "";
        inputPassword.value = "";
        inputFechaNac.value = "";
        searchEmail.value = inputEmail.value;
        btnFlujoExistente.click();
        btnBuscar.click();
      } catch (error) {
        alert("Error al crear alumno: " + error.message);
      } finally {
        btnCrearAlumno.disabled = false;
        btnCrearAlumno.innerHTML = "CREAR ALUMNO 👤";
      }
    });

    btnBuscar.addEventListener("click", async () => {
      const email = searchEmail.value.trim();
      if (!email) return;

      try {
        btnBuscar.innerHTML = "...";
        const alumno = await fetchAPI(`/alumnos/buscar?email=${email}`);

        infoAlumno.style.display = "block";
        infoAlumno.innerHTML = `✅ Alumno encontrado: ${alumno.nombre} ${alumno.apellidos}`;
        hiddenIdAlumno.value = alumno.idAlumno;

        selectGrupo.disabled = false;
        selectGrupo.style.backgroundColor = "#FFFFFF";
      } catch (error) {
        infoAlumno.style.display = "block";
        infoAlumno.style.color = "#EC221F";
        infoAlumno.innerHTML = `❌ No se encontró ningún alumno con ese email.`;
        hiddenIdAlumno.value = "";
        selectGrupo.disabled = true;
        selectGrupo.style.backgroundColor = "#E9ECEF";
      } finally {
        btnBuscar.innerHTML = "BUSCAR";
      }
    });

    const validarMatricula = () => {
      const checks = listaAsignaturas.querySelectorAll(
        'input[type="checkbox"]:checked',
      );
      if (hiddenIdAlumno.value && selectGrupo.value && checks.length > 0) {
        btnMatricular.disabled = false;
        btnMatricular.style.cursor = "pointer";
        btnMatricular.style.opacity = "1";
      } else {
        btnMatricular.disabled = true;
        btnMatricular.style.cursor = "not-allowed";
        btnMatricular.style.opacity = "0.5";
      }
    };

    listaAsignaturas.addEventListener("change", validarMatricula);

    selectGrupo.addEventListener("change", async () => {
      const idGrupo = selectGrupo.value;
      if (!idGrupo) {
        contenedorAsignaturas.style.display = "none";
        validarMatricula();
        return;
      }

      try {
        listaAsignaturas.innerHTML = "<p>Cargando asignaturas...</p>";
        contenedorAsignaturas.style.display = "flex";

        const asignaturas = await fetchAPI(`/grupos/${idGrupo}/asignaturas`);

        if (asignaturas.length === 0) {
          listaAsignaturas.innerHTML =
            '<p style="color: #EC221F;">Este grupo no tiene asignaturas configuradas.</p>';
        } else {
          listaAsignaturas.innerHTML = asignaturas
            .map(
              (a) => `
                        <label style="font-size: 18px; display: flex; align-items: center; gap: 10px; cursor: pointer;">
                            <input type="checkbox" value="${a.idAsignatura}" style="width: 20px; height: 20px;" checked>
                            ${a.nombre}
                        </label>
                    `,
            )
            .join("");
        }
        validarMatricula();
      } catch (error) {
        listaAsignaturas.innerHTML =
          '<p style="color: #EC221F;">Error al cargar asignaturas.</p>';
      }
    });

    btnMatricular.addEventListener("click", async () => {
      const idAlumno = hiddenIdAlumno.value;
      const idGrupo = selectGrupo.value;
      const opcionGrupo = selectGrupo.options[selectGrupo.selectedIndex];
      const idAnioAcademico = opcionGrupo.getAttribute("data-idanio");

      const checks = listaAsignaturas.querySelectorAll(
        'input[type="checkbox"]:checked',
      );
      const idsAsignaturas = Array.from(checks).map((c) => parseInt(c.value));

      try {
        btnMatricular.disabled = true;
        btnMatricular.innerHTML = "PROCESANDO...";

        const matriculasLote = idsAsignaturas.map((idAsig) => ({
          idAlumno: parseInt(idAlumno),
          idGrupo: parseInt(idGrupo),
          idAnio: parseInt(idAnioAcademico),
          idAsignatura: idAsig,
        }));

        await fetchAPI("/secretaria/matriculas/lote", {
          method: "POST",
          body: JSON.stringify(matriculasLote),
        });

        alert("Alumno matriculado correctamente.");

        btnMatricular.innerHTML = "MATRICULAR 📝";
        searchEmail.value = "";
        infoAlumno.style.display = "none";
        hiddenIdAlumno.value = "";
        selectGrupo.value = "";
        selectGrupo.disabled = true;
        selectGrupo.style.backgroundColor = "#E9ECEF";
        contenedorAsignaturas.style.display = "none";
        validarMatricula();
      } catch (error) {
        alert("Error al matricular: " + error.message);
        btnMatricular.disabled = false;
        btnMatricular.innerHTML = "MATRICULAR 📝";
      }
    });
  },
};
