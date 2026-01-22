package com.study.springboot.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                
                // 비로그인 접근 허용
                .requestMatchers(
                        "/",
                        "/loginForm",
                        "/login",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ).permitAll()
                
                
                // USER 권한
                .requestMatchers(
                    "/review/**",
                    "/review/reviewWrite",
                    "/review/likeToggle",
                    "/member/**"                   
                ).hasAnyRole("USER", "ADMIN")
                
                
                // ADMIN 권한
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )

           
            .formLogin(login -> login
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .usernameParameter("userId")
                .passwordParameter("userPw")
                .successHandler(new LoginSuccessHandler())
                .defaultSuccessUrl("/", true)
                .failureUrl("/loginForm?error")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/loginForm")
                .invalidateHttpSession(true)
            );

   
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
