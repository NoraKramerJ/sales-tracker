# Sales Tracker

A simple full-stack CRUD app: Java Spring Boot + PostgreSQL + Angular.

## Project Structure

```
sales-tracker/
├── backend/        # Spring Boot REST API
├── frontend/       # Angular app
└── database/       # SQL init script
```

## Prerequisites

- Java 17+
- Maven
- PostgreSQL
- Node.js + npm
- Angular CLI (`npm install -g @angular/cli`)

---

## 1. Database Setup

```bash
psql -U postgres
CREATE DATABASE salestracker;
\c salestracker
\i database/init.sql
```

---

## 2. Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

API runs at: http://localhost:8080/api/sales

### Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/sales | Get all sales |
| GET | /api/sales/{id} | Get sale by ID |
| POST | /api/sales | Create sale |
| PUT | /api/sales/{id} | Update sale |
| DELETE | /api/sales/{id} | Delete sale |

---

## 3. Frontend (Angular)

```bash
cd frontend
npm install
ng serve
```

App runs at: http://localhost:4200

---

## Database Config

Edit `backend/src/main/resources/application.properties` if your PostgreSQL credentials differ:

```properties
spring.datasource.username=postgres
spring.datasource.password=postgres
```
