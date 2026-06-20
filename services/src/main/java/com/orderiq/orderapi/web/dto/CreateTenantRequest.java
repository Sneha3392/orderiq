package com.orderiq.orderapi.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * The shape of an incoming "create tenant" request. Only fields listed here can
 * be sent by a client. @NotBlank means: not null, and not empty/whitespace-only.
 */
public record CreateTenantRequest(
        @NotBlank(message = "name must not be blank")
        String name
) {
}