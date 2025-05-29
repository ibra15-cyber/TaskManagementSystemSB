# Task Management API

A Spring Boot REST API for task CRUD operations with dynamic search capabilities using JPA Specifications.

## FIND ATTACHED THE IN THE ROOT FOLDER THE ARCHITECTURE DIAGRAM in architecture-diagram.mermaid file
## AND THE SPRING CONCEPT EXPLAINED IN THE spring-concepts-summary 
## The link to the 
## Prerequisites

- **Java 21** (required by the project toolchain)
- **PostgreSQL** database server
- **Gradle** (wrapper included)
- **Git** (for version control)

## Tech Stack

- **Spring Boot 3.5.0** - Application framework
- **Spring Data JPA** - Data persistence layer
- **PostgreSQL** - Database
- **Lombok** - Code generation for reducing boilerplate
- **SpringDoc OpenAPI** - API documentation
- **Spring DotEnv** - Environment variable management

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd <project-directory>
```

### 2. Database Setup

1. Install and start PostgreSQL
2. Create a database for the application:
   ```sql
   CREATE DATABASE taskdb;
   ```

### 3. Environment Configuration

Create a `.env` file in the project root with your database credentials:

```env
DB_URL=jdbc:postgresql://localhost:5432/taskdb
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### 4. Application Properties

Ensure your `application.properties` or `application.yml` references the environment variables:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# OpenAPI Documentation
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### 5. Build and Run

#### Using Gradle Wrapper (Recommended)

```bash
# Make gradlew executable (Linux/Mac)
chmod +x gradlew

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

#### Using Gradle (if installed globally)

```bash
gradle build
gradle bootRun
```

### 6. Verify Installation

Once the application starts:

- **Application**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/api-docs

## Available Endpoints

### Task Management

- `GET /api/tasks` - Get all tasks with optional dynamic search parameters
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update existing task
- `DELETE /api/tasks/{id}` - Delete task

### Dynamic Search Parameters

The API supports dynamic search using JPA Specifications. You can filter tasks using query parameters:

```
GET /api/tasks?title=example&status=PENDING&page=0&size=10&sort=createdDate,desc
```

## Project Structure

```
src/
├── main/
│   ├── java/com/ibra/
│   │   ├── controller/     # REST controllers
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # JPA repositories
│   │   ├── service/        # Business logic
│   │   ├── specification/  # JPA specifications for dynamic queries
│   │   └── Application.java
│   └── resources/
│       ├── application.properties
│       └── static/
└── test/
    └── java/
```

## Development

### Running Tests

```bash
./gradlew test
```

### Creating JAR

```bash
./gradlew bootJar
```

The executable JAR will be created in `build/libs/`.

### Database Migration

The application uses Hibernate with `ddl-auto=update`, so schema changes are automatically applied. For production, consider using Flyway or Liquibase for proper database versioning.

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Verify PostgreSQL is running
   - Check database credentials in `.env` file
   - Ensure database exists

2. **Port Already in Use**
   - Change the port in `application.properties`: `server.port=8081`

3. **Java Version Issues**
   - Ensure Java 21 is installed and set as JAVA_HOME
   - Check with `java -version`

### Logs

Application logs are available in the console. To enable more detailed logging:

```properties
logging.level.com.ibra=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

[Add your license information here]