package com.study.springboot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.springboot.dto.EmailAuthDTO;

@Mapper
public interface EmailAuthDAO {
	void insertAuthCode(EmailAuthDTO dto);
	EmailAuthDTO selectAuthCode(@Param("e_email") String e_email,
            					@Param("e_code") String e_code);
	void verifyEmail(@Param("e_email") String e_email);
	void deleteByEmail(@Param("e_email") String e_email);
	int isEmailVerified(@Param("e_email") String e_email);
}
