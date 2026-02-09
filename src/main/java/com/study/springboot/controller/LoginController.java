package com.study.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/loginForm")
    public String loginForm() {
        return "memberWriteForm";
    }
    
    @PostMapping("/login")
    public String loginSuccess(
        @RequestParam(required = false) String returnUrl
    ) {
        if (returnUrl != null && !returnUrl.isBlank()) {
            return "redirect:" + returnUrl;
        }
        return "redirect:/"; // 기본 이동
    }
    
    
}