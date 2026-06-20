package com.orderiq.orderapi.repository;

import com.orderiq.orderapi.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface RoleRepository extends JpaRepository<Role, UUID> {
}