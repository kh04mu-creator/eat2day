package com.study.springboot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class EmailAuthDTO {
	private int e_id;
	private String e_email;
	private String e_code;
	private boolean e_verified;
	private Date expiredat;
	
}
