import { auth } from "../utils/auth.js";

const API_BASE_URL = "https://gestion-academica-ea82.onrender.com/api";

export async function fetchAPI(endpoint, options = {}) {
  const token = auth.getToken();

  const headers = {
    "Content-Type": "application/json",
    ...options.headers,
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const config = {
    ...options,
    headers,
  };

  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, config);

    if (response.status === 401) {
      if (!endpoint.includes("/auth/login")) {
        auth.logout();
        throw new Error("Sesión expirada. Por favor, vuelve a iniciar sesión.");
      }
    }

    if (response.status === 403) {
      throw new Error("No tienes permisos para realizar esta acción.");
    }

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      const mensajeReal =
        (errorData.messages && errorData.messages[0]) ||
        (errorData.errores && errorData.errores[0]) ||
        errorData.message ||
        `Error HTTP: ${response.status}`;

      throw new Error(mensajeReal);
    }

    if (response.status === 204) {
      return null;
    }

    return await response.json();
  } catch (error) {
    console.error(`Error en la petición a ${endpoint}:`, error);
    throw error;
  }
}
