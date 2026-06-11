# 🎓 Aplicación de Gestión Educativa (API REST)

Un sistema de gestión académica diseñado bajo una arquitectura **Cliente-Servidor**. Este repositorio contiene el **Backend (API REST)**, desarrollado para centralizar la administración de usuarios, estructuración de horarios complejos, control de asistencia y seguimiento del rendimiento escolar en centros educativos.

## 🌐 Despliegue en Vivo (Live Demo)

La aplicación se encuentra desplegada en la nube y es completamente funcional. Puedes probarla aquí:

👉 **[Acceder a la aplicación en Render](https://gestion-academica-ea82.onrender.com/)**

## 🚀 Características Principales (Backend Highlights)

* **Autenticación y Autorización Compleja (RBAC):** Sistema de roles dinámico (Admin, Jefatura, Secretaría, Docente, Alumno, Tutor Legal). Implementación de **JWT (JSON Web Tokens)** con soporte para usuarios multi-rol.
* **Arquitectura Multicapa (N-Tier):** Separación estricta de responsabilidades (`Controllers`, `Services`, `Repositories`).
* **Patrón DTO y Mappers:** Aislamiento total de las entidades de base de datos. La API solo expone y recibe Data Transfer Objects (DTOs).
* **Integridad Transaccional:** Reglas de negocio estrictas gestionadas en la capa de servicios (ej. bloqueo de inserción de calificaciones si el acta de evaluación está en estado `CERRADA`).
* **Automatización de Eventos:** Triggers lógicos en el backend para el cálculo automático de porcentajes de ausencias y generación de notificaciones pasivas ante eventos críticos (superación de umbrales de faltas, nuevos partes disciplinarios, publicación de notas).

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 21
* **Framework Core:** Spring Boot
* **Seguridad:** Spring Security, JWT, BCrypt (Hashing de contraseñas)
* **Persistencia:** Spring Data JPA, Hibernate ORM
* **Base de Datos:** MySQL
* **Gestor de Dependencias:** Maven
* **Testing:** JUnit 5 (Pruebas unitarias de lógica de negocio)

## 🗄️ Diseño de Base de Datos (Modelado Relacional)

El sistema cuenta con un modelo relacional altamente normalizado para evitar redundancias y gestionar relaciones complejas:

* **Núcleo de Horarios (`SesionHoraria`):** Entidad central que resuelve la confluencia de múltiples relaciones `1:N` (Docente, Grupo, Asignatura, Franja Horaria). Permite validar conflictos de disponibilidad.
* **Núcleo Académico (`Matricula`):** Mantiene el histórico del alumno, cruzando Alumno, Año Académico, Asignatura y Grupo.

## ⚙️ Estructura del Proyecto

src/main/java/com/ieslasencinas/gestionacademica/

├── controller/   # Endpoints de la API REST

├── dto/          # Objetos de transferencia de datos

├── entity/       # Entidades JPA

├── exception/    # Gestión centralizada de errores y excepciones personalizadas

├── mapper/       # Lógica de conversión entre Entities y DTOs

├── repository/   # Interfaces de Spring Data JPA

├── security/     # Filtros JWT y configuración de Spring Security

└── service/      # Lógica de negocio y reglas de validación

## 🔒 Seguridad y Endpoints (Ejemplo)

Todas las rutas (excepto el login) están protegidas por Spring Security. Se requiere enviar el token JWT en el header `Authorization: Bearer <token>`.

| Método | Endpoint | Descripción | Rol Requerido |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Autenticación y generación de JWT | *Cualquier rol* |
| `GET` | `/api/docentes/horario` | Obtiene horario de un docente | `ROLE_DOCENTE` |
| `POST` | `/api/faltas` | Registra una falta de asistencia | `ROLE_DOCENTE` |
| `GET` | `/api/alumnos/expediente` | Historial académico del alumno | `ROLE_ALUMNO` |

## 👨‍💻 Autores

* **Gabriel Veiga Álvarez**
* **Martín Veiga Álvarez**

---
*Proyecto desarrollado como Trabajo Final del Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM).*
