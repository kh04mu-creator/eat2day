package com.study.springboot.service;

import java.math.BigDecimal;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.springboot.dao.MediaDAO;
import com.study.springboot.dao.member2DAO;
import com.study.springboot.dao.restaurantDAO;
import com.study.springboot.dto.MediaDTO;
import com.study.springboot.security.CustomUserDetails;

@Service
public class ReviewService {

    private final MediaDAO mediaDao;
    private final restaurantDAO restaurantDao;
    private final member2DAO member2Dao;

    public ReviewService(MediaDAO mediaDao, restaurantDAO restaurantDao, member2DAO member2Dao) {
        this.mediaDao = mediaDao;
        this.restaurantDao = restaurantDao;
        this.member2Dao = member2Dao;
    }

    @Transactional
    public void addReview(Long placeId, String content, BigDecimal rating, String writerMno) {

        MediaDTO dto = new MediaDTO();
        dto.setPlaceId(placeId);
        dto.setType("review");
        dto.setContent(content);
        dto.setRating(rating);

        // writer는 mno(회원번호)로 저장
        dto.setWriter(writerMno);

        mediaDao.insertReview(dto);
    }
    @Transactional
    public void deleteReview(Long me_no, CustomUserDetails user) {
    	if (user == null) throw new AccessDeniedException("로그인 필요");
    	
        MediaDTO review = mediaDao.selectMediaById(me_no);
        if (review == null) throw new IllegalArgumentException("리뷰 없음: " + me_no);
        

        boolean isAdmin = user.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // 관리자면 무조건 삭제 OK
        if (isAdmin) {
            mediaDao.deleteMediaById(me_no);
            return;
        }

        // 본인 체크 (writer = mno)
        if (!user.getMno().equals(review.getWriter())) {
            throw new AccessDeniedException("본인 리뷰만 삭제 가능");
        }

        mediaDao.deleteMediaById(me_no);
    }

}
