# Shopping Cart — Prueba Técnica Java

Sistema de carrito de compras basado en microservicios con Spring Boot 3.5.x, Java 21 y MySQL.

**Autor:** Luis Henriquez

---

## Arquitectura

### Diagrama de comunicación

```
                     ┌─────────────┐
                     │  gateway-api │  (8080)
                     └──────┬──────┘
          ┌─────────────────┼─────────────────────┐
          │                 │                     │
          ▼                 ▼                     ▼
    ┌──────────┐    ┌──────────────┐    ┌──────────────┐
    │ auth-api │    │ customer-api │    │  product-api │
    │  (8081)  │    │   (8082)     │    │   (8083)     │
    └──────────┘    └──────┬───────┘    └──────────────┘
                           │                    │
                           │ Feign              │ WebClient
                           ▼                    ▼
                     ┌──────────┐     ┌──────────────┐
                     │ order-api│     │ fakestoreapi │
                     │  (8084)  │     │  .com/products│
                     └────┬─────┘     └──────────────┘
                     ┌────┴─────┐
                     │          │
                     ▼          ▼
              ┌──────────┐ ┌──────────┐
              │detail-api│ │payment-api│
              │  (8085)  │ │  (8086)  │
              └──────────┘ └──────────┘
```

### Puertos

| Servicio       | Puerto |
|----------------|--------|
| gateway-api    | 8080   |
| auth-api       | 8081   |
| customer-api   | 8082   |
| product-api    | 8083   |
| order-api      | 8084   |
| detail-api     | 8085   |
| payment-api    | 8086   |

### Comunicación entre servicios

- **Spring Cloud Gateway** (`gateway-api`): Enruta peticiones a cada microservicio según el path. Filtro JWT global.
- **OpenFeign**: Llamadas síncronas entre microservicios (auth → customer → order → detail/payment).
- **WebClient reactivo**: `product-api` actúa como proxy hacia `https://fakestoreapi.com/products`.
- **Base de datos única**: Todos los servicios con JPA comparten la misma base de datos MySQL `shopping_cart` con `ddl-auto=update`.

---

## Requisitos previos

- **Java 21** (JDK 21+)
- **Maven 3.9+** (incluido como `mvnw` en cada servicio)
- **Docker Desktop** (para MySQL y ejecución de servicios)
- **Git**

---

## Inicio rápido

### 1. Base de datos (MySQL)

```bash
docker run -d \
  --name mysql-db \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=shopping_cart \
  -p 3306:3306 \
  mysql:8.0
```

O usando Docker Compose (incluye también los 6 microservicios):

```bash
docker-compose up -d
```

> ⚠️ `gateway-api` no está en `docker-compose.yml`. Debe ejecutarse por separado.

### 2. Variables de entorno

Dos variables son requeridas en el perfil por defecto:

```bash
export DB_PASSWORD=root
export JWT_SECRET=Z3sOhmRhZG9yLXR1LWaVanJldGUtY29uLXNvbG9yaW8tcGFyYS1sYS1jbGF2ZS1kZWotanVnaW1lbnRvLWhzNTEyLXNlY3JldA==
```

En Windows (PowerShell):

```powershell
$env:DB_PASSWORD="root"
$env:JWT_SECRET="Z3sOhmRhZG9yLXR1LWaVanJldGUtY29uLXNvbG9yaW8tcGFyYS1sYS1jbGF2ZS1kZWotanVnaW1lbnRvLWhzNTEyLXNlY3JldA=="
```

> El JWT_SECRET es una clave HS512 en base64. El valor mostrado aquí es el de desarrollo.

### 3. Construir y ejecutar (Maven)

Cada microservicio se construye individualmente. No hay un `pom.xml` raíz.

```bash
# Construir un servicio
cd auth-api
./mvnw clean install

# Ejecutar con perfil de desarrollo (hardcodea DB_PASSWORD y JWT_SECRET)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Ejecutar con perfil por defecto (requiere variables de entorno)
./mvnw spring-boot:run
```

Repetir para cada servicio. Orden sugerido:
1. `auth-api`
2. `customer-api`
3. `product-api`
4. `order-api`
5. `detail-api`
6. `payment-api`
7. `gateway-api`

### 4. Docker Compose (todo en uno)

```bash
docker-compose up -d --build
```

Esto inicia MySQL y los 7 microservicios. 

---

## Perfiles

| Perfil   | DB_PASSWORD  | JWT_SECRET              | Uso                         |
|----------|-------------|-------------------------|-----------------------------|
| default  | `${DB_PASSWORD}` (env) | `${JWT_SECRET}` (env) | Producción / pruebas externas |
| dev      | `root` (hardcodeado)   | Hardcodeado            | Desarrollo local            |
| qa       | `${DB_PASSWORD}` (env) | Hardcodeado            | QA / testing                |

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Los perfiles `dev` también habilitan Swagger/OpenAPI:

```
http://localhost:{puerto}/swagger-ui.html
```

---

## Endpoints de la API (a través de gateway-api)

### gateway-api (http://localhost:8080)

| Método | Path                          | Servicio destino |
|--------|-------------------------------|-----------------|
| POST   | `/api/v1/auth/login`          | auth-api        |
| POST   | `/api/v1/auth/register`       | auth-api        |
| GET    | `/api/v1/auth/validate`       | auth-api        |
| GET    | `/api/v1/auth/{id}/exists`    | auth-api        |
| GET    | `/api/v1/customers`           | customer-api    |
| GET    | `/api/v1/customers/{id}`      | customer-api    |
| POST   | `/api/v1/customers`           | customer-api    |
| PUT    | `/api/v1/customers/{id}`      | customer-api    |
| DELETE | `/api/v1/customers/{id}`      | customer-api    |
| GET    | `/api/v1/products`            | product-api     |
| GET    | `/api/v1/products/{id}`       | product-api     |
| POST   | `/api/v1/orders`              | order-api       |
| GET    | `/api/v1/orders/{id}`         | order-api       |
| GET    | `/api/v1/orders/customer/{id}`| order-api       |
| PATCH  | `/api/v1/orders/{id}/cancel`  | order-api       |
| PATCH  | `/api/v1/orders/{id}/finish`  | order-api       |
| GET    | `/api/v1/orders/{id}/exists`  | order-api       |
| GET    | `/api/v1/orders/{id}/status`  | order-api       |
| POST   | `/api/v1/orders-detail`       | detail-api      |
| GET    | `/api/v1/orders-detail/{id}`  | detail-api      |
| GET    | `/api/v1/orders-detail/order/{orderId}` | detail-api |
| DELETE | `/api/v1/orders-detail/{id}`  | detail-api      |
| GET    | `/api/v1/orders-detail/order/{orderId}/total` | detail-api |
| POST   | `/api/v1/payments`            | payment-api     |
| GET    | `/api/v1/payments/{id}`       | payment-api     |

### Flujo típico

```
1. POST /api/v1/auth/register       → crea usuario
2. POST /api/v1/auth/login          → obtiene JWT token
3. POST /api/v1/customers           → crea cliente (requiere userId)
4. POST /api/v1/orders              → crea orden (requiere customerId)
5. POST /api/v1/orders-detail       → agrega productos a la orden
6. GET  /api/v1/orders-detail/order/{id}/total  → calcula total
7. POST /api/v1/payments            → procesa pago (requiere orderId)
```

Incluir el header en todas las requests autenticadas:

```
Authorization: Bearer <jwt-token>
```

---

## Notas importantes

### Esquema de base de datos

El script de referencia está en `resources/sql.sql`. Las tablas se crean automáticamente con `ddl-auto=update`, pero el SQL documenta la estructura esperada:

- `user_account` — usuarios del sistema (auth)
- `customer` — clientes (customer)
- `orders` — órdenes (order)
- `order_detail` — detalle de órdenes (detail)
- `order_payment` — pagos (payment)

## Referencias

- [Spring Boot 3.5.x](https://docs.spring.io/spring-boot/docs/3.5.x/reference/)
- [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/)
- [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/)
- [FakeStore API](https://fakestoreapi.com/)
- [MapStruct](https://mapstruct.org/)
