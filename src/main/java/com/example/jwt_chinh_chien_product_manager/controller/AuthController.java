package com.example.jwt_chinh_chien_product_manager.controller;

import com.example.jwt_chinh_chien_product_manager.dto.request.SignInForm;
import com.example.jwt_chinh_chien_product_manager.dto.request.SignUpForm;
import com.example.jwt_chinh_chien_product_manager.dto.response.JwtResponse;
import com.example.jwt_chinh_chien_product_manager.dto.response.ResponseMessage;
import com.example.jwt_chinh_chien_product_manager.dto.response.ResponseObject;
import com.example.jwt_chinh_chien_product_manager.model.Role;
import com.example.jwt_chinh_chien_product_manager.model.RoleName;
import com.example.jwt_chinh_chien_product_manager.model.User;
import com.example.jwt_chinh_chien_product_manager.security.jwt.JwtProvider;
import com.example.jwt_chinh_chien_product_manager.security.userprincal.UserPrinciple;
import com.example.jwt_chinh_chien_product_manager.service.IRoleService;
import com.example.jwt_chinh_chien_product_manager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RequestMapping("/api/auth")
@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm) {
        if (userService.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("the user name exited! Please try again"), HttpStatus.OK);
        }
        if (userService.existsByEmail(signUpForm.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("the email exited! Please try again"), HttpStatus.OK);
        }
        if (!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())) {
            return new ResponseEntity<>(new ResponseMessage("password does not match"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(), passwordEncoder.encode(signUpForm.getPassword()), passwordEncoder.encode(signUpForm.getConfirmPassword()));
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(
                            () -> new RuntimeException("Role not found"));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleService.findByName(RoleName.PM).orElseThrow(
                            () -> new RuntimeException("Role not found"));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow(
                            () -> new RuntimeException("Role not found"));
                    roles.add(userRole);
                    break;
            }
        });
        user.setRoles(roles);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "create user success", user)
        );
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
//        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAuthorities())); // x??? l?? gi???ng anh ch??nh. hi???u l?? n???u tr???ng th??i ok th?? s??? kh???i t???o m???t ?????i t?????ng l?? JwtResponse
//        return new ResponseEntity<>(new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAuthorities()),HttpStatus.OK); // x??? l?? nh?? ???? h???c kh???i t???o JwtRespose r???i m???i ?????n tr???ng th??i ok
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "create token success", (new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAuthorities()))));

    }

}
