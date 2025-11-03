# Spark Doubt Backend - SRM Doubt Sharing Platform

A Spring Boot backend application for a doubt-sharing platform similar to Doubtnut, specifically designed for SRM students.

## Features

- **User Authentication**: JWT-based authentication with register and login
- **Page Management**: Different pages for CSE, ECE, Math, Physics, AI/ML, and General doubts
- **Question Management**: Users can post, update, and delete questions
- **Reply Management**: Users can reply to questions, update and delete their replies
- **Role-Based Access**: Admin and User roles with different permissions
- **Database**: H2 (development) / PostgreSQL (production) support

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** with JWT
- **Spring Data JPA**
- **H2 Database** (development)
- **PostgreSQL** (production)
- **Maven**
- **Lombok**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL (for production)

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd backend
```

### 2. Configure the database

The application is configured to use H2 in-memory database by default for development.

For **production** with PostgreSQL, uncomment the following lines in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sparkdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 5. Access H2 Console (Development only)

Visit: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:sparkdb`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user

### Pages

- `GET /api/pages` - Get all pages
- `GET /api/pages/{id}` - Get page by ID
- `GET /api/pages/name/{name}` - Get page by name
- `POST /api/pages` - Create a new page (Admin only)
- `DELETE /api/pages/{id}` - Delete a page (Admin only)

### Questions

- `GET /api/questions/page/{pageId}?page=0&size=20` - Get questions by page ID (paginated)
- `GET /api/questions/page/name/{pageName}?page=0&size=20` - Get questions by page name
- `GET /api/questions/{id}` - Get question by ID
- `POST /api/questions` - Create a new question (authenticated)
- `PUT /api/questions/{id}` - Update a question (owner only)
- `DELETE /api/questions/{id}` - Delete a question (owner or admin)

### Replies

- `GET /api/replies/question/{questionId}` - Get all replies for a question
- `POST /api/replies/question/{questionId}` - Add a reply (authenticated)
- `PUT /api/replies/{id}` - Update a reply (owner only)
- `DELETE /api/replies/{id}` - Delete a reply (owner or admin)

## Request/Response Examples

### Register User

**Request:**
```json
POST /api/auth/register
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": "uuid",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### Create Question

**Request:**
```json
POST /api/questions
Authorization: Bearer <token>
{
  "title": "How to implement binary search?",
  "description": "I'm confused about the implementation of binary search algorithm...",
  "pageId": "cse-page-uuid"
}
```

**Response:**
```json
{
  "id": "question-uuid",
  "title": "How to implement binary search?",
  "description": "I'm confused about...",
  "userId": "user-uuid",
  "userName": "John Doe",
  "pageId": "cse-page-uuid",
  "pageName": "CSE",
  "replyCount": 0,
  "createdAt": "2025-11-03T10:30:00",
  "updatedAt": "2025-11-03T10:30:00"
}
```

### Add Reply

**Request:**
```json
POST /api/replies/question/{questionId}
Authorization: Bearer <token>
{
  "content": "Here's how you implement binary search..."
}
```

## Default Pages

The application initializes with the following pages:
- **CSE**: Computer Science and Engineering
- **ECE**: Electronics and Communication Engineering
- **Mathematics**: Mathematics
- **Physics**: Physics
- **AI/ML**: Artificial Intelligence and Machine Learning
- **General**: General Doubts

## Security

- JWT tokens expire after 24 hours (configurable in `application.properties`)
- Passwords are encrypted using BCrypt
- CORS is enabled for `http://localhost:5173` and `http://localhost:3000`

## Development

To modify CORS allowed origins, update the `SecurityConfig.java`:

```java
configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://your-frontend-url"));
```

## Database Schema

### Users Table
- id (UUID)
- name (String)
- email (String, unique)
- password (String, encrypted)
- role (String)
- created_at (Timestamp)

### Pages Table
- id (UUID)
- name (String, unique)
- description (String)
- created_at (Timestamp)

### Questions Table
- id (UUID)
- title (String)
- description (Text)
- user_id (UUID, FK)
- page_id (UUID, FK)
- created_at (Timestamp)
- updated_at (Timestamp)

### Replies Table
- id (UUID)
- content (Text)
- question_id (UUID, FK)
- user_id (UUID, FK)
- created_at (Timestamp)
- updated_at (Timestamp)

## License

This project is for educational purposes.
