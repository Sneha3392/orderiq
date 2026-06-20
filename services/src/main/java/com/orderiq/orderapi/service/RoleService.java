package com.orderiq.orderapi.service;

import com.orderiq.orderapi.repository.RoleRepository;
import com.orderiq.orderapi.web.dto.RoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired RoleRepository repo;

    public List<RoleResponse> getAllRoles(){
    return repo.findAll().stream().map(role -> new RoleResponse(role.getId(),role.getName().name())).toList();
    }

}
