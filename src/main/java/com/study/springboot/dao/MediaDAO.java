package com.study.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.springboot.dto.MediaDTO;

@Mapper
public interface MediaDAO {
    int insertReview(MediaDTO dto);

    MediaDTO selectMediaById(@Param("me_no") Long me_no);

    int deleteMediaById(@Param("me_no") Long me_no);

    List<MediaDTO> selectMediaByPlaceIdAndType(@Param("placeId") Long placeId,
                                               @Param("type") String type);
    List<MediaDTO> selectReviewsByPlace(Long placeId);
}

    

