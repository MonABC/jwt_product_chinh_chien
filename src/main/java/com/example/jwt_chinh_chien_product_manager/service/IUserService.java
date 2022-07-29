package com.example.jwt_chinh_chien_product_manager.service;

import com.example.jwt_chinh_chien_product_manager.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User save (User user);
    User findUserByUsername(String username);
}
