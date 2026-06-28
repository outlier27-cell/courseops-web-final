# CourseOps

CourseOps is a course project operations system for managing course spaces, project materials, submissions, feedback, analytics, notifications, and audit logs.

## Project Structure

- `courseops-web/`: Vue 3 + Vite frontend.
- `courseops-backend/`: Spring Boot backend, package namespace `cn.edu.uibe.courseops`.
- `docker-compose.yml`: MySQL and MongoDB local services.
- `docs/`: system manual, UI/design audits, and delivery documentation.

## Local Run

Start databases:

```bash
docker compose up -d
```

Start backend:

```bash
cd courseops-backend
./mvnw spring-boot:run
```

Start frontend:

```bash
cd courseops-web
npm install
npm run dev -- --host 127.0.0.1 --port 5173
```

Open:

```text
http://127.0.0.1:5173/
```

## Demo Accounts

```text
Student: u-student-001
Teacher: u-teacher-001
Admin:   u-admin-001
Password: 123456
```

## Build Checks

Frontend:

```bash
cd courseops-web
npm run build
```

Backend:

```bash
cd courseops-backend
./mvnw test
```
