package com.orderiq.orderapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A business unit inside a tenant. A VENDOR_MANAGER sees data scoped to one vendor.
 */
@Entity
@Table(name = "vendors")
@Getter
@Setter
public class Vendor extends TenantAwareEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean active = true;
}