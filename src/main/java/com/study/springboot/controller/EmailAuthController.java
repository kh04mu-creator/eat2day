package com.study.springboot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.springboot.dao.EmailAuthDAO;
import com.study.springboot.dto.EmailAuthDTO;
import com.study.springboot.service.MailService;

@Controller
public class EmailAuthController {
	@Autowired
	MailService service;
	@Autowired
	EmailAuthDAO dao;
	
	@PostMapping("/email/send")
	@ResponseBody
	public ResponseEntity<String> sendEmail(@RequestParam("e_email") String e_email) {
	    if (e_email == null || e_email.trim().isEmpty()) {
	        return ResponseEntity.badRequest().body("이메일이 비어 있습니다.");
	    }

	    try {
	        System.out.println("이메일 인증 요청: " + e_email);
	        service.sendAuthMail(e_email);
	        return ResponseEntity.ok("OK");
	    } catch (Exception e) {
	        e.printStackTrace(); // ★ 여기 찍히는 게 진짜 원인
	        return ResponseEntity.internalServerError().body("FAIL");
	    }
	}

	
	@PostMapping("/email/verify")
	@ResponseBody
	public ResponseEntity<String> verifyEmail(
			@RequestParam("e_email") String e_email,
			@RequestParam("e_code") String e_code) {
		
		boolean result = service.verifyAuthCode(e_email, e_code);

	    if (!result) {
	        return ResponseEntity.badRequest().body("FAIL");
	    }

	    return ResponseEntity.ok("OK");
	}
}
