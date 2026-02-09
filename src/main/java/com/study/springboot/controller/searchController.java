package com.study.springboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.service.searchService;

@RestController
@RequestMapping("/search")
public class searchController {
	@Autowired
	searchService service;
	
	@PostMapping("/record")
    @ResponseBody
    public void recordKeyword(@RequestParam("keyword") String keyword) {
		System.out.println("🔥 검색어 수신: " + keyword);
        service.saveKeyword(keyword);
    }
	
	
}
