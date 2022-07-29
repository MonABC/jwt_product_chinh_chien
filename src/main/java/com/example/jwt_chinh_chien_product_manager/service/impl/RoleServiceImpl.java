package com.example.jwt_chinh_chien_product_manager.service.impl;

import com.example.jwt_chinh_chien_product_manager.model.Role;
import com.example.jwt_chinh_chien_product_manager.model.RoleName;
import com.example.jwt_chinh_chien_product_manager.repo.IRoleRepo;
import com.example.jwt_chinh_chien_product_manager.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleRepo roleRepo;
    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepo.findByName(name);
    }
}
