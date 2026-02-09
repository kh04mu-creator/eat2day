package com.study.springboot.dto;

import lombok.Data;

@Data
public class restaurantDTO {
	private String address_name; 
	private String category_group_code; 
	private String category_group_name; 
	private String category_name;
	private int distance; 
	private Long id;
	private String phone;
	private String place_name; 
	private String place_url; 
	private String road_address_name;
	private String x;
	private String y;
	private String thumb;
	
	//썸네일
	private String thumbnailUrl;
}
