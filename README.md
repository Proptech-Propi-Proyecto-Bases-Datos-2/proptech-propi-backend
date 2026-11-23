# Proptech Propi – Backend

Backend del **Sistema de Facturación de Proptech Propi**, desarrollado como proyecto del curso **IF0010 Bases de Datos II** de la Universidad de Costa Rica, Recinto de Paraíso (II ciclo 2026).

Es una API REST hecha con **Spring Boot** que se conecta a una base de datos **SQL Server**. La lógica principal vive en la base de datos (procedimientos almacenados, triggers, cifrado y roles) y el backend la expone a la aplicación por medio de endpoints HTTP.

**Autores:** Luis Sibaja y Nelson Otárola

---

## Tabla de contenidos

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Base de datos](#base-de-datos)
- [Requisitos previos](#requisitos-previos)
- [Configuración](#configuración)
- [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)
- [Endpoints de la API](#endpoints-de-la-api)
- [Pruebas con archivos .http](#pruebas-con-archivos-http)
- [Manejo de errores](#manejo-de-errores)

---

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje del backend |
| Spring Boot 4.1.1 | Framework principal |
| Spring Web MVC | Creación de la API REST |
| Spring Data JPA / Hibernate | Acceso a datos y mapeo de entidades |
| SQL Server | Motor de base de datos |
| Microsoft JDBC Driver (mssql-jdbc) | Conexión con SQL Server |
| Maven | Manejo de dependencias y compilación |
| Spring Boot DevTools | Reinicio automático en desarrollo |

---

## Arquitectura

El proyecto está organizado por capas:

```
Cliente (app / REST Client)
        │
        ▼
 Controller  →  recibe las peticiones HTTP y devuelve las respuestas
        │
        ▼
  Service    →  valida los datos y aplica las reglas del negocio
        │
        ▼
 Repository  →  consulta la base de datos con JPA o llama a los procedimientos almacenados
        │
        ▼
 SQL Server  →  tablas, procedimientos, triggers, cifrado y roles
```

- **Controller:** expone los endpoints de cada módulo.
- **Service:** contiene la lógica y las validaciones antes de enviar los datos a la base.
- **Repository:** interfaces de Spring Data JPA. Algunas operaciones usan consultas nativas para ejecutar los procedimientos almacenados de la base.
- **Entity:** clases que representan las tablas del esquema `proptech`.
- **DTO:** objetos para transportar datos, por ejemplo los datos del cliente ya descifrados.
- **Exception:** excepciones propias del sistema que se manejan de forma centralizada.

---

## Estructura del proyecto

```
proptech-propi-backend
├── pom.xml
├── src
│   ├── main
│   │   ├── java/cr/ac/ucr/paraiso/proptech/propi/backend
│   │   │   ├── ProptechPropiBackendApplication.java
│   │   │   ├── controller
│   │   │   │   ├── AuthRestController.java
│   │   │   │   ├── ClientesRestController.java
│   │   │   │   ├── FacturasRestController.java
│   │   │   │   ├── FacturasDetalleRestController.java
│   │   │   │   ├── NotasCreditoDebitoRestController.java
│   │   │   │   ├── ProductosServiciosRestController.java
│   │   │   │   ├── TiposImpuestoRestController.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── dto
│   │   │   │   └── ClienteDTO.java
│   │   │   ├── entity
│   │   │   │   ├── Clientes.java
│   │   │   │   ├── Facturas.java
│   │   │   │   ├── FacturasDetalle.java
│   │   │   │   ├── NotasCreditoDebito.java
│   │   │   │   ├── ProductosServicios.java
│   │   │   │   └── TiposImpuesto.java
│   │   │   ├── exception
│   │   │   │   ├── BusinessException.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository
│   │   │   │   ├── ClientesRepository.java
│   │   │   │   ├── FacturasRepository.java
│   │   │   │   ├── FacturasDetalleRepository.java
│   │   │   │   ├── NotasCreditoDebitoRepository.java
│   │   │   │   ├── ProductosServiciosRepository.java
│   │   │   │   └── TiposImpuestoRepository.java
│   │   │   └── service
│   │   │       ├── AuthService.java
│   │   │       ├── ClientesService.java
│   │   │       ├── FacturasService.java
│   │   │       ├── FacturasDetalleService.java
│   │   │       ├── NotasCreditoDebitoService.java
│   │   │       ├── ProductosServiciosService.java
│   │   │       ├── TiposImpuestoService.java
│   │   │       └── ValidationUtils.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── http        → archivos .http para probar cada controlador
└── mvnw / mvnw.cmd     → Maven Wrapper
```

---

## Base de datos

- **Motor:** SQL Server
- **Base de datos:** `DB_IF0010_2026_Propi`
- **Esquema:** `proptech`

### Tablas

| Tabla | Descripción |
|---|---|
| `Clientes` | Información de los clientes. La identificación, el correo y el teléfono se guardan cifrados. |
| `Facturas` | Encabezado de cada factura (cliente, fecha, condición de venta, moneda y montos). |
| `FacturasDetalle` | Líneas de cada factura (producto o servicio, cantidad y monto). |
| `NotasCreditoDebito` | Notas de crédito o débito que modifican una factura. |
| `TiposImpuesto` | Catálogo de impuestos (tarifa y porcentaje). |
| `ProductosServicios` | Catálogo de productos y servicios con su código CABYS de 13 dígitos. |
| `BitacoraAcceso` | Registro de cada ingreso y salida de los usuarios a la aplicación. |
| `BitacoraAuditoria` | Registro de los cambios (insertar, modificar, borrar) hechos en las tablas. |

### Relaciones

- Un **Cliente** tiene muchas **Facturas**.
- Una **Factura** tiene muchos **Detalles** y puede tener muchas **Notas de crédito/débito**.
- Un **Producto/Servicio** aparece en muchos **Detalles** de factura.
- Un **Tipo de impuesto** se aplica a muchos **Productos/Servicios**.

### Seguridad y otras características

- **Cifrado de columnas (Cell-Level Encryption):** los datos sensibles de los clientes se cifran con una llave simétrica (`LlaveSimetricaClientes`) protegida por un certificado (`CertificadoClientes`).
- **Login con seguridad de SQL Server:** el inicio de sesión se hace con logins y usuarios reales de SQL Server.
- **Roles:**

| Rol | Permisos |
|---|---|
| `Administrador` | Acceso completo a toda la información. |
| `Digitador` | Registro de clientes, facturas y notas de crédito/débito. |
| `Consulta` | Solo lectura, para reportes. |

- **Procedimientos almacenados:** CRUD de clientes, tipos de impuesto y productos/servicios; registro de facturas, detalles y notas; bitácora de accesos; y reportes (`ReporteClientesResumido`, `ReporteFacturas`, `ReportePistasAuditoria`, `ReporteConfiguracionSeguridad`).
- **Triggers de auditoría:** cada tabla principal tiene triggers de INSERT, UPDATE y DELETE que guardan los cambios en `BitacoraAuditoria`.
- **Borrado lógico:** clientes, tipos de impuesto y productos no se eliminan, sino que cambian su `estado` a `I` (inactivo).
- **Nivel de aislamiento:** `READ COMMITTED` con `READ_COMMITTED_SNAPSHOT` activado.

---

## Requisitos previos

- **JDK 21**
- **SQL Server** (2019 o superior) escuchando en el puerto `1433`
- **Maven** (opcional, porque el proyecto incluye Maven Wrapper)
- **VS Code** con la extensión **REST Client** (opcional, para usar los archivos `.http`)
- Haber ejecutado el **script SQL del proyecto**, que crea la base `DB_IF0010_2026_Propi` con sus tablas, procedimientos, triggers, llaves de cifrado, roles y logins.

---

## Configuración

La configuración está en `src/main/resources/application.properties`:

```properties
spring.application.name=proptech-propi

spring.datasource.url=<url propia>
spring.datasource.username=<login>
spring.datasource.password=<contraseña del login>
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.servlet.context-path=/proptech-propi
server.port=8084
```

| Propiedad | Descripción |
|---|---|
| `spring.datasource.url` | Dirección del servidor SQL Server y nombre de la base. |
| `spring.datasource.username` / `password` | Login de SQL Server que usa el backend para las consultas generales. |
| `physical-strategy` | Respeta los nombres exactos de las tablas y columnas definidos en `@Table` y `@Column`. |
| `show-sql` | Muestra en consola las sentencias SQL que ejecuta Hibernate. |
| `context-path` y `port` | La API queda disponible en `http://localhost:8084/proptech-propi`. |

> Si tu SQL Server usa otro puerto, otra instancia u otras credenciales, cambiá esos valores antes de ejecutar.

---

## Cómo ejecutar el proyecto

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/Proptech-Propi-Proyecto-Bases-Datos-2/proptech-propi-backend.git
   cd proptech-propi-backend
   ```

2. Ejecutar el script SQL del proyecto en SQL Server.

3. Revisar las credenciales en `application.properties`.

4. Levantar el backend:

   **Windows**
   ```bash
   mvnw.cmd spring-boot:run
   ```

   **Linux / macOS**
   ```bash
   ./mvnw spring-boot:run
   ```

   En VS Code también se puede ejecutar `ProptechPropiBackendApplication.java` con el botón **Run**.

5. La API queda disponible en:

   ```
   http://localhost:8084/proptech-propi
   ```

---

## Endpoints de la API

URL base: `http://localhost:8084/proptech-propi`

### Autenticación

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Inicia sesión con un login de SQL Server (`login_admin`, `login_digitador` o `login_consulta`). |

### Clientes

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/clientes` | Lista los clientes con los datos sensibles descifrados. Permite filtrar por estado (`A` o `I`). |
| GET | `/api/clientes/cifrados` | Lista los clientes con los datos tal como están guardados (cifrados). |
| GET | `/api/clientes/id/{id}` | Obtiene un cliente descifrado por su id. |
| POST | `/api/clientes/insertarCliente` | Inserta un cliente con el procedimiento `InsertarCliente`, que cifra los datos sensibles. |
| PUT | `/api/clientes/id/{id}` | Actualiza los datos de un cliente. |
| DELETE | `/api/clientes/id/{id}` | Inactiva el cliente (borrado lógico). |

```json
{
  "nombre": "Cliente de prueba",
  "numIdentificacion": "123456789",
  "tipoIdentificacion": "Cédula",
  "provincia": "Cartago",
  "canton": "Paraiso",
  "distrito": "Paraiso",
  "barrio": "Centro",
  "direccionExacta": "100 metros al norte del parque central",
  "profesion": "Profesional",
  "actividadEconomica": "Servicios",
  "correoElectronico": "cliente@correo.com",
  "telefono": "8888-8888"
}
```

### Tipos de impuesto

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/tipos-impuestos` | Lista los tipos de impuesto. |
| GET | `/api/tipos-impuestos/{id}` | Obtiene un tipo de impuesto por id. |
| POST | `/api/tipos-impuestos` | Crea un tipo de impuesto. |
| POST | `/api/tipos-impuestos/insertarTipo` | Crea un tipo de impuesto con el procedimiento almacenado. |
| PUT | `/api/tipos-impuestos/{id}` | Actualiza un tipo de impuesto. |
| DELETE | `/api/tipos-impuestos/{id}` | Inactiva el tipo de impuesto (borrado lógico). |

```json
{
  "tarifa": "08",
  "descripcion": "Tarifa general 13%",
  "porcentaje": 13.00,
  "estado": "A"
}
```

### Productos y servicios

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/productos-servicios` | Lista los productos y servicios. |
| GET | `/api/productos-servicios/{id}` | Obtiene un producto o servicio por id. |
| POST | `/api/productos-servicios` | Crea un producto o servicio. |
| POST | `/api/productos-servicios/insertarProducto` | Crea un producto o servicio con el procedimiento almacenado. |
| PUT | `/api/productos-servicios/{id}` | Actualiza un producto o servicio. |
| DELETE | `/api/productos-servicios/{id}` | Inactiva el producto o servicio (borrado lógico). |

```json
{
  "codCabys": "1234567890123",
  "tipoImpuesto": { "idImpuesto": 1 },
  "descripcion": "Servicio de prueba",
  "unidadMedida": "Unidad",
  "precio": 10000.00,
  "estado": "A"
}
```

### Facturas

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/facturas` | Lista las facturas. |
| GET | `/api/facturas/{id}` | Obtiene una factura por id. |
| POST | `/api/facturas` | Crea una factura. |
| PUT | `/api/facturas/{id}` | Actualiza una factura. |
| DELETE | `/api/facturas/{id}` | Elimina una factura. |

```json
{
  "cliente": { "idCliente": 1 },
  "fechaFactura": "2026-10-26T19:00:00",
  "condicionVenta": "CONTADO",
  "medioPago": "EFECTIVO",
  "moneda": "CRC",
  "tipoCambio": 1.0000,
  "observaciones": "Factura de prueba",
  "subtotal": 10000.00,
  "totalImpuestos": 1300.00,
  "montoTotal": 11300.00
}
```

### Detalle de facturas

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/facturas-detalle` | Lista los detalles de factura. |
| GET | `/api/facturas-detalle/{id}` | Obtiene un detalle por id. |
| POST | `/api/facturas-detalle` | Crea una línea de detalle. |
| PUT | `/api/facturas-detalle/{id}` | Actualiza una línea de detalle. |
| DELETE | `/api/facturas-detalle/{id}` | Elimina una línea de detalle. |

```json
{
  "factura": { "idFactura": 1 },
  "productoServicio": { "idProducto": 1 },
  "cantidad": 2,
  "montoLinea": 22600.00
}
```

### Notas de crédito y débito

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/notas-credito-debito` | Lista las notas. |
| GET | `/api/notas-credito-debito/{id}` | Obtiene una nota por id. |
| POST | `/api/notas-credito-debito` | Crea una nota. |
| POST | `/api/notas-credito-debito/insertarNota` | Crea una nota con el procedimiento `CrearNota`. |
| PUT | `/api/notas-credito-debito/{id}` | Actualiza una nota. |
| DELETE | `/api/notas-credito-debito/{id}` | Elimina una nota. |

```json
{
  "factura": { "idFactura": 1 },
  "fecha": "2026-10-26T19:00:00",
  "tipo": "Credito",
  "motivo": "Devolución de producto",
  "montoTotal": 11300.00
}
```

### Reportes (REQ 007)

Cada endpoint ejecuta su procedimiento almacenado. Las fechas van en formato `yyyy-MM-dd`.

| Método | Endpoint | Procedimiento | Filtros |
|---|---|---|---|
| GET | `/api/reportes/clientes` | `ReporteClientesResumido` | `desde`, `hasta`, `estado` (`A`, `I` o `T`) |
| GET | `/api/reportes/facturas` | `ReporteFacturas` | `desde`, `hasta` |
| GET | `/api/reportes/auditoria` | `ReportePistasAuditoria` | `desde`, `hasta`, `usuario` (login; vacío = todos) |
| GET | `/api/reportes/seguridad` | `ReporteConfiguracionSeguridad` | `usuario` (usuario de BD; vacío = todos) |

---

## Pruebas con archivos .http

En `src/test/http` hay un archivo `.http` por cada controlador, con peticiones de ejemplo listas para usar:

| Archivo | Qué prueba |
|---|---|
| `AuthRestController.http` | Inicio de sesión con los tres logins |
| `ClientesRestController.http` | CRUD de clientes |
| `TiposImpuestoRestController.http` | CRUD de tipos de impuesto |
| `ProductosServiciosRestController.http` | CRUD de productos y servicios |
| `FacturasRestController.http` | CRUD de facturas |
| `FacturasDetalleRestController.http` | CRUD de detalles de factura |
| `NotasCreditoDebitoRestController.http` | CRUD de notas de crédito y débito |
| `ReportesRestController.http` | Los 4 reportes con distintos filtros |

**Cómo usarlos:**

1. Instalar la extensión **REST Client** en VS Code.
2. Levantar el backend.
3. Abrir cualquier archivo `.http` y tocar **Send Request** encima de la petición que se quiere probar.

---

## Manejo de errores

Los errores se manejan de forma centralizada en `GlobalExceptionHandler`:

| Excepción | Cuándo ocurre |
|---|---|
| `ResourceNotFoundException` | Se busca un registro que no existe (por ejemplo, un cliente con un id inválido). |
| `BusinessException` | Se rompe una regla del negocio (por ejemplo, un código CABYS que no tiene 13 dígitos o un porcentaje fuera de rango). |

Los mensajes de error que lanzan los procedimientos almacenados con `THROW` también se devuelven al cliente como respuesta de la API.

---

## Curso

**IF0010 – Bases de Datos II**
Universidad de Costa Rica, Recinto de Paraíso
II ciclo 2026