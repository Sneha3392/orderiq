# OrderIQ — Architecture

> Status: **baseline complete**. Diagrams are embedded as Mermaid (rendered by GitHub) so they stay in version control and diff cleanly.

This document explains what OrderIQ is, how its parts fit together, and how data and requests move through it.

---

## 1. System overview

OrderIQ is three cooperating Spring Boot services around an FMCG order database. The `order-api-service` is the secured front door; the `ai-middleware-service` handles AI predictions; the `async-worker-service` does slow background work driven by events. PostgreSQL is the shared system of record; Redis caches AI results; Kafka carries asynchronous events.

```mermaid
flowchart TD
    client["Client apps"]
    api["order-api-service<br/>secured REST gateway"]
    ai["ai-middleware-service<br/>cache, call LLM, audit"]
    worker["async-worker-service<br/>background report jobs"]
    kafka[["Kafka + DLQ"]]
    db[("PostgreSQL")]
    redis[("Redis")]
    llm["LLM provider<br/>mock or real"]
    store[("Report store")]

    client --> api
    api --> ai
    api -- publish --> kafka
    kafka -- consume --> worker
    ai --> redis
    ai --> llm
    ai --> db
    api --> db
    worker --> db
    worker --> store
```

## 2. Component responsibilities

| Service | Responsibilities |
| --- | --- |
| **order-api-service** | Authentication (JWT/OAuth2), tenant-aware CRUD for the business domain, request validation, exception handling, publishing async events. |
| **ai-middleware-service** | LLM provider abstraction, context hydration from the DB, Redis caching, resilient provider calls (timeout/retry/circuit breaker/fallback), response normalisation, AI audit logging. |
| **async-worker-service** | Consumes events, generates reports, manages a status lifecycle, enforces idempotency, and routes poison messages to a dead-letter queue. |

## 3. Data model

The schema groups into four concerns: tenancy & identity, the sales network, the catalog, and transactions. Every business table carries a `tenant_id` (the isolation key). Three operational tables (`report_requests`, `ai_audit_logs`, `idempotency_records`) are intentionally loosely coupled and are added in later phases.

```mermaid
erDiagram
    tenants ||--o{ vendors : owns
    tenants ||--o{ users : has
    users }o--o{ roles : assigned
    vendors ||--o{ customers : serves
    customers ||--o{ outlets : operates
    routes ||--o{ outlets : groups
    sales_reps ||--o{ routes : covers
    sales_reps ||--o| users : "logs in as"
    categories ||--o{ products : contains
    customers ||--o{ orders : places
    outlets ||--o{ orders : receives
    sales_reps ||--o{ orders : books
    orders ||--o{ order_items : contains
    products ||--o{ order_items : "listed in"
    tenants { uuid id PK }
    vendors { uuid id PK }
    users { uuid id PK }
    roles { uuid id PK }
    customers { uuid id PK }
    outlets { uuid id PK }
    routes { uuid id PK }
    sales_reps { uuid id PK }
    categories { uuid id PK }
    products { uuid id PK }
    orders { uuid id PK }
    order_items { uuid id PK }
```

**Multi-tenancy:** the current baseline uses a shared schema with a `tenant_id` discriminator column. Schema-per-vendor is a documented alternative (see `DECISIONS.md`).

## 4. Data-flow view

Which service reads and writes which store:

```mermaid
flowchart LR
    api["order-api"] -->|writes tenants, orders| db[("PostgreSQL")]
    ai["ai-middleware"] -->|reads order history| db
    ai -->|writes ai_audit_logs| db
    ai <-->|cache get/put| redis[("Redis")]
    worker["async-worker"] -->|reads/writes report_requests| db
    worker -->|writes report files| store[("Report store")]
```

## 5. Synchronous prediction flow

A prediction is answered while the caller waits. The cache is checked first; on a miss we hydrate context, call the provider behind a resilience layer, normalise, audit, and cache.

```mermaid
sequenceDiagram
    participant C as Client
    participant API as order-api
    participant AI as ai-middleware
    participant R as Redis
    participant DB as PostgreSQL
    participant L as LLM provider
    C->>API: POST /predict (with JWT)
    API->>AI: prediction request
    AI->>R: cache lookup
    alt cache hit
        R-->>AI: cached result
    else cache miss
        AI->>DB: hydrate order history
        AI->>L: call provider (timeout/retry/breaker)
        L-->>AI: raw response
        AI->>AI: normalise + write audit log
        AI->>R: store result (TTL)
    end
    AI-->>API: normalised prediction
    API-->>C: 200 OK
```

## 6. Asynchronous report flow

Slow work is not done inline. The API records the request, publishes a thin event (IDs only), and returns immediately. The worker does the heavy lifting and advances the status.

```mermaid
sequenceDiagram
    participant C as Client
    participant API as order-api
    participant DB as PostgreSQL
    participant K as Kafka
    participant W as async-worker
    participant S as Report store
    C->>API: POST /reports
    API->>DB: save report_request (REQUESTED)
    API->>K: publish thin event {requestId}
    API-->>C: 202 Accepted
    K->>W: deliver event
    W->>DB: idempotency check, set PROCESSING
    W->>DB: hydrate report data
    W->>S: write report file
    W->>DB: set COMPLETED
    Note over W,K: repeated failure routes the message to the DLQ
```

## 7. Cross-cutting concerns

- **Caching** — Redis stores AI results under tenant-aware keys with a TTL; a cache miss falls through to the provider; if Redis is down the system degrades rather than fails.
- **Resilience** — Resilience4j wraps provider calls with timeout, retry, circuit breaker, and fallback. (See `FAILURE_HANDLING.md`.)
- **Observability** — correlation IDs thread through logs; OpenTelemetry traces span services; Prometheus scrapes metrics; Grafana visualises them. (See `OBSERVABILITY.md`.)
- **Security** — JWT/OAuth2 resource server; role-based access on endpoints; tenant-scoped authorization on every query. (See `SECURITY.md`.)

## 8. Deployment view

Locally everything runs as containers via Docker Compose. For orchestration, each service is deployed as a Kubernetes Deployment fronted by Services and an Ingress.

```mermaid
flowchart TB
    subgraph Local["Local — Docker Compose"]
        lc1["order-api"]
        lc2["postgres"]
        lc3["redis"]
        lc4["kafka"]
    end
    subgraph Cluster["Kubernetes"]
        ing["Ingress"]
        s1["order-api Deployment"]
        s2["ai-middleware Deployment"]
        s3["async-worker Deployment"]
        ing --> s1
        ing --> s2
    end
```

Infrastructure-as-code (Terraform / AWS SAM) for cloud resources lives in `infra/`. (See `DEPLOYMENT.md`.)

## 9. Technology choices

Java 21, Spring Boot 3.5.x, Maven, PostgreSQL + Spring Data JPA, Flyway, Redis, Kafka, Resilience4j, OpenTelemetry/Prometheus/Grafana, Docker, Kubernetes, Terraform/SAM, GitHub Actions. The reasoning and alternatives for each significant choice are recorded in `DECISIONS.md`.
