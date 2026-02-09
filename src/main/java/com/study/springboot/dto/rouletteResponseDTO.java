package com.study.springboot.dto;

import java.util.List;

import lombok.Data;

@Data
public class rouletteResponseDTO {
	private List<String> items;
    private String result;
}
