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
//        http.csrf().ignoringAntMatchers("/**");// v?? hi???u h??a csrf cho m???t s??? ???????ng d???n nh???t ?????nh
//        http.httpBasic().authenticationEntryPoint(restAuthEntryPoint);//t??y ch???nh l???i th??ng b??o 401 th??ng qua class restEntryPoint
//        http.authorizeRequests()
//                .antMatchers("/login",
//                        "/register", "/**").permitAll() // t???t c??? truy c???p ???????c
//                .anyRequest().authenticated()  // c??c request c??n l???i c???n x??c th???c
//                .and().csrf().disable(); // v?? hi???u h??a c???a csrf (ki???m so??t quy???n truy c???p)
//        http.addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) //l???p finter ki???m tra chu???i jwt
//                .exceptionHandling().accessDeniedHandler(cusTomAccessDeniedHandler()); //x??? l?? l?? ngo???i l??? khi kh??ng c?? quy???n truy c???p
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////http.cors(); // ng??n ch???n truy c???p t??? mi???n kh??c
//    }

}
