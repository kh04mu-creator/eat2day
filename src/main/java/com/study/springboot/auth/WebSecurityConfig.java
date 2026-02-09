package com.study.springboot.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.study.springboot.service.CustomOAuth2UserService;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public WebSecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(request -> request
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()

                // 비로그인 접근 허용
                .requestMatchers(
                    "/",
                    "/home",
                    "/roulette/**",
                    "/spin",
                    "/loginForm",
                    "/login",
                    "/logout",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/img/**",
                    "/upload/**",
                    "/memberWriteForm",
                    "/memberWrite",
                    "/jusoPopup/**",
                    "/place/**",
                    "/search/**",
                    "/searchList/**",
                    "/searchKeyword",
                    "/autocomplete",
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/email/**",
                    "/member/checkNickname",
                    "/member/checkEmail",
                    "/debug/**"
                ).permitAll()

                // USER 권한
                .requestMatchers(                  
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
            	    .successHandler((req, res, auth) -> {
            	        System.out.println("✅ LOGIN SUCCESS: " + auth.getName());
            	        res.sendRedirect("/");
            	    })
            	    .failureHandler((req, res, ex) -> {
            	        System.out.println("❌ LOGIN FAIL: " + ex.getClass().getSimpleName() + " / " + ex.getMessage());
            	        res.sendRedirect("/loginForm?error");
            	    })
            	    .permitAll()
            	)


            // ✅ 소셜 로그인 (카카오/구글) + CustomOAuth2UserService 연결
            .oauth2Login(oauth -> oauth
                .loginPage("/loginForm")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .defaultSuccessUrl("/", true)
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
