package com.study.springboot.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.springboot.security.CustomUserDetails;
import com.study.springboot.service.ReviewService;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 1) 회원만 작성 가능 (writer = mno)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/review/write")
    public String write(
            @RequestParam("placeId") Long placeId,
            @RequestParam("content") String content,
            @RequestParam(value="rating", required=false) BigDecimal rating,

            // ✅ 리스트 유지용
            @RequestParam(value="keyword", required=false) String keyword,
            @RequestParam(value="lat", required=false) Double lat,
            @RequestParam(value="lon", required=false) Double lon,
            @RequestParam(value="card", defaultValue="1") int card,

            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) return "redirect:/loginForm";
        if (rating == null) rating = BigDecimal.ZERO;

        reviewService.addReview(placeId, content, rating, user.getMno());

        String k = keyword == null ? "" : URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String la = (lat == null) ? "" : String.valueOf(lat);
        String lo = (lon == null) ? "" : String.valueOf(lon);
        
        

        return "redirect:/place/" + placeId
                + "?keyword=" + URLEncoder.encode(keyword == null ? "" : keyword, StandardCharsets.UTF_8)
                + "&lat=" + (lat == null ? "" : lat)
                + "&lon=" + (lon == null ? "" : lon)
                + "&card=" + card;
    }
    
    

    // 2) 본인만 삭제 가능 + 3) ADMIN은 모두 삭제 가능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/review/delete")
    public String delete(
            @RequestParam("me_no") Long me_no,
            @RequestParam("placeId") Long placeId,

            // ✅ 삭제 후에도 리스트 유지용 (이게 핵심)
            @RequestParam(value="keyword", required=false) String keyword,
            @RequestParam(value="lat", required=false) Double lat,
            @RequestParam(value="lon", required=false) Double lon,
            @RequestParam(value="card", defaultValue="1") int card,

            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) return "redirect:/loginForm";

        reviewService.deleteReview(me_no, user);

        String k = keyword == null ? "" : URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String la = (lat == null) ? "" : String.valueOf(lat);
        String lo = (lon == null) ? "" : String.valueOf(lon);

        return "redirect:/place/" + placeId
                + "?keyword=" + k
                + "&lat=" + la
                + "&lon=" + lo
                + "&card=" + card;
    }
}
