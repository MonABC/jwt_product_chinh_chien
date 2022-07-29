package com.example.jwt_chinh_chien_product_manager.repo;

import com.example.jwt_chinh_chien_product_manager.model.Role;
import com.example.jwt_chinh_chien_product_manager.model.RoleName;
import com.example.jwt_chinh_chien_product_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
