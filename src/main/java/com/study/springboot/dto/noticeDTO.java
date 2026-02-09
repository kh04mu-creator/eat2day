package com.study.springboot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class noticeDTO {
	private String no_no;
	private String no_title;
	private Date no_date;
	private String no_content;
	private String no_upload;
}
