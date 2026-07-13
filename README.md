# Runswithshedlock

A distributed scheduling and locking application built with Spring Boot 3.5.0, featuring ShedLock for preventing duplicate scheduled job executions in distributed systems. Combines Spring Scheduling with Thymeleaf frontend and HTMX for managing scheduled tasks across multiple instances.

**Author:** Sathish Jayapal
**Last Updated:** February 2026

---

## Table of Contents

- [Project Description](#project-description)
- [Tech Stack](#tech-stack)
- [Key Features](#key-features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Development](#development)
- [Building for Production](#building-for-production)
- [Docker Deployment](#docker-deployment)
- [Testing](#testing)
- [Database Management](#database-management)
- [ShedLock Configuration](#shedlock-configuration)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)
- [Resources](#resources)

---

## Project Description

**Runswithshedlock** is a demonstration of distributed scheduled task management in Spring Boot microservices. It showcases:
- Backend: Spring Boot 3.5.0 with Java 21
- Frontend: Thymeleaf with HTMX for interactive task management UI
- Scheduling: ShedLock 6.3.0 for distributed locking
- Database: PostgreSQL with Flyway migrations
- Security: Spring Security with session management
- Code Quality: Spotless for code formatting, PlantUML diagrams

This project exemplifies enterprise patterns for running scheduled jobs safely across multiple instances without duplicate executions, making it ideal for microservices architectures.

---

## Tech Stack

### Backend
- **Framework:** Spring Boot 3.5.0
- **Language:** Java 21
- **Build Tool:** Maven 3.8+
- **Database:** PostgreSQL 14+
- **ORM:** Spring Data JPA / Hibernate
- **Scheduling:** ShedLock 6.3.0 with JDBC backend
- **Security:** Spring Security 6.x with session management (JDBC)
- **Database Migrations:** Flyway
- **Code Quality:** Spotless for formatting
- **Documentation:** PlantUML for diagrams
- **API Documentation:** SpringDoc OpenAPI 2.8.6

### Frontend
- **Template Engine:** Thymeleaf 3.x
- **Security Integration:** Thymeleaf Extras Spring Security 6
- **Interactivity:** HTMX for dynamic updates
- **Styling:** Tailwind CSS
- **Node.js:** Version 22
- **Build Integration:** Frontend Maven Plugin

### Testing & Monitoring
- **Testing Framework:** JUnit 5, Spring Boot Test
- **Test Containers:** Testcontainers for PostgreSQL
- **Monitoring:** Spring Boot Actuator
- **Data Generation:** Faker for test data
- **REST Testing:** REST Assured

---

## Key Features

- **Distributed Locking:** ShedLock ensures scheduled tasks run exactly once across multiple instances
- **Spring Scheduling:** @Scheduled annotation support with cron expressions
- **Session Management:** JDBC-based session storage for distributed sessions
- **Thymeleaf UI:** Server-side rendered interface for monitoring scheduled jobs
- **HTMX Interactions:** Dynamic UI updates without page reload
- **Code Formatting:** Spotless Maven plugin for consistent code style
- **API Documentation:** OpenAPI 3.0 with Swagger UI integration
- **Security:** Spring Security integration with Thymeleaf tag support
- **Flyway Migrations:** Version-controlled database schema
- **Metrics & Monitoring:** Spring Boot Actuator endpoints
- **Docker Ready:** Complete containerization support

---

## Prerequisites

### System Requirements
- **Java:** JDK 21 or higher
- **Node.js:** Version 22 or higher
- **Maven:** 3.8.0 or higher
- **PostgreSQL:** 14 or higher
- **Docker:** Latest version (for running PostgreSQL locally)
- **Git:** For version control

### Development Tools
- **IDE:** IntelliJ IDEA (with Lombok plugin), VS Code, or Eclipse
- **Database Client:** pgAdmin or DBeaver (optional)
- **API Testing:** Postman or curl
- **PlantUML Viewer:** Optional for viewing architecture diagrams

---

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd runswithshedlock
```

### 2. Start PostgreSQL with Docker

```bash
# Using provided docker-compose (if available)
docker run -d \
  --name postgres-shedlock \
  -e POSTGRES_DB=runswithshedlock \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=P4ssword! \
  -p 5432:5432 \
  postgres:17
```

### 3. Configure Application

Create `src/main/resources/application-local.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/runswithshedlock
    username: postgres
    password: P4ssword!
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    locations: classpath:db/migration
```

### 4. Install Frontend Dependencies

```bash
npm install
```

### 5. Verify Installation

```bash
# Test database connection
mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

---

## Development

### Running the Application in IDE

#### IntelliJ IDEA Setup

1. **Install Lombok Plugin:**
   - Settings → Plugins → Search "Lombok" → Install
   - Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable

2. **Configure VM Options:**
   - Run → Edit Configurations
   - Select Spring Boot application
   - Click "Modify options" → "Add VM options"
   - Add: `-Dspring.profiles.active=local`

3. **Run the Application:**
   - Click Run to start on port 8080

### Starting the Frontend Dev Server

```bash
npm run devserver
```

This starts Webpack Dev Server on port 8081 with:
- Hot module replacement for templates/CSS
- Proxy to Spring Boot backend
- Browser auto-refresh on changes

### Development Workflow

1. **Backend Changes:** Java code → Auto-reload via DevTools
2. **Scheduled Jobs:** Modify @Scheduled methods → Monitor in Thymeleaf UI
3. **Database Schema:** Add Flyway migration → Auto-migrates on startup
4. **Frontend Changes:** Template/CSS modifications → Live reload

### Access Points

```
Frontend:           http://localhost:8081
Backend API:        http://localhost:8080
Swagger UI:         http://localhost:8080/swagger-ui.html
Health Check:       http://localhost:8080/actuator/health
Scheduled Jobs UI:  http://localhost:8081/jobs
PostgreSQL:         localhost:5432
```

---

## Building for Production

### Full Build Process

```bash
# Clean and build
mvnw clean package
```

This will:
1. Compile Java code
2. Format code with Spotless
3. Download Node.js (if not cached)
4. Install npm dependencies
5. Build optimized frontend bundle
6. Run all tests
7. Package executable JAR

### Running the Packaged Application

```bash
# Run with production profile
java -Dspring.profiles.active=production \
  -Dspring.datasource.url=jdbc:postgresql://prod-host:5432/runswithshedlock \
  -Dspring.datasource.username=postgres \
  -Dspring.datasource.password=prod-password \
  -jar ./target/runswithshedlock-0.0.1-SNAPSHOT.jar
```

### Code Formatting

```bash
# Format code with Spotless
mvnw spotless:apply

# Check formatting without applying changes
mvnw spotless:check
```

---

## Docker Deployment

### Building Docker Image

```bash
# Create OCI image
mvnw spring-boot:build-image \
  -Dspring-boot.build-image.imageName=me.sathish/runswithshedlock:latest
```

### Running Container

```bash
# Run with environment variables
docker run -d \
  --name runswithshedlock \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/runswithshedlock \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=prod-password \
  me.sathish/runswithshedlock:latest
```

### Docker Compose Example

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:17
    environment:
      POSTGRES_DB: runswithshedlock
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: P4ssword!
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data

  app1:
    image: me.sathish/runswithshedlock:latest
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: production
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/runswithshedlock
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: P4ssword!
      SHEDLOCK_ENABLED: 'true'
    depends_on:
      - postgres

  app2:
    image: me.sathish/runswithshedlock:latest
    ports:
      - "8081:8080"
    environment:
      SPRING_PROFILES_ACTIVE: production
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/runswithshedlock
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: P4ssword!
      SHEDLOCK_ENABLED: 'true'
    depends_on:
      - postgres

volumes:
  postgres-data:
```

---

## Testing

### Backend Testing

```bash
# Run all tests
mvnw test

# Run specific test class
mvnw test -Dtest=ScheduledJobTest

# Run with detailed output
mvnw test -X
```

### Running Multiple Instances

For testing ShedLock in a distributed environment:

```bash
# Terminal 1 - Instance 1
java -Dspring.profiles.active=production \
  -Dserver.port=8080 \
  -jar ./target/runswithshedlock-0.0.1-SNAPSHOT.jar

# Terminal 2 - Instance 2
java -Dspring.profiles.active=production \
  -Dserver.port=8081 \
  -jar ./target/runswithshedlock-0.0.1-SNAPSHOT.jar

# Scheduled jobs will run only on one instance
```

### Test Database Configuration

```yaml
# src/test/resources/application-test.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/runswithshedlock_test
    username: postgres
    password: test
  jpa:
    hibernate:
      ddl-auto: create-drop
  flyway:
    locations: classpath:db/migration
```

---

## Database Management

### ShedLock Schema

Flyway automatically creates the ShedLock table:

```sql
-- V001__Create_shedlock_table.sql
CREATE TABLE shedlock (
    name VARCHAR(64) NOT NULL,
    lock_at TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

CREATE INDEX idx_shedlock_lock_at ON shedlock(lock_at);
```

### Session Schema

JDBC session storage creates:

```sql
CREATE TABLE spring_session (
    primary_id CHAR(36) NOT NULL,
    session_id CHAR(36) NOT NULL,
    creation_time BIGINT NOT NULL,
    last_access_time BIGINT NOT NULL,
    max_inactive_interval INT NOT NULL,
    expiry_time BIGINT NOT NULL,
    principal_name VARCHAR(100),
    PRIMARY KEY (primary_id)
);

CREATE TABLE spring_session_attributes (
    session_primary_id CHAR(36) NOT NULL,
    attribute_name VARCHAR(200) NOT NULL,
    attribute_bytes BYTEA,
    PRIMARY KEY (session_primary_id, attribute_name),
    FOREIGN KEY (session_primary_id) REFERENCES spring_session(primary_id)
);
```

### Creating Migrations

```bash
# Create migration file
# Format: V{version}__{description}.sql
```

#### Sample Migration

```sql
-- V003__Create_scheduled_tasks_table.sql
CREATE TABLE scheduled_tasks (
    id SERIAL PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    cron_expression VARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## ShedLock Configuration

### Spring Configuration

```java
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                .withDefaultLockAtMostFor(Duration.ofMinutes(10))
                .build()
        );
    }
}
```

### Using @Scheduled with @SchedulerLock

```java
@Component
public class ScheduledTasks {

    @Scheduled(cron = "0 0 * * * *")  // Every hour
    @SchedulerLock(
        name = "task_hourly_job",
        lockAtMostFor = "55m",
        lockAtLeastFor = "4m"
    )
    public void hourlyJob() {
        // This runs only once across all instances
        // Even if multiple instances are running
        System.out.println("Hourly job executed");
    }

    @Scheduled(fixedDelay = 60000)  // Every 60 seconds
    @SchedulerLock(
        name = "task_periodic_job",
        lockAtMostFor = "50s",
        lockAtLeastFor = "5s"
    )
    public void periodicJob() {
        // Safe execution in distributed environment
    }
}
```

### Configuration Properties

```yaml
shedlock:
  enabled: true
  defaults:
    lock-at-most-for: 10m
    lock-at-least-for: 0s

spring:
  task:
    scheduling:
      thread-name-prefix: scheduled-
      pool:
        size: 5
```

---

## Project Structure

```
runswithshedlock/
├── pom.xml                              # Maven configuration
├── package.json                         # Frontend dependencies
├── docker-compose.yml                   # Service orchestration (if present)
│
├── src/main/
│   ├── java/me/sathish/runswithshedlock/
│   │   ├── config/                      # Spring & ShedLock configuration
│   │   ├── controller/                  # REST/MVC controllers
│   │   ├── entity/                      # JPA entities
│   │   ├── repository/                  # Spring Data repositories
│   │   ├── service/                     # Business logic
│   │   ├── schedule/                    # Scheduled job classes
│   │   ├── security/                    # Security configuration
│   │   ├── dto/                         # Data transfer objects
│   │   └── RunswithshedlockApplication.java
│   │
│   └── resources/
│       ├── db/migration/                # Flyway migrations
│       │   ├── V001__create_shedlock_table.sql
│       │   ├── V002__create_session_tables.sql
│       │   └── V003__initial_schema.sql
│       ├── application.yml              # Configuration
│       ├── application-local.yml        # Dev configuration
│       ├── application-production.yml   # Prod configuration
│       ├── templates/                   # Thymeleaf templates
│       │   ├── layout.html
│       │   ├── jobs/                    # Job monitoring UI
│       │   └── fragments/
│       └── static/                      # CSS, JS, images
│
├── src/test/
│   ├── java/                            # Integration tests
│   └── resources/
│       └── application-test.yml
│
└── README.md
```

---

## API Endpoints

### Job Management Endpoints

```
GET    /api/v1/jobs                      # List scheduled jobs
GET    /api/v1/jobs/{jobName}            # Get job details
POST   /api/v1/jobs/{jobName}/trigger    # Manually trigger job
GET    /api/v1/jobs/{jobName}/history    # Get job execution history
GET    /api/v1/locks                     # View ShedLock status
```

### Actuator Endpoints

```
GET    /actuator                         # Available endpoints
GET    /actuator/health                  # Health status
GET    /actuator/metrics                 # Metrics
GET    /actuator/scheduledtasks          # List scheduled tasks
GET    /actuator/threaddump              # Thread dump
```

### Documentation

```
GET    /swagger-ui.html                  # Swagger UI
GET    /v3/api-docs                      # OpenAPI JSON
```

---

## Configuration

### Application Properties

#### Development (`application-local.yml`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/runswithshedlock
    username: postgres
    password: P4ssword!
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 10
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  session:
    store-type: jdbc
    jdbc:
      initialize-schema: always
  flyway:
    locations: classpath:db/migration

  task:
    scheduling:
      pool:
        size: 5

server:
  port: 8080

shedlock:
  enabled: true
  defaults:
    lock-at-most-for: 10m
    lock-at-least-for: 0s

logging:
  level:
    root: INFO
    me.sathish: DEBUG
    org.springframework.scheduling: DEBUG
```

#### Production (`application-production.yml`)

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 20
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  session:
    store-type: jdbc
    jdbc:
      initialize-schema: always
  flyway:
    locations: classpath:db/migration

  task:
    scheduling:
      pool:
        size: 10

server:
  port: 8080
  compression:
    enabled: true

shedlock:
  enabled: ${SHEDLOCK_ENABLED:true}

logging:
  level:
    root: WARN
```

---

## Troubleshooting

### ShedLock Not Working

```
ERROR: shedlock table not found
```

**Solution:**
```bash
# Ensure Flyway migrations are applied
mvnw flyway:migrate

# Check table exists
psql -U postgres -d runswithshedlock -c "\d shedlock"
```

### Scheduled Job Running on All Instances

```
Multiple instances executing the same job
```

**Solution:**
```java
// Verify @SchedulerLock annotation is present
@Scheduled(cron = "0 0 * * * *")
@SchedulerLock(name = "unique_job_name", lockAtMostFor = "10m")
public void myJob() {
    // Implementation
}
```

### PostgreSQL Connection Issues

```
ERROR: Connection refused
```

**Solution:**
```bash
# Start PostgreSQL
docker run -d --name postgres -e POSTGRES_DB=runswithshedlock -p 5432:5432 postgres:17

# Verify connection
psql -U postgres -d runswithshedlock -c "SELECT 1"
```

### Session Storage Issues

```
ERROR: spring_session table not found
```

**Solution:**
```yaml
spring:
  session:
    store-type: jdbc
    jdbc:
      initialize-schema: always  # Auto-create tables
```

### Spotless Formatting Issues

```
ERROR: Spotless found formatting violations
```

**Solution:**
```bash
# Auto-fix formatting
mvnw spotless:apply

# Check without fixing
mvnw spotless:check
```

---

## Development Tips

### Scheduling Patterns

```java
// Cron expressions
@Scheduled(cron = "0 0 * * * *")           // Every hour
@Scheduled(cron = "0 0 0 * * *")           // Daily at midnight
@Scheduled(cron = "0 0 0 ? * MON")         // Every Monday
@Scheduled(cron = "0 */5 * * * *")         // Every 5 minutes

// Fixed delay (time between executions)
@Scheduled(fixedDelay = 60000)             // 60 seconds

// Fixed rate (time between start times)
@Scheduled(fixedRate = 60000)              // Every 60 seconds

// One-time delay
@Scheduled(initialDelay = 1000, fixedRate = 60000)
```

### Monitoring Scheduled Jobs

```thymeleaf
<!-- Template to display job status -->
<div th:each="lock : ${locks}">
  <p th:text="'Job: ' + ${lock.name}"></p>
  <p th:text="'Locked by: ' + ${lock.lockedBy}"></p>
  <p th:text="'Lock until: ' + ${lock.lockAt}"></p>
</div>
```

---

## Resources

### Official Documentation
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [ShedLock Documentation](https://github.com/lukas-krecan/ShedLock)
- [Spring Scheduling Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling)

### Database & Sessions
- [Spring Session JDBC](https://docs.spring.io/spring-session/docs/current/reference/html5/#httpsession-jdbc)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Database Migrations](https://flywaydb.org/documentation/)

### Frontend Technologies
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [HTMX Documentation](https://htmx.org/)
- [Tailwind CSS](https://tailwindcss.com/)

### Tools & Utilities
- [Spotless Code Formatter](https://github.com/diffplug/spotless)
- [PlantUML Diagrams](https://plantuml.com/)
- [SpringDoc OpenAPI](https://springdoc.org/)

### Learning Resources
- [Maven Documentation](https://maven.apache.org/)
- [Distributed Systems Patterns](https://martinfowler.com/articles/patterns-of-distributed-systems/)
