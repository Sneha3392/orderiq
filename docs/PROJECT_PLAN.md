# OrderIQ — Project Plan

This plan maps the spec's **Start-to-Finish Deliverable Checklist** to concrete milestones and a working backlog. It is a living document — we update statuses as we go.

Legend: ✅ done · 🟡 in progress · ⚪ not started

---

## Milestones (aligned to the deliverable checklist)

| # | Milestone | Checklist stage | Key deliverables | Status |
| --- | --- | --- | --- | --- |
| M1 | Project planning | Project planning | Repo + mandated structure, README skeleton, this plan, backlog | 🟡 |
| M2 | Architecture baseline | Architecture baseline | `ARCHITECTURE.md`, system / data-flow / deployment / request-flow diagrams | ⚪ |
| M3 | Design decisions | Design decisions | `DECISIONS.md` / ADRs with alternatives and tradeoffs | ⚪ |
| M4 | Implementation baseline | Implementation baseline | Spring Boot structure, config & profiles, validation, exception handling, OpenAPI, sample data | 🟡 |
| M5 | Testing baseline | Testing baseline | JUnit structure, Mockito unit tests, Testcontainers integration tests, CI test run | ⚪ |
| M6 | Operational proof | Operational proof | Logs, traces, metrics, dashboards, failure scenarios, cache & deployment evidence | ⚪ |
| M7 | Final proof pack | Final proof pack | `DEMO_NOTES.md`, API examples, screenshots, green pipeline, K8s status, resume bullets | ⚪ |

> Note: M4 (implementation) was partially started ahead of M2/M3 while learning the
> mechanics. We are now completing M1–M3 properly, then resuming M4 from the backlog.

---

## Feature backlog

### Foundation (Phase 1)
- ✅ Define data model (entities + relationships)
- ✅ Spring Boot project skeleton (Maven, dependencies)
- ✅ Tenancy & identity entities (Tenant, Vendor, User, Role)
- ✅ Flyway V1 migration + seeded roles
- ✅ Local PostgreSQL via Docker Compose
- ✅ `GET /api/roles` (read seeded data)
- 🟡 Tenant CRUD (`POST` + `GET` done; fetch-one, update pending)
- ⚪ Global exception handling (`@RestControllerAdvice`)
- ⚪ Remaining entities: sales network, catalog, orders (V2/V3 migrations)
- ⚪ Config profiles (dev / test)
- ⚪ OpenAPI / Swagger UI
- ⚪ Sample data seeding (≥ 3 tenants)
- ⚪ Security: JWT/OAuth2 resource server, role + tenant enforcement

### AI middleware (Phase 2)
- ⚪ Provider abstraction + mock LLM
- ⚪ Prompt-version tracking & response normalisation
- ⚪ AI audit logging
- ⚪ Redis cache (tenant-aware keys, TTL, hit/miss metrics)
- ⚪ Resilience4j (timeout, retry, circuit breaker, fallback)

### Event-driven async (Phase 3)
- ⚪ Kafka setup + thin events
- ⚪ Report request lifecycle + worker
- ⚪ Idempotency records
- ⚪ Dead-letter queue

### Observability (Phase 4)
- ⚪ Correlation IDs + structured logging
- ⚪ OpenTelemetry tracing across services
- ⚪ Prometheus metrics + Grafana dashboards

### Packaging & shipping (Phase 5)
- ⚪ Dockerfiles for each service
- ⚪ Kubernetes manifests
- ⚪ Terraform / SAM assets
- ⚪ GitHub Actions CI/CD

### Testing (woven through all phases)
- ⚪ Mockito unit tests per service layer
- ⚪ Testcontainers integration tests
- ⚪ Security & failure-path tests

---

## Issue/task board

Track these as GitHub Issues grouped under the milestones above (GitHub → Issues →
assign each to its Milestone). A simple board with **To do / In progress / Done**
columns is enough; the backlog above is the seed list.
