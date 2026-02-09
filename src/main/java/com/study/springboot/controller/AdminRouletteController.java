package com.study.springboot.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.springboot.dao.rouletteDAO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminRouletteController {

    private final rouletteDAO rouletteDAO;

    @GetMapping("/rouletteList")
    public String rouletteList(Model model) {
        model.addAttribute("list", rouletteDAO.selectAll());
        return "admin/rouletteList";
    }

    @PostMapping("/rouletteInsert")
    public String rouletteInsert(@RequestParam("ro_keyword") String ro_keyword) {
        ro_keyword = (ro_keyword == null) ? "" : ro_keyword.trim();
        if (ro_keyword.isEmpty()) return "redirect:/admin/rouletteList";
        rouletteDAO.insert(ro_keyword);
        return "redirect:/admin/rouletteList";
    }

    @PostMapping("/rouletteUpdate")
    public String rouletteUpdate(@RequestParam("ro_no") String ro_no,
                                 @RequestParam("ro_keyword") String ro_keyword) {
        ro_keyword = (ro_keyword == null) ? "" : ro_keyword.trim();
        if (ro_keyword.isEmpty()) return "redirect:/admin/rouletteList";
        rouletteDAO.update(ro_no, ro_keyword);
        return "redirect:/admin/rouletteList";
    }

    @PostMapping("/rouletteDelete")
    public String rouletteDelete(@RequestParam("ro_no") String ro_no) {
        rouletteDAO.delete(ro_no);
        return "redirect:/admin/rouletteList";
    }
}
