package com.study.springboot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.springboot.dao.restaurantDAO;
import com.study.springboot.dto.restaurantDTO;

@Service
public class RestaurantSearchService {
	@Autowired
	private RestHighLevelClient client;
	@Autowired
	private restaurantDAO dao;
	
	public Map<String, Object> search(
			@RequestParam("keyword") String keyword,
		    @RequestParam("lat") double lat,
		    @RequestParam("lon") double lon,
		    @RequestParam("page") int page,
		    @RequestParam("size") int size) throws Exception {

        // 1️ 검색 인덱스
        SearchRequest request = new SearchRequest("restaurant");

        // 2️ 검색 본문
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 🔍 키워드 + 거리 필터
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(
                    QueryBuilders.multiMatchQuery(
                        keyword,
                        "category_name",
                        "place_name"
                    )
                )
                .filter(
                    QueryBuilders.geoDistanceQuery("location")
                        .point(lat, lon)
                        .distance("3km")
                );

        builder.query(boolQuery);

        // 3️ 페이징 설정
        builder.from(page * size);
        builder.size(size);

        // ⭐ totalCount 필수
        builder.trackTotalHits(true);

        request.source(builder);
		
		// 4 ES 검색 실행
		SearchResponse response = client.search(request, RequestOptions.DEFAULT);
		
		// 5 결과 → DTO 변환
		List<restaurantDTO> list = new ArrayList<>();
		
		for(SearchHit hit:response.getHits().getHits()) {
			Map<String, Object> source = hit.getSourceAsMap();
			
			restaurantDTO dto = new restaurantDTO();

			// id는 Long
			dto.setId(Long.parseLong(source.get("id").toString()));

			dto.setPlace_name(String.valueOf(source.get("place_name")));
			dto.setAddress_name(String.valueOf(source.get("address_name")));
			dto.setRoad_address_name(String.valueOf(source.get("road_address_name")));

			Object phone = source.get("phone");
			if (phone != null) dto.setPhone(phone.toString());

			// location 처리 그대로 OK
			Map<String, Object> loc = (Map<String, Object>) source.get("location");
			if (loc != null) {
			    Object doclon = loc.get("lon");
			    Object doclat = loc.get("lat");

			    if (doclon instanceof Number && doclat instanceof Number) {
			        dto.setX(String.valueOf(((Number) doclon).doubleValue()));
			        dto.setY(String.valueOf(((Number) doclat).doubleValue()));
			    }
			}

			// 썸네일은 Long으로 조회
			String thumb = dao.selectThumbnailByPlaceId(dto.getId());
			dto.setThumbnailUrl(thumb);

			
		list.add(dto);
	}
		// 6️ 전체 개수
        long totalCount =
                response.getHits().getTotalHits().value;

        // 7️ Map으로 묶기
        Map<String, Object> result = new HashMap<>();
        
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("page", page);
        result.put("size", size);

        return result;
	}
	// 자동완성 + 하이라이트 (category_name 전용)
	   public List<Map<String, String>> autocompleteHighlight(String keyword) throws Exception {

	       SearchRequest request = new SearchRequest("restaurant");
	       SearchSourceBuilder source = new SearchSourceBuilder();

	       // 1️ 자동완성 쿼리 (category_name만)
	       BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
	          //.should(QueryBuilders.matchQuery("category_name.auto", keyword))
	          .should(QueryBuilders.matchPhrasePrefixQuery("category_name", keyword)) 
	           .should(QueryBuilders.matchPhrasePrefixQuery("category_name.auto", keyword))
	           .minimumShouldMatch(1);

	       source.query(boolQuery);
	       source.size(10);
	       

	       // 2️ 하이라이트 설정
	       HighlightBuilder highlightBuilder = new HighlightBuilder()
	           .requireFieldMatch(false)
	           .preTags("<em class=\"highlight\">")
	           .postTags("</em>")
	          .field("category_name")
	          .field("category_name.auto");

	       source.highlighter(highlightBuilder);

	       request.source(source);

	       // 3️ ES 실행
	       SearchResponse response =
	           client.search(request, RequestOptions.DEFAULT);

	       // 4️ 결과 가공
	       List<Map<String, String>> result = new ArrayList<>();

	       for (SearchHit hit : response.getHits().getHits()) {

	           Map<String, Object> src = hit.getSourceAsMap();
	           Map<String, HighlightField> highlight = hit.getHighlightFields();
	           System.out.println("highlight = " + highlight);

	           String categoryName = "";
	           Object raw = src.get("category_name");
	           if (raw != null) {
	               categoryName = raw.toString();
	           }

	           // 하이라이트 적용
	           HighlightField chf = highlight.get("category_name");
	           if (chf == null) {
	               chf = highlight.get("category_name.auto");
	           }
	           if (chf != null && chf.fragments() != null && chf.fragments().length > 0) {
	               categoryName = chf.fragments()[0].string();
	           }


	           Map<String, String> item = new HashMap<>();
	           item.put("category_name", categoryName);

	           result.add(item);
	       }

	       return result;
	   }
	
	
}
