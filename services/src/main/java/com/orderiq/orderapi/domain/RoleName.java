package com.orderiq.orderapi.domain;

/**
 * The fixed set of roles. Three are human users; two are machine identities
 * used for service-to-service calls (AI_SERVICE, ASYNC_WORKER).
 */
public enum RoleName {
    ADMIN,
    VENDOR_MANAGER,
    SALES_REP,
    AI_SERVICE,
    ASYNC_WORKER
}