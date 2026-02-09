package com.study.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.springboot.dto.searchDTO;

@Mapper
public interface searchDAO {
	public int existsKeyword(@Param("s_keyword") String s_keyword);
	public int insertKeyword(@Param("s_keyword") String s_keyword); //검색어 입력
	public int increaseCount(@Param("s_keyword") String s_keyword); // 검색어 카운트
	public List<searchDTO> topKeywords();
	public searchDTO findByKeyword(@Param("s_keyword") String s_keyword);
	
}
