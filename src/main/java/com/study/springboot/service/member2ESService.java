package com.study.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.springboot.dto.member2DTO;

@Service
public class member2ESService {

    @Autowired
    private RestHighLevelClient client;

    private static final String INDEX = "member2";

    // ✅ 엘라스틱서치에 저장
    public void save(member2DTO dto) throws Exception {
        if (dto == null || dto.getM_no() == null || dto.getM_no().trim().isEmpty()) {
            throw new IllegalStateException("m_no(회원번호)가 null입니다. DB 저장 후 ES 저장을 호출하세요.");
        }

        // 엘라스틱서치에 저장할 도큐먼트 생성
        Map<String, Object> map = new HashMap<>();
        map.put("m_no", dto.getM_no());
        map.put("m_email", dto.getM_email());
        map.put("m_pw", dto.getM_pw());
        map.put("m_name", dto.getM_name());
        map.put("m_nickname", dto.getM_nickname());
        map.put("m_gender", dto.getM_gender());
        map.put("m_birth", dto.getM_birth());
        map.put("m_tel", dto.getM_tel());
        map.put("m_addr", dto.getM_addr());
        map.put("m_date", dto.getM_date());
        map.put("m_auth", dto.getM_auth());
        map.put("m_verified", dto.getM_verified());
        map.put("m_provider", dto.getM_provider());
        map.put("m_provider_id", dto.getM_provider_id());

        System.out.println("ES 저장 시도: " + dto.getM_no());

        IndexRequest request = new IndexRequest(INDEX)
                .id(dto.getM_no())     // ✅ id는 m_no 그대로 (String이면 toString 불필요)
                .source(map);

        client.index(request, RequestOptions.DEFAULT);

        System.out.println("ES 저장 성공");
    }

    // ✅ 엘라스틱서치에서 삭제 (탈퇴 시 호출)
    public void deleteById(String mNo) throws Exception {
        if (mNo == null || mNo.trim().isEmpty()) {
            return; // 삭제할 id가 없으면 그냥 종료
        }

        System.out.println("ES 삭제 시도: " + mNo);

        DeleteRequest request = new DeleteRequest(INDEX, mNo);
        client.delete(request, RequestOptions.DEFAULT);

        System.out.println("ES 삭제 성공");
    }
}
