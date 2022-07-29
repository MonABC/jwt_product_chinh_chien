package com.example.jwt_chinh_chien_product_manager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {
    private String name;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private Set<String> roles;
}
