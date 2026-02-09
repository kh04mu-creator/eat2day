package com.study.springboot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class questionDTO {
	private String q_no;
	private String q_title;
	private Date q_date;
	private String q_content;
	private String q_secret;
	private String m_no;
	private String m_nickname; 
}
