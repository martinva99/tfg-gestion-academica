import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const UsuariosView = {
  render: async () => {
    const rolActivo = auth.getRolActivo();
    if (rolActivo !== "ROLE_ADMINISTRADOR")
      return `<div class="alert alert-danger text-center mt-5">Acceso denegado.</div>`;

    return `
            <div style="display: flex; flex-direction: column; align-items: center; width: 100%; padding-bottom: 50px;">
                <h1 style="font-weight: 700; font-size: 24px; margin-bottom: 40px;">Administración de Usuarios</h1>
                
                <div style="display: flex; gap: 20px; margin-bottom: 40px;">
                    <button id="btn-flujo-nuevo" style="width: 300px; height: 60px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 8px; cursor: pointer; transition: opacity 0.2s;">
                        ➕ NUEVO USUARIO
                    </button>
                    <button id="btn-flujo-existente" style="width: 300px; height: 60px; background-color: #FFFFFF; color: #2C3E50; font-weight: 700; font-size: 18px; border: 2px solid #2C3E50; border-radius: 8px; cursor: pointer; transition: background-color 0.2s;">
                        🔍 GESTIONAR EXISTENTE
                    </button>
                </div>

                <div id="form-nuevo" style="display: none; width: 800px; background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; padding: 30px; flex-direction: column; gap: 20px;">
                    <h2 style="font-weight: 700; font-size: 20px; border-bottom: 2px solid #2C3E50; padding-bottom: 10px;">1. Datos Personales</h2>
                    
                    <div style="display: flex; gap: 20px;">
                        <div style="flex: 1;"><label>Nombre:</label><input type="text" id="n-nombre" class="form-control"></div>
                        <div style="flex: 1;"><label>Apellidos:</label><input type="text" id="n-apellidos" class="form-control"></div>
                    </div>
                    <div style="display: flex; gap: 20px;">
                        <div style="flex: 1;"><label>Email:</label><input type="email" id="n-email" class="form-control"></div>
                        <div style="flex: 1;"><label>Teléfono:</label><input type="text" id="n-telefono" class="form-control"></div>
                    </div>
                    <div><label>Contraseña (Mín. 8 caracteres):</label><input type="password" id="n-password" class="form-control"></div>

                    <h2 style="font-weight: 700; font-size: 20px; margin-top: 20px; border-bottom: 2px solid #2C3E50; padding-bottom: 10px;">2. Roles y Perfiles</h2>
                    
                    <div style="display: flex; flex-wrap: wrap; gap: 15px;" id="n-roles-container">
                        <label><input type="checkbox" value="ROLE_ALUMNO" class="n-rol-check"> Alumno</label>
                        <label><input type="checkbox" value="ROLE_DOCENTE" class="n-rol-check"> Docente</label>
                        <label><input type="checkbox" value="ROLE_TUTOR_LEGAL" class="n-rol-check"> Tutor Legal</label>
                        <label><input type="checkbox" value="ROLE_SECRETARIA" class="n-rol-check"> Secretaría</label>
                        <label><input type="checkbox" value="ROLE_JEFATURA" class="n-rol-check"> Jefatura</label>
                        <label><input type="checkbox" value="ROLE_ADMINISTRADOR" class="n-rol-check"> Administrador</label>
                    </div>

                    <div id="n-extra-alumno" style="display: none; background: #E9ECEF; padding: 15px; border-radius: 8px;">
                        <label>Fecha de Nacimiento (Obligatorio para Alumno):</label>
                        <input type="date" id="n-fecha-nac" class="form-control">
                    </div>
                    <div id="n-extra-docente" style="display: none; background: #E9ECEF; padding: 15px; border-radius: 8px;">
                        <label>Especialidad (Obligatorio para Docente):</label>
                        <input type="text" id="n-especialidad" class="form-control" placeholder="Ej: Informática">
                    </div>

                    <div style="display: flex; justify-content: flex-end; margin-top: 20px;">
                        <button id="btn-crear-usuario" style="width: 200px; height: 50px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 20px; cursor: pointer;">
                            CREAR USUARIO
                        </button>
                    </div>
                </div>

                <div id="form-existente" style="display: flex; width: 800px; background-color: #F0F8FF; border: 1px solid #000000; border-radius: 8px; padding: 30px; flex-direction: column; gap: 20px;">
                    
                    <div>
                        <label style="font-weight: 700; font-size: 18px; margin-bottom: 8px;">Buscar por Email:</label>
                        <div style="display: flex; gap: 10px;">
                            <input type="email" id="e-search-email" placeholder="ejemplo@test.com" class="form-control">
                            <button id="btn-buscar-usuario" style="width: 120px; background-color: #2C3E50; color: #FFFFFF; font-weight: 700; border: none; border-radius: 8px; cursor: pointer;">BUSCAR</button>
                        </div>
                    </div>

                    <div id="e-editor-container" style="display: none; flex-direction: column; gap: 20px; margin-top: 20px;">
                        <input type="hidden" id="e-id-usuario">
                        
                        <h2 style="font-weight: 700; font-size: 20px; border-bottom: 2px solid #2C3E50; padding-bottom: 10px;">Datos Personales</h2>
                        <div style="display: flex; gap: 20px;">
                            <div style="flex: 1;"><label>Nombre:</label><input type="text" id="e-nombre" class="form-control"></div>
                            <div style="flex: 1;"><label>Apellidos:</label><input type="text" id="e-apellidos" class="form-control"></div>
                        </div>
                        <div style="display: flex; gap: 20px;">
                            <div style="flex: 1;"><label>Teléfono:</label><input type="text" id="e-telefono" class="form-control"></div>
                            <div style="flex: 1;">
                                <label>Estado:</label>
                                <select id="e-activo" class="form-control">
                                    <option value="true">Activo</option>
                                    <option value="false">Desactivado</option>
                                </select>
                            </div>
                        </div>
                        <div><label>Nueva Contraseña (Dejar en blanco para no cambiar):</label><input type="password" id="e-password" class="form-control"></div>

                        <h2 style="font-weight: 700; font-size: 20px; margin-top: 20px; border-bottom: 2px solid #2C3E50; padding-bottom: 10px;">Roles Asignados</h2>
                        <div style="display: flex; flex-wrap: wrap; gap: 15px;" id="e-roles-container">
                            <label><input type="checkbox" value="ROLE_ALUMNO" class="e-rol-check"> Alumno</label>
                            <label><input type="checkbox" value="ROLE_DOCENTE" class="e-rol-check"> Docente</label>
                            <label><input type="checkbox" value="ROLE_TUTOR_LEGAL" class="e-rol-check"> Tutor Legal</label>
                            <label><input type="checkbox" value="ROLE_SECRETARIA" class="e-rol-check"> Secretaría</label>
                            <label><input type="checkbox" value="ROLE_JEFATURA" class="e-rol-check"> Jefatura</label>
                            <label><input type="checkbox" value="ROLE_ADMINISTRADOR" class="e-rol-check"> Administrador</label>
                        </div>

                        <div style="display: flex; justify-content: space-between; margin-top: 20px;">
                            <button id="btn-desactivar-usuario" style="width: 200px; height: 50px; background-color: #EC221F; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 20px; cursor: pointer;">
                                DESACTIVAR 🚫
                            </button>
                            <button id="btn-actualizar-usuario" style="width: 200px; height: 50px; background-color: #04A12E; color: #FFFFFF; font-weight: 700; font-size: 18px; border: none; border-radius: 20px; cursor: pointer;">
                                ACTUALIZAR 💾
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
  },

  init: () => {
    const btnFlujoNuevo = document.getElementById("btn-flujo-nuevo");
    const btnFlujoExistente = document.getElementById("btn-flujo-existente");
    const formNuevo = document.getElementById("form-nuevo");
    const formExistente = document.getElementById("form-existente");

    if (!btnFlujoNuevo) return;

    btnFlujoNuevo.addEventListener("click", () => {
      formNuevo.style.display = "flex";
      formExistente.style.display = "none";
      btnFlujoNuevo.style.backgroundColor = "#2C3E50";
      btnFlujoNuevo.style.color = "#FFFFFF";
      btnFlujoExistente.style.backgroundColor = "#FFFFFF";
      btnFlujoExistente.style.color = "#2C3E50";
    });

    btnFlujoExistente.addEventListener("click", () => {
      formNuevo.style.display = "none";
      formExistente.style.display = "flex";
      btnFlujoExistente.style.backgroundColor = "#2C3E50";
      btnFlujoExistente.style.color = "#FFFFFF";
      btnFlujoNuevo.style.backgroundColor = "#FFFFFF";
      btnFlujoNuevo.style.color = "#2C3E50";
    });

    const checksNuevo = document.querySelectorAll(".n-rol-check");
    const extraAlumno = document.getElementById("n-extra-alumno");
    const extraDocente = document.getElementById("n-extra-docente");

    checksNuevo.forEach((chk) => {
      chk.addEventListener("change", () => {
        if (chk.value === "ROLE_ALUMNO")
          extraAlumno.style.display = chk.checked ? "block" : "none";
        if (chk.value === "ROLE_DOCENTE")
          extraDocente.style.display = chk.checked ? "block" : "none";
      });
    });

    document
      .getElementById("btn-crear-usuario")
      .addEventListener("click", async (e) => {
        const btn = e.target;
        const nombre = document.getElementById("n-nombre").value;
        const apellidos = document.getElementById("n-apellidos").value;
        const email = document.getElementById("n-email").value;
        const telefono = document.getElementById("n-telefono").value;
        const password = document.getElementById("n-password").value;

        const rolesSeleccionados = Array.from(
          document.querySelectorAll(".n-rol-check:checked"),
        ).map((c) => c.value);

        if (
          !nombre ||
          !apellidos ||
          !email ||
          !password ||
          rolesSeleccionados.length === 0
        ) {
          alert(
            "Rellene los campos obligatorios y seleccione al menos un rol.",
          );
          return;
        }

        try {
          btn.disabled = true;
          btn.innerHTML = "CREANDO...";

          const usuario = await fetchAPI("/usuarios/registro", {
            method: "POST",
            body: JSON.stringify({
              nombre,
              apellidos,
              email,
              password,
              telefonoContacto: telefono,
            }),
          });

          for (const rol of rolesSeleccionados) {
            await fetchAPI(`/usuarios/${usuario.idUsuario}/roles/${rol}`, {
              method: "POST",
            });

            try {
              if (rol === "ROLE_ALUMNO") {
                await fetchAPI("/alumnos/asignar", {
                  method: "POST",
                  body: JSON.stringify({
                    idUsuario: usuario.idUsuario,
                    fechaNacimiento:
                      document.getElementById("n-fecha-nac").value,
                  }),
                });
              } else if (rol === "ROLE_DOCENTE") {
                await fetchAPI("/docentes/asignar", {
                  method: "POST",
                  body: JSON.stringify({
                    idUsuario: usuario.idUsuario,
                    especialidad:
                      document.getElementById("n-especialidad").value,
                  }),
                });
              } else if (rol === "ROLE_TUTOR_LEGAL") {
                await fetchAPI("/tutores-legales/asignar", {
                  method: "POST",
                  body: JSON.stringify({ idUsuario: usuario.idUsuario }),
                });
              }
            } catch (e) {
              if (!e.message.includes("JSON")) throw e;
            }
          }

          alert("Usuario creado y configurado con éxito.");
          document.querySelectorAll("#form-nuevo input").forEach((i) => {
            if (i.type !== "checkbox") i.value = "";
            else i.checked = false;
          });
          extraAlumno.style.display = "none";
          extraDocente.style.display = "none";
        } catch (error) {
          alert("Error al crear: " + error.message);
        } finally {
          btn.disabled = false;
          btn.innerHTML = "CREAR USUARIO";
        }
      });

    let rolesOriginales = [];
    document
      .getElementById("btn-buscar-usuario")
      .addEventListener("click", async (e) => {
        const email = document.getElementById("e-search-email").value;
        if (!email) return;

        try {
          e.target.innerHTML = "...";
          const usuario = await fetchAPI(`/usuarios/buscar?email=${email}`);

          document.getElementById("e-id-usuario").value = usuario.idUsuario;
          document.getElementById("e-nombre").value = usuario.nombre;
          document.getElementById("e-apellidos").value = usuario.apellidos;
          document.getElementById("e-telefono").value =
            usuario.telefonoContacto || "";
          document.getElementById("e-activo").value = usuario.activo.toString();
          document.getElementById("e-password").value = "";

          rolesOriginales = usuario.roles;
          document.querySelectorAll(".e-rol-check").forEach((chk) => {
            chk.checked = rolesOriginales.includes(chk.value);
          });

          document.getElementById("e-editor-container").style.display = "flex";
        } catch (error) {
          alert("Usuario no encontrado.");
          document.getElementById("e-editor-container").style.display = "none";
        } finally {
          e.target.innerHTML = "BUSCAR";
        }
      });

    document
      .getElementById("btn-actualizar-usuario")
      .addEventListener("click", async (e) => {
        const btn = e.target;
        const idUsuario = document.getElementById("e-id-usuario").value;
        const password = document.getElementById("e-password").value;

        const bodyUpdate = {
          nombre: document.getElementById("e-nombre").value,
          apellidos: document.getElementById("e-apellidos").value,
          telefonoContacto: document.getElementById("e-telefono").value,
          activo: document.getElementById("e-activo").value === "true",
        };
        if (password) bodyUpdate.nuevaPassword = password;

        try {
          btn.disabled = true;
          btn.innerHTML = "GUARDANDO...";

          await fetchAPI(`/usuarios/${idUsuario}`, {
            method: "PUT",
            body: JSON.stringify(bodyUpdate),
          });

          const rolesActuales = Array.from(
            document.querySelectorAll(".e-rol-check:checked"),
          ).map((c) => c.value);

          const rolesAAñadir = rolesActuales.filter(
            (r) => !rolesOriginales.includes(r),
          );
          const rolesAQuitar = rolesOriginales.filter(
            (r) => !rolesActuales.includes(r),
          );

          for (const rol of rolesAAñadir) {
            await fetchAPI(`/usuarios/${idUsuario}/roles/${rol}`, {
              method: "POST",
            });
            if (rol === "ROLE_ALUMNO")
              await fetchAPI("/alumnos/asignar", {
                method: "POST",
                body: JSON.stringify({
                  idUsuario,
                  fechaNacimiento: "2000-01-01",
                }),
              });
            if (rol === "ROLE_DOCENTE")
              await fetchAPI("/docentes/asignar", {
                method: "POST",
                body: JSON.stringify({ idUsuario, especialidad: "General" }),
              });
            if (rol === "ROLE_TUTOR_LEGAL")
              await fetchAPI("/tutores-legales/asignar", {
                method: "POST",
                body: JSON.stringify({ idUsuario }),
              });
          }

          for (const rol of rolesAQuitar) {
            await fetchAPI(`/usuarios/${idUsuario}/roles/${rol}`, {
              method: "DELETE",
            });
          }

          alert("Usuario actualizado correctamente.");
          rolesOriginales = rolesActuales;
        } catch (error) {
          alert("Error al actualizar: " + error.message);
        } finally {
          btn.disabled = false;
          btn.innerHTML = "ACTUALIZAR 💾";
        }
      });

    document
      .getElementById("btn-desactivar-usuario")
      .addEventListener("click", async () => {
        const idUsuario = document.getElementById("e-id-usuario").value;
        if (
          !confirm(
            "¿Seguro que deseas desactivar este usuario? No podrá iniciar sesión.",
          )
        )
          return;

        try {
          await fetchAPI(`/usuarios/${idUsuario}/desactivar`, {
            method: "PATCH",
          });
          document.getElementById("e-activo").value = "false";
          alert("Usuario desactivado.");
        } catch (error) {
          alert("Error al desactivar: " + error.message);
        }
      });
  },
};
