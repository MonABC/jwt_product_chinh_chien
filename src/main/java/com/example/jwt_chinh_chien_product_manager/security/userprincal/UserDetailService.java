package com.example.jwt_chinh_chien_product_manager.security.userprincal;

import com.example.jwt_chinh_chien_product_manager.model.User;
import com.example.jwt_chinh_chien_product_manager.repo.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private IUserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() ->new UsernameNotFoundException("user not found -> username password" + username));
        return UserPrinciple.build(user);
    }
}
