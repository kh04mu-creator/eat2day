package com.study.springboot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Media {
	private String placeId;
	private String type;     // image / review
	private String content;  // 이미지 경로 or 리뷰 텍스트
	private Integer rating;
	private String writer;
	
}
