package com.study.springboot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class restaurantService {
	@Autowired
	RestaurantSearchService esService;
	
	public Map<String, Object> search(
			@RequestParam("keyword") String keyword,
			@RequestParam("lat") double lat,
			@RequestParam("lon") double lon,
			@RequestParam(value = "page", defaultValue = "0") int page,
		    @RequestParam(value = "size", defaultValue = "10") int size
			) throws Exception{
		return esService.search(keyword, lat, lon, page, size);
	}
	//자동완성 + 하이라이트
    public List<Map<String,String>> autocomplete(String keyword) throws Exception{
       return esService.autocompleteHighlight(keyword);
    }
}
