package com.study.springboot.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MediaDTO {
	private Long me_no;
    private Long placeId;     // RESTAURANT의 ID (숫자!)
    private String type;
    private String content;
    private BigDecimal rating;    // NUMBER(3,1)
    private String writer;    // WRITER와 매칭
    private String writerNickname; // 화면용
	
}
