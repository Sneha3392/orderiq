package com.orderiq.orderapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Role definitions are global (shared across tenants), so this extends
 * BaseEntity, not TenantAwareEntity. Stored as the enum NAME, never its ordinal,
 * so reordering the enum can never silently corrupt existing rows.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleName name;
}