package com.study.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.springboot.dto.noticeDTO;

@Mapper
public interface noticeDAO {

    List<noticeDTO> no_listDao();

    noticeDTO no_viewDao(@Param("no_no") String no_no);

    int no_writeDao(noticeDTO dto);

    int no_deleteDao(@Param("no_no") String no_no);

    int no_updateDao(noticeDTO dto);
}

