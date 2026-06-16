# Loan Request Service 

A full-stack application for managing customers and loan requests with Angular 18 frontend and Spring Boot backend.

## Prerequisites
- Java 21
- Maven
- PostgreSQL (or H2)
- Node.js (v18 or higher)
- npm (v9 or higher)


## DEPLOYMENT OPTIONS


### Option 1: Using Start Script 

```bash
./start.sh backend
```

```bash
./start.sh frontend
```

### Option 2: Manual Start

```bash
./mvnw spring-boot:run
```

```bash
npm install
npm start
```

### Option 3: Docker Compose
```bash
./start.sh docker
```

The frontend will be available at `http://localhost:4200`

### Testing

```bash
./mvnw test
```

```bash
cd frontend
npm test
```

### Database
```bash
# Use H2 for testing:
# Update database config in application.properties
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

## Technology Stack

**Backend**:
- Java 21
- Spring Boot 3.3
- Spring Data JPA
- Hibernate
- Maven
- JUnit 5
- Mockito

**Frontend**:
- Angular 18
- TypeScript 5.4
- Bootstrap 5
- RxJS
- HTML5/SCSS

**Database**:
- PostgreSQL (configurable)
- H2 (for testing)

**DevOps**:
- Docker & Docker Compose
- Maven
- npm
