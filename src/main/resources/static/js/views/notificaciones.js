import { fetchAPI } from "../api/apiClient.js";
import { auth } from "../utils/auth.js";

export const NotificacionesView = {
  render: async () => {
    const userInfo = auth.getUserInfo();
    let notificaciones = [];

    try {
      notificaciones = await fetchAPI(
        `/notificaciones/usuario/${userInfo.idUsuario}`,
      );

      notificaciones.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));
      notificaciones = notificaciones.slice(0, 20);
    } catch (error) {
      return `<div class="alert alert-danger text-center">Error al cargar notificaciones: ${error.message}</div>`;
    }

    const notificacionesHtml = notificaciones
      .map((notif) => {
        const isLeida = notif.leida;
        const bgColor = isLeida ? "#FFFFFF" : "#F0F8FF";
        const fontWeight = isLeida ? "400" : "700";
        const circuloHtml = isLeida
          ? ""
          : `<div class="notif-circle" style="width: 12px; height: 12px; background-color: #2C3E50; border-radius: 50%; margin-right: 15px; flex-shrink: 0;"></div>`;

        const fechaObj = new Date(notif.fecha);
        const fechaStr =
          fechaObj.toLocaleDateString("es-ES") +
          " " +
          fechaObj.toLocaleTimeString("es-ES", {
            hour: "2-digit",
            minute: "2-digit",
          });

        return `
                <div class="notificacion-card" data-id="${notif.idNotificacion}" data-leida="${isLeida}" 
                     style="width: 100%; max-width: 950px; height: 90px; background-color: ${bgColor}; border: 1px solid #000000; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center; padding: 0 20px; cursor: pointer; transition: background-color 0.2s;">
                    
                    <div style="display: flex; align-items: center; overflow: hidden; padding-right: 20px;">
                        ${circuloHtml}
                        <span class="notif-text" style="font-weight: ${fontWeight}; font-size: 20px; color: #000000; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                            ${notif.mensaje}
                        </span>
                    </div>
                    
                    <div style="font-weight: 400; font-size: 14px; color: #000000; flex-shrink: 0;">
                        ${fechaStr}
                    </div>
                </div>
            `;
      })
      .join("");

    return `
            <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
                
                <div style="width: 100%; max-width: 950px; position: relative; margin-bottom: 40px; min-height: 43px;">
                    <h1 style="font-weight: 700; font-size: 24px; color: #000000; text-align: center; margin: 0;">
                        Mis notificaciones
                    </h1>
                    
                    <button id="btn-marcar-todas" style="position: absolute; right: 0; top: 0; width: 320px; height: 43px; background-color: #F8F9FA; border: 0.5px solid #000000; border-radius: 10px; font-weight: 400; font-size: 20px; color: #000000; cursor: pointer; display: flex; justify-content: center; align-items: center;">
                        Marcar todas como leídas
                        <span style="margin-left: 10px;">✔️</span>
                    </button>
                </div>

                <div id="lista-notificaciones" style="width: 100%; display: flex; flex-direction: column; align-items: center;">
                    ${notificaciones.length > 0 ? notificacionesHtml : '<p style="font-size: 20px;">No tienes notificaciones.</p>'}
                </div>
            </div>
        `;
  },

  init: () => {
    const userInfo = auth.getUserInfo();

    const btnMarcarTodas = document.getElementById("btn-marcar-todas");
    if (btnMarcarTodas) {
      btnMarcarTodas.addEventListener("click", async () => {
        try {
          await fetchAPI(
            `/notificaciones/usuario/${userInfo.idUsuario}/leidas`,
            { method: "PATCH" },
          );
          window.dispatchEvent(new Event("hashchange"));
        } catch (error) {
          alert("Error al marcar todas como leídas: " + error.message);
        }
      });
    }

    const lista = document.getElementById("lista-notificaciones");
    if (lista) {
      lista.addEventListener("click", async (e) => {
        const card = e.target.closest(".notificacion-card");
        if (!card) return;

        const idNotif = card.getAttribute("data-id");
        const isLeida = card.getAttribute("data-leida") === "true";

        if (isLeida) return;

        try {
          await fetchAPI(`/notificaciones/${idNotif}/leida`, {
            method: "PATCH",
          });

          card.setAttribute("data-leida", "true");
          card.style.backgroundColor = "#FFFFFF";

          const circulo = card.querySelector(".notif-circle");
          if (circulo) circulo.remove();

          const texto = card.querySelector(".notif-text");
          if (texto) texto.style.fontWeight = "400";
        } catch (error) {
          alert("Error al marcar como leída: " + error.message);
        }
      });
    }
  },
};
