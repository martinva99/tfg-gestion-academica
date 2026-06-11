const TOKEN_KEY = "jwt_token";
const ROLE_KEY = "rol_activo";
const ROLES_AVAILABLE_KEY = "roles_disponibles";
const NOMBRE_KEY = "user_nombre";
const APELLIDOS_KEY = "user_apellidos";
const ID_KEY = "user_id";

export const auth = {
  setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
  },
  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },

  setRolActivo(rol) {
    localStorage.setItem(ROLE_KEY, rol);
  },
  getRolActivo() {
    return localStorage.getItem(ROLE_KEY);
  },

  setRolesDisponibles(roles) {
    localStorage.setItem(ROLES_AVAILABLE_KEY, JSON.stringify(roles));
  },
  getRolesDisponibles() {
    const roles = localStorage.getItem(ROLES_AVAILABLE_KEY);
    return roles ? JSON.parse(roles) : [];
  },

  setUserInfo(nombre, apellidos, idUsuario) {
    localStorage.setItem(NOMBRE_KEY, nombre);
    localStorage.setItem(APELLIDOS_KEY, apellidos);
    localStorage.setItem(ID_KEY, idUsuario);
  },

  getUserInfo() {
    return {
      nombre: localStorage.getItem(NOMBRE_KEY) || "",
      apellidos: localStorage.getItem(APELLIDOS_KEY) || "",
      idUsuario: localStorage.getItem(ID_KEY) || "",
    };
  },

  logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
    localStorage.removeItem(ROLES_AVAILABLE_KEY);
    localStorage.removeItem(NOMBRE_KEY);
    localStorage.removeItem(APELLIDOS_KEY);
    localStorage.removeItem(ID_KEY);
    window.location.hash = "#/login";
  },

  isAuthenticated() {
    return !!this.getToken();
  },
};
