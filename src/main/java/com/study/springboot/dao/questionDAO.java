package com.study.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.springboot.dto.questionDTO;

@Mapper
public interface questionDAO {
	List<questionDTO> q_listDao();

	questionDTO q_viewDao(@Param("q_no") String q_no);

    int q_writeDao(questionDTO dto);

    int q_deleteDao(@Param("q_no") String q_no);

    int q_updateDao(questionDTO dto);
}
