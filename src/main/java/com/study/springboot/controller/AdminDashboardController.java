package com.study.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.study.springboot.dao.AdminDashboardDAO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardDAO dashboardDAO;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {

        model.addAttribute("totalMembers", dashboardDAO.totalMembers());
        model.addAttribute("todayNewMembers", dashboardDAO.todayNewMembers());
        model.addAttribute("todayNewPosts", dashboardDAO.todayNewPosts());
        model.addAttribute("qnaCount", dashboardDAO.qnaCount());

        return "admin/dashboard";
    }
}
