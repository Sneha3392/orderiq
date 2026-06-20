package com.orderiq.orderapi.repository;

import com.orderiq.orderapi.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
}