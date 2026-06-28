# UIBE CourseOps 后端运行说明

## 当前状态

本项目已新增 `courseops-backend` Spring Boot 后端，并将前端 `courseopsApi.ts` 接入真实 HTTP API。

前端默认行为：

- 优先请求 `http://127.0.0.1:8080/api`
- 如果后端或数据库未启动，自动 fallback 到前端 mock 数据
- 上传、状态变更、日志追加已保留原有前端链路

## 技术栈

- Java 21
- Spring Boot 3.5.8
- Spring Security
- JWT
- MyBatis-Plus
- MySQL 8
- MongoDB 7
- Knife4j / OpenAPI
- Multipart 文件上传

## 数据库启动

先打开 Docker Desktop，然后在项目根目录执行：

```powershell
docker compose up -d mysql mongo
```

MySQL 初始化脚本：

```text
courseops-backend/src/main/resources/db/schema.sql
```

数据库默认连接：

```text
MySQL:  localhost:3306/courseops
User:   courseops
Pass:   courseops123

Mongo:  mongodb://courseops:courseops123@localhost:27017/courseops_logs?authSource=admin
```

## 启动后端

当前机器没有全局 Maven，但工程自带 Maven Wrapper。

```powershell
cd courseops-backend
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
.\mvnw.cmd spring-boot:run
```

后端地址：

```text
http://127.0.0.1:8080
```

Knife4j / Swagger：

```text
http://127.0.0.1:8080/doc.html
http://127.0.0.1:8080/swagger-ui.html
```

## 启动前端

```powershell
cd courseops-web
npm.cmd run dev
```

前端地址：

```text
http://127.0.0.1:5173/
```

如果要强制关闭 mock fallback：

```powershell
$env:VITE_COURSEOPS_MOCK_FALLBACK='false'
npm.cmd run dev
```

## 已实现 API

认证：

- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/auth/users`

课程：

- `GET /api/courses`
- `GET /api/courses/{id}`

项目：

- `GET /api/projects`
- `GET /api/projects/{id}`
- `POST /api/projects`
- `PUT /api/projects/{id}`
- `DELETE /api/projects/{id}`
- `GET /api/projects/{id}/members`

材料：

- `GET /api/materials`
- `GET /api/materials/{id}`
- `PATCH /api/materials/{id}/status`
- `POST /api/materials/{id}/upload`
- `GET /api/materials/{id}/histories`
- `GET /api/materials/{id}/feedback`

提交记录：

- `GET /api/submissions`
- `POST /api/submissions`
- `GET /api/submissions/material/{materialId}`

教师反馈：

- `GET /api/feedback`
- `POST /api/feedback`
- `PATCH /api/feedback/{id}/resolve`
- `GET /api/feedback/project/{projectId}`

通知：

- `GET /api/notifications`
- `GET /api/notifications/unread-count`
- `PATCH /api/notifications/{id}/read`

日志：

- `GET /api/logs`
- `POST /api/logs`

分析：

- `GET /api/analytics`

## 已验证

```powershell
cd courseops-backend
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
.\mvnw.cmd -q -DskipTests package
```

通过。

```powershell
cd courseops-web
npm.cmd run build
```

通过。

## 当前阻塞

Docker Desktop daemon 当前未启动，因此本轮无法实际拉起 MySQL/Mongo 容器做运行时 API 冒烟测试。

错误摘要：

```text
failed to connect to the docker API at npipe:////./pipe/dockerDesktopLinuxEngine
```

解决方式：

1. 打开 Docker Desktop。
2. 等待左下角显示 Docker Engine running。
3. 重新执行 `docker compose up -d mysql mongo`。
4. 再启动 Spring Boot 后端。
