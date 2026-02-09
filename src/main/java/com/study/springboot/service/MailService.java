package com.study.springboot.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springboot.dao.EmailAuthDAO;
import com.study.springboot.dto.EmailAuthDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	@Autowired
	JavaMailSender mailSender;
	@Autowired
	EmailAuthDAO dao;
	
	// 인증 메일 발송
	@Transactional
	public void sendAuthMail(String e_email) {
		// 기존 인증 정보 삭제(재발송 대비)
		dao.deleteByEmail(e_email);
		
		// 이메일 미입력시 에러
		if (e_email == null || e_email.trim().isEmpty()) {
	        throw new IllegalArgumentException("이메일 없음");
	    }
		
		// 인증번호 생성
		String code = String.valueOf(
			    (int)(Math.random() * 900000) + 100000
			);
		
		// 인증번호 발송
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("aksenaksen41@gmail.com");
		message.setTo(e_email);
		message.setSubject("[회원가입] 이메일 인증번호 안내");
		message.setText(
				"회원가입을 위한 인증번호입니다.\n\n"
				+"인증번호: " + code + "\n\n"
				+"* 5분 이내에 입력해주세요."
				);
		try {
		    mailSender.send(message);
		    System.out.println("이메일 발송 성공");

		    EmailAuthDTO dto = new EmailAuthDTO();
			dto.setE_email(e_email);
			dto.setE_code(code);
			dto.setExpiredat(
					Timestamp.valueOf(LocalDateTime.now().plusMinutes(5))
					);
			
			dao.insertAuthCode(dto);
		} catch (Exception e) {
		    throw new RuntimeException("이메일 발송 실패", e);
		}
	}
	
	// 인증번호 검증
	@Transactional
	public boolean verifyAuthCode(String e_email, String e_code) {

		EmailAuthDTO dto = dao.selectAuthCode(e_email, e_code);
		
		//인증 실패
		if(dto == null) {
			return false;
		}
		
		// 인증 성공
		dao.verifyEmail(e_email);
		return true;
		
	}
	
	//이메일 인증 여부 확인
	public boolean isEmailVerified(String e_email) {
		return dao.isEmailVerified(e_email) > 0;
	}
	
	
	
	
	
	
}
