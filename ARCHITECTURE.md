# OrderIQ — Architecture

> Status: **in progress** — Architecture-baseline stage. Diagrams to be embedded from `docs/diagrams/`.

## 1. System overview
The three services and how they relate.

## 2. Component responsibilities
- **order-api-service** — secured REST gateway.
- **ai-middleware-service** — LLM provider abstraction, caching, audit.
- **async-worker-service** — background report jobs via events.

## 3. Data model
Entity groups and the multi-tenancy strategy. (ERD in `docs/diagrams/`.)

## 4. Synchronous prediction flow
## 5. Asynchronous report flow
## 6. Cross-cutting concerns
Caching, resilience, observability.

## 7. Deployment view
Docker Compose (local) and Kubernetes.

## Diagrams
- System architecture — `docs/diagrams/`
- Data model (ERD) — `docs/diagrams/`
- Request / async flow — `docs/diagrams/`
- Deployment view — `docs/diagrams/`
