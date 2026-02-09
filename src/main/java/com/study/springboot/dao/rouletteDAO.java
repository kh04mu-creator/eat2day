package com.study.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.springboot.dto.rouletteDTO;

@Mapper
public interface rouletteDAO {
	List<rouletteDTO> selectAll();
    rouletteDTO selectRandom();
    
 // 관리자: 추가/수정/삭제
    int insert(@Param("ro_keyword") String ro_keyword);
    int update(@Param("ro_no") String ro_no, @Param("ro_keyword") String ro_keyword);
    int delete(@Param("ro_no") String ro_no);
}
