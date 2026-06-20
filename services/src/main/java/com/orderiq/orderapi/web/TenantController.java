package com.orderiq.orderapi.web;

import com.orderiq.orderapi.service.TenantService;
import com.orderiq.orderapi.web.dto.CreateTenantRequest;
import com.orderiq.orderapi.web.dto.TenantResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    // @RequestBody    -> parse the JSON body into a CreateTenantRequest.
    // @Valid          -> enforce the DTO's validation rules first; a violation
    //                    becomes an automatic 400 Bad Request.
    // ResponseEntity  -> lets us set the 201 Created status explicitly.
    @PostMapping
    public ResponseEntity<TenantResponse> create(@Valid @RequestBody CreateTenantRequest request) {
        TenantResponse created = tenantService.createTenant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<TenantResponse> list() {
        return tenantService.listTenants();
    }
}