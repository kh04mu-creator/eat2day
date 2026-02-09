package com.study.springboot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.dao.rouletteDAO;
import com.study.springboot.dto.rouletteDTO;
import com.study.springboot.dto.rouletteResponseDTO;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/roulette")
@RequiredArgsConstructor
public class RouletteController {

    private final rouletteDAO rouletteDAO;

    @GetMapping("/spin")
    public rouletteResponseDTO spin() {
        List<rouletteDTO> list = rouletteDAO.selectAll();
        rouletteDTO random = rouletteDAO.selectRandom();

        // DAO에서 null이 반환될 경우 처리
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("룰렛 항목이 없습니다.");
        }

        if (random == null) {
            random = list.get(0); // 최소 1개라도 있으므로 첫 항목 반환
        }

        rouletteResponseDTO response = new rouletteResponseDTO();
        response.setItems(list.stream().map(rouletteDTO::getRo_keyword).toList());
        response.setResult(random.getRo_keyword());

        return response;
    }
}
