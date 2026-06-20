package com.orderiq.orderapi.service;

import com.orderiq.orderapi.domain.Tenant;
import com.orderiq.orderapi.repository.TenantRepository;
import com.orderiq.orderapi.web.dto.CreateTenantRequest;
import com.orderiq.orderapi.web.dto.TenantResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    // @Transactional: the whole method runs as one database transaction.
    // If anything throws, every change rolls back — no half-written rows.
    @Transactional
    public TenantResponse createTenant(CreateTenantRequest request) {
        Tenant tenant = new Tenant();
        tenant.setName(request.name());
        // active defaults to true; id, createdAt, updatedAt are filled in for us.
        Tenant saved = tenantRepository.save(tenant);
        return toResponse(saved);
    }

    // readOnly = true is a hint that lets Hibernate skip change-tracking on reads.
    @Transactional(readOnly = true)
    public List<TenantResponse> listTenants() {
        return tenantRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private TenantResponse toResponse(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.isActive(),
                tenant.getCreatedAt());
    }
}