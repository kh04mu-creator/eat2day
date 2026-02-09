package com.study.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.springboot.dto.searchDTO;
import com.study.springboot.service.searchService;

@Controller
public class HomeController {
	@Autowired
	searchService service;
    
    @GetMapping("/")
	public String home(Model model) {
	    List<searchDTO> topKeywords = service.getTopKeywords();
	    System.out.println("TOP KEYWORDS SIZE: " + topKeywords.size());
	    model.addAttribute("topKeywords", topKeywords);
	    return "home"; // JSP 이름
	}
    
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        System.out.println(keyword); // 한글 정상 출력돼야 함
        return "result";
    }
}

