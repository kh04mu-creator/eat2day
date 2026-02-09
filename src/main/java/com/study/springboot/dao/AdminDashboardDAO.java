package com.study.springboot.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDashboardDAO {

    int totalMembers();        // 전체 회원 수
    int todayNewMembers();     // 오늘 가입자 수
    int todayNewPosts();       // 오늘 게시물 수
    int qnaCount();            // QnA 수 (예: 미답변)
}
