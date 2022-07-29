package com.example.jwt_chinh_chien_product_manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 6, max = 60)
    private String name;
    @NotBlank
    @Size(min = 6, max = 60)
    private String username;
    @NotBlank
    @Size(max = 60)
    @Email
    private String email;
    @NotBlank
    @JsonIgnore
    @Size(min = 6, max = 60)
    private String password;
    @NotBlank
    @JsonIgnore
    @Size(min = 6, max = 60)
    private String confirmPassword;
    @Lob
    private String avatar;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    public User(String name, String username, String email, String password, String confirmPassword, String avatar, Set<Role> roles) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.avatar = avatar;
        this.roles = roles;
    }

    public User(@NotBlank @Size(min = 6, max = 60) String name,
                @NotBlank @Size(min = 6, max = 60) String username,
                @NotBlank @Size(max = 60) @Email String email,
                @NotBlank @Size(min = 6, max = 60) String encode,
                @NotBlank @Size(min = 6, max = 60) String encode1) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = encode;
        this.confirmPassword = encode1;
    }
}
