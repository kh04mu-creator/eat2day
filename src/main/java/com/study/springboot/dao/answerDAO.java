package com.study.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.springboot.dto.AnswerCountDTO;
import com.study.springboot.dto.AnswerLatestDTO;
import com.study.springboot.dto.answerDTO;

@Mapper
public interface answerDAO {
	// 질문번호로 답변 리스트
	List<answerDTO> a_listByQno(@Param("q_no") String q_no);
	
	 // qnaList에서 (답변대기/완료)용: 질문별 답변 개수
    List<AnswerCountDTO> a_countByQno();
    
    // 관리자센터용 
    List<AnswerLatestDTO> a_latestByQuestion();

	answerDTO a_viewDao(@Param("a_no") String a_no);

    int a_writeDao(answerDTO dto);

    int a_deleteDao(@Param("a_no") String a_no);

    int a_updateDao(answerDTO dto);
}
