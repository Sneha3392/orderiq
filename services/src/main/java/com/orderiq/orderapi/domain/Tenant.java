package com.orderiq.orderapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The top-level isolation boundary: one distribution business on the platform.
 */
@Entity
@Table(name = "tenants")
@Getter
@Setter
public class Tenant extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean active = true;
}