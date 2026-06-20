package com.orderiq.orderapi.web.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * What we return to clients about a tenant. Note we expose the generated id,
 * active flag, and createdAt — none of which the client could set on creation.
 */
public record TenantResponse(
        UUID id,
        String name,
        boolean active,
        Instant createdAt
) {
}