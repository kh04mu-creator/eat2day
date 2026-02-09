package com.study.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.springboot.dao.member2DAO;
import com.study.springboot.dto.member2DTO;

@Service
public class member2Service {

    @Autowired
    private member2DAO dao;

    @Autowired
    private member2ESService esService;

    public int countByNickname(String nickname) {
        return dao.countByNickname(nickname);
    }

    // 닉네임 사용 가능 여부
    public boolean isNicknameAvailable(String m_nickname) {
        System.out.println("닉네임 체크: [" + m_nickname + "]");
        int count = dao.countByNickname(m_nickname.trim());
        System.out.println("중복 개수: " + count);
        return count == 0;
    }

    // ✅ 회원가입: DB 저장 후 ES 저장
    public void write(member2DTO dto) throws Exception {

        // 1) m_no 먼저 생성해서 dto에 세팅
        String newNo = dao.nextMemberNo();
        dto.setM_no(newNo);

        // 2) DB 저장
        dao.m_writeDao(dto);

        // 3) ES 저장
        esService.save(dto);
    }

    
 // ✅ 회원탈퇴: DB 삭제 후 ES 삭제
    public int delete(String m_no) throws Exception {

        int result = dao.m_deleteDao(m_no); // DB 삭제

        if (result > 0) {
            try {
                esService.deleteById(m_no); // ES 삭제
            } catch (Exception e) {
                System.out.println("ES 삭제 실패(로그만): " + e.getMessage());
            }
        }
        return result;
    }

}
