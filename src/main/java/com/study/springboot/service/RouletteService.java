package com.study.springboot.service;

import org.springframework.stereotype.Service;

import com.study.springboot.dao.rouletteDAO;
import com.study.springboot.dto.rouletteDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private final rouletteDAO rouletteDAO;

    public rouletteDTO spin() {
        return rouletteDAO.selectRandom();
    }
}

