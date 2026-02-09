package com.study.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springboot.dao.searchDAO;
import com.study.springboot.dto.searchDTO;

import lombok.RequiredArgsConstructor;

@Service
public class searchService {
	@Autowired
	searchDAO dao;
	@Autowired
	searchESService service;
	
	 @Transactional
	    public void saveKeyword(String keyword) {
		 
	        if (keyword == null || keyword.trim().isEmpty()) return;
	        
	        try {
		        if (dao.existsKeyword(keyword) > 0) {
		        	dao.increaseCount(keyword);
		        	
		        	searchDTO dto = dao.findByKeyword(keyword); // 조회 메서드 필요
		        	service.increaseCount(dto);
		        	
		        } else {
		        	searchDTO dto = new searchDTO();
		            dto.setS_keyword(keyword);
		            dto.setS_count(1);
		        	
		        	dao.insertKeyword(keyword);
		        	service.save(dto);
		        }
	        }catch (Exception e){
	        	e.printStackTrace();
	        }
	    }

	    public List<searchDTO> getTopKeywords() {
	        return dao.topKeywords();
	    }

}
