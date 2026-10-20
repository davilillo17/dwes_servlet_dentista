# Proyecto Clínica Dental (Versión Servlets / MVC)

Gestión de citas de pacientes de una clínica dental, refactorizada de un modelo basado en JSP + scriptlets a una arquitectura **MVC (Modelo-Vista-Controlador)** utilizando **Servlets**, **JavaBeans** y **JDBC** sobre MySQL.

## Estructura del Proyecto

```
dwes_servlet_dentista/
├── pom.xml
└── src/main/
├── java/
│   └── com/dentista/
│       ├── config/
│       │   └── Conexion.java         <- Configuración centralizada de la base de datos
│       ├── controller/
│       │   ├── CitaServlet.java        <- Controlador para gestión de citas
│       │   └── PacienteServlet.java    <- Controlador para gestión de pacientes
│       ├── dao/
│       │   ├── CitaDAO.java            <- Consultas SQL y lógica de citas
│       │   ├── OdontologoDAO.java      <- Consultas SQL de odontólogos
│       │   └── PacienteDAO.java        <- Consultas SQL y lógica de pacientes
│       └── model/
│           ├── Cita.java               <- Entidad Cita
│           ├── Odontologo.java         <- Entidad Odontólogo
│           └── Paciente.java           <- Entidad Paciente
├── resources/
│   └── dentista.sql                    <- Script de creación de la base de datos
└── webapp/
├── WEB-INF/
│   ├── formularioCita.jsp          <- Formulario de alta de cita
│   ├── formularioPaciente.jsp      <- Formulario de alta de paciente
│   ├── listadoCitas.jsp            <- Listado de citas y/o histórico
│   └── listadoPacientes.jsp        <- Listado de pacientes
├── index.jsp                       <- Menú principal público
└── estilos.css
```

> El proyecto ha sido refactorizado para separar responsabilidades (MVC): **toda la lógica de conexión y consultas SQL se ha movido a clases DAO**, la gestión de peticiones a **Servlets**, y las **vistas JSP se han ocultado dentro de `WEB-INF/`**, eliminando por completo los scriptlets y utilizando **JSTL** y **Expression Language (EL)**.

## Base de datos

Base de datos `dentista` con tres tablas:

| Tabla         | Campo clave     | Contenido                                                          |
|---------------|-----------------|--------------------------------------------------------------------|
| `paciente`    | `id_paciente`   | nombre, apellidos, dni, fecha_nacimiento, telefono, email, direccion, alergias |
| `odontologos` | `id_odontologo` | nombre, especialidad                                                  |
| `cita`        | `id_cita`       | fecha, hora, `id_paciente` (FK), `id_odontologo` (FK), motivo        |

Relaciones:

- `cita.id_paciente` → `paciente.id_paciente`, con `ON DELETE CASCADE`: al borrar un paciente se borran también sus citas.
- `cita.id_odontologo` → `odontologos.id_odontologo`: la cita guarda la clave del odontólogo, no su nombre, para que los profesionales se mantengan en un único sitio.

## Prerrequisitos

1. Importar la base de datos `src/main/resources/dentista.sql`:

   ```bash
   mysql -u user -puser < src/main/resources/dentista.sql
   ```

   Las citas de ejemplo se crean con `DATE_ADD(CURDATE(), INTERVAL n DAY)`, por lo que
   siempre habrá citas próximas y pasadas sea cual sea el día de importación.

2. Disponer de MySQL Server en `localhost:3306` con el usuario `user` / contraseña `user`
   (si son distintos, modificarlos en Conexion.java).

3. Java 21 y Maven 3.8+.

## Plataforma: Tomcat 9 (Java EE 8)

La aplicación está preparada para **Tomcat 9**, que implementa Java EE 8 y por tanto usa
el *namespace* `javax.*` (no `jakarta.*`):

| Especificación | Versión | Artefacto Maven                 |
|----------------|---------|---------------------------------|
| Servlet        | 4.0     | `javax.servlet:javax.servlet-api:4.0.1` |
| JSP            | 2.3     | incluido en el contenedor        |
| Expression Lang| 3.0     | incluido en el contenedor        |

El contenedor que corresponde es **Tomcat 9.0.x** (no 10.x, que ya es Jakarta EE 9 con
`jakarta.*`).

```bash
mvn clean package
```

Genera `target/scriplet_dentista.war`, que se despliega en Tomcat 9.0.x.
El contexto de la aplicación es `/scriplet_dentista`.

## Notas de implementación (Arquitectura Servlets / MVC)

- La página de inicio pública es `index.jsp`, cuyos enlaces apuntan directamente a las rutas mapeadas de los **Servlets** (`/pacientes`, `/citas`, `/cita-form`, `/paciente-form`).
- Las vistas JSP están protegidas dentro de la carpeta `WEB-INF/` y se accede a ellas exclusivamente mediante `RequestDispatcher.forward()` desde los Servlets.
- Las conexiones a la base de datos están centralizadas en `Conexion.java` mediante JDBC.
- Los accesos a datos utilizan clases **DAO** (`PacienteDAO`, `CitaDAO`, `OdontologoDAO`) con `PreparedStatement` para evitar inyecciones SQL.
- El guardado y borrado de pacientes y citas se procesa mediante peticiones `POST` a los Servlets correspondientes (`/paciente-guardar`, `/paciente-borrar`, `/cita-guardar`, `/cita-borrar`).
