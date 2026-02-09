package com.study.springboot.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.springboot.dao.MediaDAO;
import com.study.springboot.dao.restaurantDAO;
import com.study.springboot.dto.MediaDTO;
import com.study.springboot.dto.restaurantDTO;
import com.study.springboot.security.CustomUserDetails;

@Controller
public class PlaceController {

    @Autowired
    private restaurantDAO restaurantDao;

    @Autowired
    private MediaDAO mediaDao;

    /**
     * 왼쪽 검색폼 action="/place" 용
     * - keyword로 검색해서 "첫 번째 결과" 상세로 redirect
     * - 결과 없으면 place_detail 페이지는 띄우되 카드 숨김(showCard=false)
     */
    @GetMapping("/place")
    public String placeSearch(
            @RequestParam(value="keyword", required=false) String keyword,
            @RequestParam(value="lat", required=false) Double lat,
            @RequestParam(value="lon", required=false) Double lon
    ) {
        if (keyword == null) keyword = "";

        String encKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        List<restaurantDTO> placeList = restaurantDao.selectPlaceListByKeyword(keyword);

        if (placeList == null || placeList.isEmpty()) {
            return "redirect:/place/0?keyword=" + encKeyword
                    + "&lat=" + (lat != null ? lat : "")
                    + "&lon=" + (lon != null ? lon : "")
                    + "&card=0";
        }

        Long firstId = placeList.get(0).getId();

        return "redirect:/place/" + firstId
                + "?keyword=" + encKeyword
                + "&lat=" + (lat != null ? lat : "")
                + "&lon=" + (lon != null ? lon : "")
                + "&card=0";
    }

    @GetMapping("/place/{placeId}")
    public String placeDetail(
    		@PathVariable("placeId") Long placeId,
            @RequestParam(value="keyword", required=false) String keyword,
            @RequestParam(value="lat", required=false) Double lat,
            @RequestParam(value="lon", required=false) Double lon,
            @RequestParam(value="card", defaultValue="0") int card,
            Model model,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        //  placeId가 0 같은 더미로 왔을 때(검색 결과 없음 처리용)
        if (placeId == null || placeId <= 0) {
            model.addAttribute("restaurant", null);
            model.addAttribute("images", List.of());
            model.addAttribute("reviews", List.of());
            model.addAttribute("showCard", false);

            // 왼쪽 리스트용
            model.addAttribute("keyword", keyword);
            model.addAttribute("lat", lat);
            model.addAttribute("lon", lon);

            // 권한용
            model.addAttribute("loginMno", user != null ? user.getMno() : null);
            model.addAttribute("isAdmin", user != null && user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

            return "place_detail";
        }

        //  1) 식당
        restaurantDTO restaurant = restaurantDao.selectRestaurantById(placeId);

        // 식당 없으면 카드 숨김 처리
        if (restaurant == null) {
            model.addAttribute("restaurant", null);
            model.addAttribute("images", List.of());
            model.addAttribute("reviews", List.of());
            model.addAttribute("showCard", false);

            model.addAttribute("keyword", keyword);
            model.addAttribute("lat", lat);
            model.addAttribute("lon", lon);

            model.addAttribute("loginMno", user != null ? user.getMno() : null);
            model.addAttribute("isAdmin", user != null && user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
            
         // 2. 리뷰 목록 (여기에 추가!)
            List<MediaDTO> reviews = mediaDao.selectReviewsByPlace(placeId);
            model.addAttribute("reviews", reviews);    

            return "place_detail";
        }

        // 2) 이미지 / 리뷰
        List<MediaDTO> images = mediaDao.selectMediaByPlaceIdAndType(placeId, "image");
        List<MediaDTO> reviews = mediaDao.selectReviewsByPlace(placeId); 

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("images", images);
        model.addAttribute("reviews", reviews);

        // 3) 왼쪽 리스트(restaurantList.js)용
        model.addAttribute("keyword", keyword);
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);

        // 4) 카드 표시 여부
        model.addAttribute("showCard", card == 1);

        // 5) 삭제 권한용
        model.addAttribute("loginMno", user != null ? user.getMno() : null);
        model.addAttribute("isAdmin", user != null && user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        return "place_detail";
    }
}
