# OrderIQ

> A production-style **AI order intelligence backend** for an FMCG ordering domain — built as a hands-on learning project covering modern backend engineering end to end.

OrderIQ predicts and assists ordering for a fast-moving-consumer-goods business: customers, outlets, routes, sales reps, products, and orders. It is built as three cooperating services and wrapped in the production scaffolding (security, caching, messaging, resilience, observability, containerisation, CI/CD) that a real system needs.

---

## Overview

OrderIQ is composed of three services:

| Service | Role |
| --- | --- |
| **order-api-service** | Secured REST gateway: authentication, tenant-aware CRUD, request validation. |
| **ai-middleware-service** | Sits between the APIs and an LLM: context hydration, Redis caching, resilient provider calls, audit logging. |
| **async-worker-service** | Consumes events to generate reports in the background, with idempotency, a status lifecycle, and a dead-letter queue. |

See [`ARCHITECTURE.md`](./ARCHITECTURE.md) for the full picture and diagrams.

## Tech stack

- **Language / framework:** Java 21, Spring Boot 3.5.x, Maven
- **Persistence:** PostgreSQL, Spring Data JPA (Hibernate), Flyway migrations
- **Caching:** Redis *(planned)*
- **Messaging:** Kafka *(planned)*
- **Resilience:** Resilience4j *(planned)*
- **Observability:** OpenTelemetry, Prometheus, Grafana *(planned)*
- **Packaging & infra:** Docker, Docker Compose, Kubernetes, Terraform / AWS SAM *(planned)*
- **CI/CD:** GitHub Actions *(planned)*
- **Testing:** JUnit 5, Mockito, Testcontainers *(planned)*

## Repository structure

```
orderiq/
├── README.md                # this file
├── ARCHITECTURE.md          # system, data, flow, and deployment views
├── DECISIONS.md             # Architecture Decision Records (ADRs)
├── FAILURE_HANDLING.md      # resilience: retries, circuit breakers, DLQ
├── SECURITY.md              # auth, roles, tenant isolation
├── OBSERVABILITY.md         # traces, metrics, dashboards
├── DEPLOYMENT.md            # containers, k8s, infra-as-code
├── DEMO_NOTES.md            # demo walkthrough and evidence
├── docker-compose.yml       # local infrastructure (Postgres, later Redis/Kafka)
├── .github/workflows/       # CI/CD pipelines
├── k8s/                     # Kubernetes manifests
├── infra/                   # Terraform / AWS SAM assets
├── services/
│   ├── order-api-service/   # the secured REST gateway (in progress)
│   ├── ai-middleware-service/   # (planned)
│   └── async-worker-service/    # (planned)
├── docs/
│   ├── diagrams/            # architecture & flow diagrams
│   └── screenshots/         # demo evidence
├── sample-data/             # seed data for tenants, products, orders
└── scripts/                 # helper scripts
```

## Getting started (local)

Prerequisites: **JDK 21**, **Docker Desktop**, and an IDE (IntelliJ IDEA Community).

```bash
# 1. Start local infrastructure (PostgreSQL)
docker compose up -d

# 2. Run the order API (from services/order-api-service)
./mvnw spring-boot:run
```

On startup, Flyway creates the schema and seeds reference data, then the service is available at `http://localhost:8080`.

Quick check:

```bash
curl http://localhost:8080/api/roles      # lists the five roles
```

See [`DEPLOYMENT.md`](./DEPLOYMENT.md) for the full deployment guide.

## Project status

This project is built in phases. See [`docs/PROJECT_PLAN.md`](./docs/PROJECT_PLAN.md) for the milestone plan and backlog.

| Phase | Focus | Status |
| --- | --- | --- |
| 1 | Foundation: data model, Order API, security | 🟡 in progress |
| 2 | AI middleware path: provider, cache, resilience | ⚪ not started |
| 3 | Event-driven async: Kafka, worker, DLQ | ⚪ not started |
| 4 | Observability: traces, metrics, dashboards | ⚪ not started |
| 5 | Packaging & shipping: Docker, K8s, IaC, CI/CD | ⚪ not started |

## Documentation index

[Architecture](./ARCHITECTURE.md) · [Decisions](./DECISIONS.md) · [Security](./SECURITY.md) · [Failure handling](./FAILURE_HANDLING.md) · [Observability](./OBSERVABILITY.md) · [Deployment](./DEPLOYMENT.md) · [Demo notes](./DEMO_NOTES.md) · [Project plan](./docs/PROJECT_PLAN.md)
