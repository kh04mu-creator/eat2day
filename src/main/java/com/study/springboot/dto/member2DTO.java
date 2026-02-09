package com.study.springboot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class member2DTO {
	private String m_no;
	private String m_email;
	private String m_pw;
	private String m_name;
	private String m_nickname;
	private String m_gender;
	private String m_birth;
	private String m_tel;
	private String m_addr;
	private Date m_date;
	private String m_auth;
	private String m_verified;
	private String m_provider;      // local / google / kakao
	private String m_provider_id;   // 소셜 고유 ID	
}
