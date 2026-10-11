# API Biblioteca

API para la gestión de una biblioteca que permite administrar libros, personas y préstamos. El proyecto combina una API REST con una interfaz web desarrollada con Thymeleaf para gestionar los registros de forma visual.

## Descripción

La aplicación permite:

- Registrar, consultar, actualizar y eliminar libros.
- Registrar, consultar, actualizar y eliminar personas.
- Registrar préstamos y devoluciones.
- Consultar préstamos activos.
- Gestionar la interfaz web desde páginas HTML con Thymeleaf.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- MySQL
- Thymeleaf
- Maven

## Requisitos previos

- JDK 21 o superior
- Maven 3.9+
- MySQL 8.x
- Git

## Instalación

1. Clona el repositorio:

```bash
git clone https://github.com/mafercova/api1_biblioteca.git
cd api1_biblioteca
```

2. Crea la base de datos en MySQL:

```sql
CREATE DATABASE libreria;
```

3. Configura las variables de entorno antes de arrancar la aplicación:

```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/libreria?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
export SPRING_DATASOURCE_USERNAME='tu_usuario'
export SPRING_DATASOURCE_PASSWORD='tu_contraseña'
export SERVER_PORT='1500'
```

En Windows PowerShell:

```powershell
$env:SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/libreria?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
$env:SPRING_DATASOURCE_USERNAME='tu_usuario'
$env:SPRING_DATASOURCE_PASSWORD='tu_contraseña'
$env:SERVER_PORT='1500'
```

4. Compila el proyecto:

```bash
./mvnw clean install
```

5. Ejecuta la API:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
mvnw.cmd spring-boot:run
```

La aplicación quedará disponible en:

- API: http://localhost:1500
- Interfaz web: http://localhost:1500/vista/libros

## Variables de entorno necesarias

Se recomienda no guardar credenciales reales en el repositorio. Define estas variables en el entorno de ejecución:

```bash
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SERVER_PORT
```

Ejemplo:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/libreria?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=tu_password_segura
SERVER_PORT=1500
```

## Estructura del proyecto

```text
api1_biblioteca/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/api/libreria/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   └── test/
├── .gitignore
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── .gitattributes
```

## Endpoints de la API

### Libros

- `GET /libros` — Obtiene todos los libros.
- `POST /libros` — Crea un libro.
- `GET /libros/{isbn}` — Busca un libro por ISBN.
- `PUT /libros/{isbn}` — Actualiza un libro.
- `DELETE /libros/{isbn}` — Elimina un libro.
- `GET /libros/autor/{autor}` — Busca libros por autor.

### Personas

- `GET /personas` — Obtiene todas las personas.
- `POST /personas` — Crea una persona.
- `GET /personas/{id}` — Busca una persona por ID.
- `PUT /personas/{id}` — Actualiza una persona.
- `DELETE /personas/{id}` — Elimina una persona.
- `GET /personas/nombre/{nombre}` — Busca personas por nombre.

### Préstamos

- `GET /prestamos` — Obtiene todos los préstamos.
- `GET /prestamos/activos` — Obtiene préstamos activos.
- `POST /prestamos/{id}/devolver` — Devuelve un préstamo.
- `PUT /prestamos/{id}?isbn={isbn}&personaId={personaId}` — Actualiza un préstamo.
- `DELETE /prestamos/{id}` — Elimina un préstamo.

### Vistas web

- `GET /vista/libros`
- `GET /vista/personas`
- `GET /vista/prestamos`

## Ejemplos de peticiones y respuestas

### 1) Crear un libro

Petición:

```bash
curl -X POST http://localhost:1500/libros \
  -H 'Content-Type: application/json' \
  -d '{
    "titulo": "Clean Code",
    "autor": "Robert C. Martin",
    "editorial": "Pearson",
    "añoPublicacion": "2008",
    "numeroEjemplares": 5
  }'
```

Respuesta esperada:

```json
{
  "isbn": 1,
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "editorial": "Pearson",
  "añoPublicacion": "2008",
  "numeroEjemplares": 5
}
```

### 2) Obtener todos los libros

```bash
curl http://localhost:1500/libros
```

Respuesta esperada:

```json
[
  {
    "isbn": 1,
    "titulo": "Clean Code",
    "autor": "Robert C. Martin",
    "editorial": "Pearson",
    "añoPublicacion": "2008",
    "numeroEjemplares": 5
  }
]
```

### 3) Crear una persona

```bash
curl -X POST http://localhost:1500/personas \
  -H 'Content-Type: application/json' \
  -d '{
    "nombre": "Ana",
    "apellidos": "García",
    "contrasena": "miPassword123",
    "telefono": 987654321,
    "correoElectronico": "ana@correo.com",
    "direccion": "Calle Mayor 10",
    "sexo": "Femenino",
    "fechaNacimiento": "1995-05-10",
    "estado": "Activo",
    "rol": "Usuario"
  }'
```

Respuesta esperada:

```json
{
  "id": 1,
  "nombre": "Ana",
  "apellidos": "García",
  "contrasena": "miPassword123",
  "telefono": 987654321,
  "correoElectronico": "ana@correo.com",
  "direccion": "Calle Mayor 10",
  "sexo": "Femenino",
  "fechaNacimiento": "1995-05-10",
  "estado": "Activo",
  "rol": "Usuario"
}
```

### 4) Registrar un préstamo

```bash
curl -X POST "http://localhost:1500/prestamos/1/devolver"
```

> La lógica de préstamo se gestiona desde la vista web o desde la capa de servicio, validando que el usuario esté activo y que exista disponibilidad del libro.

## Consideraciones importantes

- No se deben subir credenciales reales, contraseñas, tokens, archivos `.env`, certificados ni secretos.
- No se deben subir archivos generados como `target/`, carpetas de IDE (`.idea`, `.vscode`) ni archivos temporales.
- Las credenciales deben configurarse mediante variables de entorno del sistema o un archivo local no versionado.

## Licencia

Este proyecto se entrega como ejercicio académico/educativo y no incluye una licencia definida por el repositorio.
