# Forum System

A full-stack forum system with a **Spring Boot** backend and a **Vue.js** frontend. This project demonstrates a modern web application with user authentication, post and comment management, and role-based access control.

## Project Structure

```
Forum/
  backend/    # Spring Boot backend (Java)
  frontend/   # Vue.js frontend
```

## Features

- User registration and login (JWT authentication)
- Role-based access control (admin, user)
- Create, edit, delete posts and comments
- Category management
- RESTful API
- Responsive frontend UI

## Backend (Spring Boot)

- **Location:** `backend/`
- **Main file:** `ForumApplication.java`
- **Build tool:** Maven
- **API:** RESTful, secured with JWT

### Setup

1. Navigate to the backend directory:
   ```sh
   cd backend
   ```
2. Build and run the application:
   ```sh
   mvn spring-boot:run
   ```
3. The backend will start on [http://localhost:8080](http://localhost:8080)

### Configuration

- Edit `src/main/resources/application.yml` for DB and JWT settings.
- Test data is loaded from `test-data.sql` on startup.

## Frontend (Vue.js)

- **Location:** `frontend/`
- **Main file:** `src/main.js`
- **Build tool:** Vite

### Setup

1. Navigate to the frontend directory:
   ```sh
   cd frontend
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the development server:
   ```sh
   npm run dev
   ```
4. The frontend will be available at [http://localhost:3000](http://localhost:3000)

## API Endpoints

- Auth: `/api/auth/login`, `/api/auth/register`
- Posts: `/api/posts`, `/api/posts/{id}`
- Comments: `/api/comments`, `/api/comments/{id}`
- Categories: `/api/categories`, `/api/categories/{id}`

## Development

- Backend: Java 17+, Maven
- Frontend: Node.js 16+, npm

## License

MIT
