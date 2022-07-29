package com.example.jwt_chinh_chien_product_manager.config;

import com.example.jwt_chinh_chien_product_manager.security.jwt.JwtEntryPoint;
import com.example.jwt_chinh_chien_product_manager.security.jwt.JwtTokenFilter;
import com.example.jwt_chinh_chien_product_manager.security.userprincal.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {  // bean ma hoa pass nguoi dung
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() { // cau hinh lai loi nguoi dung khong co quyen truy cap
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override // phan anh chinhs
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().ignoringAntMatchers("/**"); // vo hieu hoa csrf cho 1 so duong nhat dinh
        httpSecurity.cors().and().csrf().disable() // vo hieu hoa csrf (kiem soat quyen truy cap)
                .authorizeRequests().antMatchers("/**", "/login", "/register").permitAll()// tat ca truy cap duoc
                .anyRequest().authenticated() // cac request con lai can xac thuc
                .and().exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // lop filter kiem tra chuoi jwt
    }
//    @Override // phan cua chieens
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().ignoringAntMatchers("/**");// vô hiệu hóa csrf cho một số đường dẫn nhất định
//        http.httpBasic().authenticationEntryPoint(restAuthEntryPoint);//tùy chỉnh lại thông báo 401 thông qua class restEntryPoint
//        http.authorizeRequests()
//                .antMatchers("/login",
//                        "/register", "/**").permitAll() // tất cả truy cập được
//                .anyRequest().authenticated()  // các request còn lại cần xác thực
//                .and().csrf().disable(); // vô hiệu hóa của csrf (kiểm soát quyền truy cập)
//        http.addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) //lớp finter kiểm tra chuỗi jwt
//                .exceptionHandling().accessDeniedHandler(cusTomAccessDeniedHandler()); //xử lí lí ngoại lệ khi không có quyền truy cập
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////http.cors(); // ngăn chặn truy cập từ miền khác
//    }

}
