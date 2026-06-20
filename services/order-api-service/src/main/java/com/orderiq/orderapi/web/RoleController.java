package com.orderiq.orderapi.web;

import com.orderiq.orderapi.service.RoleService;
import com.orderiq.orderapi.web.dto.RoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    RoleService service;

    @GetMapping
    public List<RoleResponse> getRoles(){
    return service.getAllRoles();
    }
}
