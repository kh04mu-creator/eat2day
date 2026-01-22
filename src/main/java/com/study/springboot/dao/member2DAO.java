package com.study.springboot.dao;

import java.util.List;

import com.study.springboot.dto.member2DTO;

public interface member2DAO {
	public List<member2DTO> m_listDao(); // 회원목록 조회
	public member2DTO m_viewDao(int m_no); // 회원정보 상세보기
	public int m_writeDao(member2DTO dto); // 회원가입
	public int m_deleteDao(int m_no); // 회원탈퇴
	public int m_updateDao(member2DTO dto); // 회원정보 수정	
	public member2DTO findByUserId(String userId);
}