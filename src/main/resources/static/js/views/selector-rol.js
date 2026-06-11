import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const SelectorRolView = {
  render: () => {
    const roles = auth.getRolesDisponibles();

    const formatRole = (role) => {
      return role.replace("ROLE_", "").replace("_", " ");
    };

    const buttonsHtml = roles
      .map(
        (rol) => `
            <button class="login-btn role-btn" style="margin-bottom: 15px;" data-role="${rol}">
                ENTRAR COMO ${formatRole(rol)}
            </button>
        `,
      )
      .join("");

    return `
            <div class="d-flex justify-content-center align-items-center login-container">
                <div class="login-card">
                    <h1 class="login-title">Seleccione su perfil</h1>
                    <p class="login-subtitle">Tiene múltiples roles asignados</p>
                    
                    <div id="roles-container" class="mt-4">
                        ${buttonsHtml}
                    </div>
                    
                    <div class="text-center mt-3">
                        <a href="#" id="btn-cancelar" style="color: #EC221F; text-decoration: none; font-weight: bold;">Cancelar y salir</a>
                    </div>
                </div>
            </div>
        `;
  },

  init: () => {
    const container = document.getElementById("roles-container");
    const btnCancelar = document.getElementById("btn-cancelar");

    btnCancelar.addEventListener("click", (e) => {
      e.preventDefault();
      auth.logout();
    });

    container.addEventListener("click", async (e) => {
      if (e.target.classList.contains("role-btn")) {
        const selectedRole = e.target.getAttribute("data-role");

        try {
          const response = await fetchAPI("/auth/select-profile", {
            method: "POST",
            body: JSON.stringify({ rolActivo: selectedRole }),
          });

          if (response && response.token) {
            auth.setToken(response.token);
            auth.setRolActivo(selectedRole);
            auth.setUserInfo(
              response.nombre,
              response.apellidos,
              response.idUsuario,
            );

            window.location.hash = "#/dashboard";
          }
        } catch (error) {
          alert("Error al seleccionar perfil: " + error.message);
        }
      }
    });
  },
};
