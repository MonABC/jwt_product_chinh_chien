package com.example.jwt_chinh_chien_product_manager.repo;

import com.example.jwt_chinh_chien_product_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepo extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
        Boolean existsByUsername(String username);
        Boolean existsByEmail(String email);

        User findUserByUsername(String username);
}
