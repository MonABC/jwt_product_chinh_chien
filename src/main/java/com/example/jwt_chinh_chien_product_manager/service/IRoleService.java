package com.example.jwt_chinh_chien_product_manager.service;

import com.example.jwt_chinh_chien_product_manager.model.Role;
import com.example.jwt_chinh_chien_product_manager.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
