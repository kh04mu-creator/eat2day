package com.study.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.springboot.dto.member2DTO;

@Mapper
public interface member2DAO {
	public List<member2DTO> m_listDao(); // 회원목록 조회
	public member2DTO m_viewDao(@Param("m_no") String m_no); // 회원정보 상세보기
	public int m_writeDao(member2DTO dto); // 회원가입
	public int m_deleteDao(@Param("m_no") String m_no); // 회원탈퇴
	public int m_updateDao(member2DTO dto); // 회원정보 수정	
	public member2DTO findByUserId(@Param("userId") String userId);
	void verifyEmail(@Param("m_email") String m_email); //이메일 인증여부
	public int countByNickname(@Param("m_nickname") String m_nickname); //닉네임 중복 검사
	public member2DTO findByProvider(@Param("provider") String provider,
            				@Param("providerId") String providerId);
	public int m_writeSocialDao(member2DTO dto);
	public String nextMemberNo();

}