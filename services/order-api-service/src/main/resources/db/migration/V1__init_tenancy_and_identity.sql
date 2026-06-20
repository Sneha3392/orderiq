-- V1__init_tenancy_and_identity.sql
-- First slice of the OrderIQ schema: the multi-tenancy backbone and identity.
-- Flyway runs this once, in order, before Hibernate validates the entities.

create table tenants (
    id          uuid         primary key,
    name        varchar(255) not null,
    active      boolean      not null default true,
    created_at  timestamptz  not null,
    updated_at  timestamptz  not null
);

create table vendors (
    id          uuid         primary key,
    tenant_id   uuid         not null references tenants (id),
    name        varchar(255) not null,
    active      boolean      not null default true,
    created_at  timestamptz  not null,
    updated_at  timestamptz  not null
);
-- Index the tenant key: almost every business query filters on it.
create index idx_vendors_tenant on vendors (tenant_id);

create table roles (
    id          uuid        primary key,
    name        varchar(50) not null unique,
    created_at  timestamptz not null,
    updated_at  timestamptz not null
);

create table users (
    id            uuid         primary key,
    tenant_id     uuid         not null references tenants (id),
    email         varchar(320) not null,
    password_hash varchar(255) not null,
    created_at    timestamptz  not null,
    updated_at    timestamptz  not null,
    -- Email is unique within a tenant, not across the whole table.
    constraint uq_users_tenant_email unique (tenant_id, email)
);
create index idx_users_tenant on users (tenant_id);

create table user_roles (
    user_id uuid not null references users (id) on delete cascade,
    role_id uuid not null references roles (id),
    primary key (user_id, role_id)
);

-- Seed the fixed role set. gen_random_uuid() is built into PostgreSQL 13+.
insert into roles (id, name, created_at, updated_at) values
    (gen_random_uuid(), 'ADMIN',          now(), now()),
    (gen_random_uuid(), 'VENDOR_MANAGER', now(), now()),
    (gen_random_uuid(), 'SALES_REP',      now(), now()),
    (gen_random_uuid(), 'AI_SERVICE',     now(), now()),
    (gen_random_uuid(), 'ASYNC_WORKER',   now(), now());