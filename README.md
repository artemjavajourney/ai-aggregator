# AI Studio (Frontend + Backend)

A tiny "OpenAI-like" UI where you ask a question and see a 3-step pipeline:
THINK -> CRITIQUE -> VALIDATE -> FINAL.

First version runs with local stub agents (no API keys).
Later we will plug real providers behind an interface.

## Prerequisites
- Java 17+
- Node 18+

## Run backend (Spring Boot)
```bash
cd ai-studio-backend
mvn spring-boot:run
```
Backend: http://localhost:8080

## Run frontend (React + Vite)
```bash
cd ai-studio-frontend
npm i
npm run dev
```
Frontend: http://localhost:5173

Vite proxies `/api` to the backend.

## Useful endpoints
- GET  /api/sessions
- POST /api/sessions
- GET  /api/sessions/{id}/messages
- POST /api/sessions/{id}/ask  { "question": "..." }
