# PEER-POINT

A full-stack web application for sharing and discussing doubts (Spark Doubt platform).

## What is this project about

PEER-POINT is built to help students and learners share academic questions (doubts), get community answers, and discuss solutions collaboratively. It focuses on quick, searchable threads of questions with support for:

- Creating and browsing doubt posts (text, code snippets, images)
- Commenting and threaded discussion for follow-up questions
- Voting or reacting to useful answers (community moderation)
- User authentication and role-based access (sign up / login, protected endpoints)

The goal is to make it fast and easy for students to get help, track recurring problems, and build a searchable knowledge base of solved doubts.

This repository contains a React + Vite frontend (TypeScript, Tailwind, shadcn UI components) and a Java Spring Boot backend (Java 17, Spring Boot 3.x) with JPA and JWT-based auth.

## Where to find the code

- Frontend: `./src` (React + TypeScript + Vite)
- Backend: `./backend/src/main/java` (Spring Boot application)

## Tech stack

- Frontend
	- Vite
	- React 18 (TypeScript)
	- Tailwind CSS
	- shadcn-ui (Radix + custom UI primitives)
	- React Router, React Query

- Backend
	- Java 17
	- Spring Boot 3.x (web, data-jpa, security, validation)
	- JPA/Hibernate
	- PostgreSQL (production), H2 (dev/test)
	- JWT (io.jsonwebtoken)

- Dev / Tooling
	- Node.js, npm/yarn (frontend)
	- Maven (backend)
	- ESLint, TypeScript, Tailwind, Vite

## Key dependencies

Frontend (selected from `package.json`)

- Runtime
	- react ^18.3.1
	- react-dom ^18.3.1
	- @tanstack/react-query ^5.83.0
	- react-router-dom ^6.30.1
	- tailwindcss-animate ^1.0.7
	- framer-motion ^12.23.24
	- date-fns ^3.6.0
	- zod ^3.25.76
	- lucide-react ^0.462.0
	- recharts ^2.15.4
	- sonner ^1.7.4

- UI / Radix (shadcn UI uses these)
	- @radix-ui/react-* (accordion, dialog, popover, toast, etc.)
	- class-variance-authority, clsx

- DevDependencies (selected)
	- vite ^5.4.19
	- typescript ^5.8.3
	- tailwindcss ^3.4.17
	- @vitejs/plugin-react-swc ^3.11.0
	- eslint ^9.32.0

For the full, exact lists see `package.json` in the repo root.

Backend (selected from `backend/pom.xml`)

- Spring Boot
	- org.springframework.boot:spring-boot-starter-web
	- org.springframework.boot:spring-boot-starter-data-jpa
	- org.springframework.boot:spring-boot-starter-security
	- org.springframework.boot:spring-boot-starter-validation

- Databases
	- org.postgresql:postgresql (runtime)
	- com.h2database:h2 (runtime, dev/test)

- Auth / JWT
	- io.jsonwebtoken:jjwt-api / jjwt-impl / jjwt-jackson (0.12.3)

- Helpers / tooling
	- org.projectlombok:lombok (optional)
	- org.springframework.boot:spring-boot-devtools (optional, runtime)

For the complete backend dependency list and versions, see `backend/pom.xml`.

## Quick start (development)

1) Frontend

```powershell
cd "c:\Users\Aditya Mishra\Desktop\APP project\spark-animate-learn"
npm install
npm run dev
```

2) Backend

From the repository root (requires Maven):

```powershell
cd backend
mvn spring-boot:run
```

(If you prefer the wrapper and it's present: `./mvnw spring-boot:run`)

## Notes

- Database configuration is in `backend/src/main/resources/application.properties` (or `target/classes/application.properties` for packaged builds).
- Adjust environment variables and `application.properties` to point to PostgreSQL in production.

If you want, I can also add a short CONTRIBUTING or Developer Setup section with step-by-step environment setup and common env vars to set (DB URL, JWT secret, etc.).
