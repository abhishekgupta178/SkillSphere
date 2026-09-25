# SkillSphere — Complete Working Project

## Stack
Frontend: HTML + CSS + JavaScript
Backend: Java 17 + Spring Boot + REST API
Database: MySQL + Spring Data JPA / Hibernate
Build: Maven

## Project structure
- frontend/index.html
- frontend/css/style.css
- frontend/js/app.js
- backend/pom.xml
- backend/src/main/java/... (Java source files)
- backend/src/main/resources/application.properties
- database/schema.sql

## Features
- Login screen with a separate Create Account flow
- Registration collects full name, email, password and confirmation
- Separate Dashboard, My Profile, My Skills and My Projects sections
- Add/remove skills with Beginner, Intermediate or Advanced level
- Add/remove projects and GitHub links
- Find Students page with search by particular skill
- Search results come from Spring Boot + MySQL through REST API

## Run
1. Start MySQL and make sure `backend/src/main/resources/application.properties` has the correct MySQL password.
2. From the backend folder:

```bash
mvn spring-boot:run
```

3. In VS Code, open `frontend/index.html` with **Live Server**.
4. Open the Live Server URL, normally:

```text
http://localhost:5500/frontend/
```

The project does not use Python. Live Server is enough for the frontend.

## Main API endpoints
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/user/{id}

GET/POST /api/students
GET/PUT/DELETE /api/students/{id}
GET /api/students/search?skill=Java
GET/POST/DELETE /api/students/{id}/skills
GET/POST/DELETE /api/students/{id}/projects/{projectId}

GET/POST /api/skills
GET/PUT/DELETE /api/skills/{id}

GET/POST /api/projects
GET/PUT/DELETE /api/projects/{id}

## Note
The login implementation is intended for a college-project demonstration. Passwords are currently stored as provided; a production deployment should use BCrypt/Spring Security.
