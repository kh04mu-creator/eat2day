package com.study.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.springboot.dao.questionDAO;

@Controller
public class questionController {
	@Autowired
	questionDAO dao;
	
	@RequestMapping("/member/questionWriteForm")
    public String questionWriteForm() {
        return "member/questionWriteForm";
    }
}
