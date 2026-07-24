# Microservicio de Gestión de Descuentos

Este microservicio se encarga de la administración y publicación de descuentos promocionales en la plataforma de cine. Está desarrollado en Java 23 utilizando **Spring Boot 3.5.5** y cumple con los patrones de diseño orientados a objetos con un modelo de dominio rico (no anémico).

## 🚀 Tecnologías Principales
* **Java 23**
* **Spring Boot 3.5.5** (Spring Web, JPA/Hibernate, Security, OAuth2 Resource Server)
* **PostgreSQL 16** (Base de datos persistente)
* **RabbitMQ** (Mensajería orientada a eventos de integración)
* **Keycloak** (Autenticación y autorización mediante tokens JWT)
* **Lombok** (Para DTOs e infraestructura, excluido del modelo de dominio)

---

## 🛠️ Estructura del Proyecto
El código sigue la estructura por capas y paquetes típica de la arquitectura del ecosistema:
```text
src/main/java/com/unrn/descuentos
├── config          # Configuraciones de RabbitMQ y Seguridad (Keycloak)
├── controller      # Controladores REST expuestos
├── domain          # Entidad de dominio rica (validaciones y negocio encapsulado)
├── dto             # Objetos de Transferencia de Datos (DTO)
├── event           # Clases y estructuras para eventos RabbitMQ
├── repository      # Repositorios JPA
└── service         # Lógica de aplicación y publicación de eventos
```

---

## 📋 Endpoints de la API REST

### Públicos
* **GET `/descuentos`**: Obtiene todos los descuentos actualmente válidos (activos en la fecha actual).
* **GET `/descuentos/{id}`**: Obtiene el detalle de un descuento por su identificador.

### Administrativos (Requieren Token JWT Keycloak)
* **POST `/descuentos`**: Crea un nuevo descuento.
  * **Payload de entrada:**
    ```json
    {
      "codigo": "CINECLOUDFREE",
      "descripcion": "Descuento especial por inauguración",
      "monto": 250.00,
      "fechaDesde": "2026-06-01",
      "fechaHasta": "2026-06-30"
    }
    ```
* **PUT `/descuentos/{id}`**: Modifica un descuento existente.
* **DELETE `/descuentos/{id}`**: Elimina un descuento.
* **GET `/descuentos/todos`**: Lista la totalidad de descuentos registrados.

---

## 📬 Eventos de Integración (RabbitMQ)

Cada vez que se realiza una modificación mutable sobre los descuentos, se emite un evento al Exchange de mensajería:

* **Exchange:** `descuento_exchange` (configurable en propiedades)
* **Routing Key:** `descuento.event` (configurable en propiedades)

### Formato de Evento
```json
{
  "eventType": "CREATE | UPDATE | DELETE",
  "key": 1,
  "data": {
    "id": 1,
    "codigo": "CINECLOUDFREE",
    "monto": 250.00,
    "fechaDesde": "2026-06-01",
    "fechaHasta": "2026-06-30"
  }
}
```

---

## ⚙️ Configuración y Ejecución

El microservicio se ejecuta en el puerto **`8083`** para evitar colisiones con otros microservicios.

### Ejecución Local con Maven
Para compilar y correr las pruebas automáticas en memoria (H2):
```bash
./mvnw clean test
```

Para correr la aplicación de forma local conectándose a los servicios comunes:
```bash
./mvnw spring-boot:run
```

### Ejecución con Docker Compose
La infraestructura centralizada del proyecto se encuentra en el directorio `/infraestructura`. Se puede levantar la base de datos de descuentos y el contenedor del microservicio ejecutando:
```bash
docker compose up -d descuentos-app
```
*(Nota: la configuración del contenedor mapea el puerto host `8083:8083` y el puerto de base de datos PostgreSQL a `5434:5432`)*
