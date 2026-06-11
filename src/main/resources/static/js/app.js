import { LoginView } from "./views/login.js";
import { SelectorRolView } from "./views/selector-rol.js";
import { auth } from "./utils/auth.js";
import { DashboardView } from "./views/dashboard.js";
import { Layout } from "./components/layout.js";
import { HorarioView } from "./views/horario.js";
import { NotificacionesView } from "./views/notificaciones.js";
import { FaltasView } from "./views/faltas.js";
import { ExpedienteView } from "./views/expediente.js";
import { DisciplinaView } from "./views/disciplina.js";
import { AsistenciaView } from "./views/asistencia.js";
import { CalificacionesView } from "./views/calificaciones.js";
import { EvaluacionesView } from "./views/evaluaciones.js";
import { HorariosJefaturaView } from "./views/horarios-jefatura.js";
import { MatriculasView } from "./views/matriculas.js";
import { UsuariosView } from "./views/usuarios.js";

const routes = {
  "/login": LoginView,
  "/selector-rol": SelectorRolView,
  "/dashboard": DashboardView,
  "/horario": HorarioView,
  "/notificaciones": NotificacionesView,
  "/faltas": FaltasView,
  "/expediente": ExpedienteView,
  "/disciplina": DisciplinaView,
  "/asistencia": AsistenciaView,
  "/calificaciones": CalificacionesView,
  "/evaluaciones": EvaluacionesView,
  "/horarios-jefatura": HorariosJefaturaView,
  "/matriculas": MatriculasView,
  "/usuarios": UsuariosView,
};

const router = async () => {
  let path = window.location.hash.slice(1) || "/";

  const isAuthenticated = auth.isAuthenticated();

  if (path === "/") {
    path = isAuthenticated ? "/selector-rol" : "/login";
    window.location.hash = `#${path}`;
    return;
  }

  if (!isAuthenticated && path !== "/login") {
    window.location.hash = "#/login";
    return;
  }

  if (isAuthenticated && path === "/login") {
    window.location.hash = "#/selector-rol";
    return;
  }

  const view = routes[path];

  const appContainer = document.getElementById("app");

  if (view) {
    if (path === "/login" || path === "/selector-rol") {
      appContainer.innerHTML = view.render ? await view.render() : view;
    } else {
      const viewHtml = await view.render();
      appContainer.innerHTML = await Layout.render(viewHtml, path);

      const btnLogout = document.getElementById("btn-logout");
      if (btnLogout) {
        btnLogout.addEventListener("click", (e) => {
          e.preventDefault();
          auth.logout();
        });
      }
    }

    if (view.init) {
      view.init();
    }
  } else {
    appContainer.innerHTML = `<h2 class="text-center mt-5">Error 404: Página no encontrada</h2>`;
  }
};

window.addEventListener("hashchange", router);

window.addEventListener("load", router);
