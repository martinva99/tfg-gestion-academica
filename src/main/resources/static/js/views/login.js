import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const LoginView = {
  render: () => {
    return `
            <div class="d-flex justify-content-center align-items-center login-container">
                <div class="login-card">
                    <h1 class="login-title">Sistema de gestión académica</h1>
                    <p class="login-subtitle">Inicie sesión para continuar</p>
                    
                    <form id="login-form">
                        <label class="login-label" for="email">Correo electrónico:</label>
                        <input type="email" id="email" class="login-input" required autocomplete="email">
                        
                        <label class="login-label" for="password">Contraseña:</label>
                        <input type="password" id="password" class="login-input" required autocomplete="current-password">
                        
                        <!-- Mensaje de error oculto por defecto -->
                        <div id="error-msg" class="error-message">Correo electrónico y/o contraseña incorrectos</div>
                        
                        <button type="submit" class="login-btn">INICIAR SESIÓN</button>
                    </form>
                </div>
            </div>
        `;
  },

  init: () => {
    const form = document.getElementById("login-form");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const errorMsg = document.getElementById("error-msg");

    const clearErrors = () => {
      emailInput.classList.remove("error");
      passwordInput.classList.remove("error");
      errorMsg.style.display = "none";
    };

    emailInput.addEventListener("input", clearErrors);
    passwordInput.addEventListener("input", clearErrors);

    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      const email = emailInput.value.trim();
      const password = passwordInput.value;

      try {
        const response = await fetchAPI("/auth/login", {
          method: "POST",
          body: JSON.stringify({ email, password }),
        });

        console.log("Respuesta del backend:", response);

        if (response && response.token) {
          auth.setToken(response.token);
          auth.setRolesDisponibles(response.roles);
          auth.setUserInfo(
            response.nombre,
            response.apellidos,
            response.idUsuario,
          );

          window.location.hash = "#/selector-rol";
        }
      } catch (error) {
        emailInput.classList.add("error");
        passwordInput.classList.add("error");
        errorMsg.style.display = "block";

        if (error.message !== "Error HTTP: 401") {
          errorMsg.textContent = error.message;
        } else {
          errorMsg.textContent =
            "Correo electrónico y/o contraseña incorrectos";
        }
      }
    });
  },
};
