package com.orderiq.orderapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Every business entity that belongs to a single tenant extends this.
 * The tenant_id column is the isolation key: all queries must filter on it.
 * (Tenant itself does NOT extend this — it is the tenant.)
 */
@MappedSuperclass
@Getter
@Setter
public abstract class TenantAwareEntity extends BaseEntity {

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;
}