package com.study.springboot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.study.springboot.dto.restaurantDTO;

@Mapper
public interface restaurantDAO {
	// 상세 (RESTAURANT.ID)
    restaurantDTO selectRestaurantById(@Param("id") Long id);
    //전체 리스트
    List<restaurantDTO> selectAllPlaces();
    // 키워드 검색
    List<restaurantDTO> selectPlaceListByKeyword(@Param("keyword") String keyword);
    // 근처 리스트: 지금은 전체 반환(필요하면 거리조건 추가)
    List<restaurantDTO> selectNearby();
    // 썸네일 1장 (MEDIA.PLACE_ID = RESTAURANT.ID)
    String selectThumbnailByPlaceId(Long placeId);
    public List<restaurantDTO> listRestaurant(); //전체 식당 리스트
    //식당 등록
    public int addRestaurant(restaurantDTO dto); 
    public restaurantDTO viewRestaurant(@Param("id") int id); //식당 세부정보
	public int deleteRestaurant(@Param("id") int id); //식당 삭제
	public int updateRestaurant(restaurantDTO dto); //식당정보 수정
	
    
}
