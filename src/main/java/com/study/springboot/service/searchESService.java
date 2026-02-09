package com.study.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.springboot.dto.searchDTO;

@Service
public class searchESService {
	@Autowired
	private RestHighLevelClient client;
	
	// ✅ 검색어 카운트 증가 (update)
    public void increaseCount(searchDTO dto) throws Exception {
    	System.out.println("🔥 ES increaseCount() 진입");

        UpdateRequest request = new UpdateRequest("search", dto.getS_no())
            .script(new Script(
                ScriptType.INLINE,
                "painless",
                "ctx._source.s_count += 1",
                Map.of()
            ))
            // 🔥 문서가 없으면 insert (upsert)
            .upsert(Map.of(
                "s_no", dto.getS_no(),
                "s_keyword", dto.getS_keyword(),
                "s_count", dto.getS_count()
            ));

        client.update(request, RequestOptions.DEFAULT);
    }
	
	// 엘라스틱서치에 저장
		public void save(searchDTO dto) throws Exception{
			System.out.println("🔥 ES save() 진입");
			// ID검증(null 체크)(필수X)
			if(dto.getS_no() == null) {
				throw new IllegalStateException("search 문서 ID(s_no)가 null입니다.");
			}
			
			// 엘라스틱서치에 저장할 도큐먼트 생성
			Map<String,Object> map = new HashMap<>();
			map.put("s_no", dto.getS_no());
			map.put("s_keyword", dto.getS_keyword());
			map.put("s_count", dto.getS_count());
			
			// IndextRequest 생성해서 저장
			IndexRequest request = new IndexRequest("search")
					.id(dto.getS_no())
					.source(map);
			
			// 엘라스틱서치 인덱싱
			client.index(request, RequestOptions.DEFAULT);
		}
		
}
