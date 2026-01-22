package com.study.springboot.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        // 로그인 아이디 (userId)
        String loginId = authentication.getName();
        session.setAttribute("loginId", loginId);

        // ❗ mno가 필요하면 UserDetails 커스터마이징 필요 (아래 설명)
        // session.setAttribute("loginMno", mno);

        response.sendRedirect("/");
    }
}
