<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Place Detail</title>

<c:set var="path" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="${path}/css/place_detail.css">
</head>

<body>
<!--  페이지 전환 오버레이 (로고 로딩) -->
<div id="page-transition" class="enter" aria-hidden="true"
     style="opacity:1; background:#fff4ec;">
  <div class="pt-wrap">
    <img src="${path}/images/logonew.png" class="pt-logo" alt="eat2day">
    <div class="pt-dots" aria-hidden="true">
      <span></span><span></span><span></span>
    </div>
  </div>
</div>

<div id="page-transition"></div>

<script>window.APP_PATH = "${path}";</script>

<div id="layout">

  <!-- restaurantList.js가 읽는 hidden (id 필수, 1번만 유지) -->
  <input type="hidden" id="keyword" value="${keyword}">
  <input type="hidden" id="lat" value="${empty lat ? '' : lat}">
  <input type="hidden" id="lon" value="${empty lon ? '' : lon}">

  <!-- ===== Left panel ===== -->
  <div id="search-panel">
    <div class="header-container">
      <a class="nav-link" href="${path}/">
        <div class="logo-wrap1">
          <img src="${path}/images/logonew.png" alt="eat2day2" class="sidebar-logo1">
        </div>
      </a>

      <form id="placeDetailSearchForm"
            class="search-header"
            method="get"
            action="${path}/place">
        <input type="text" name="keyword" placeholder="검색어" value="${keyword}">
        <input type="hidden" name="lat" id="pd-lat" value="${empty lat ? '' : lat}">
        <input type="hidden" name="lon" id="pd-lon" value="${empty lon ? '' : lon}">
        <button type="submit">검색</button>
      </form>
    </div>

    <!-- 리스트/페이징: restaurantList.js가 렌더 (table/tbody 제거) -->
    <div class="place-list">
      <div id="resultBody" class="result-body"></div>
      <div id="pagination"></div>
    </div>
  </div>

  <!-- ===== Right map ===== -->
  <div id="map-area">
    <div id="map"></div>

    <!-- ===== Place card (server-rendered) ===== -->
    <c:if test="${not empty restaurant}">
      <div id="place-card" style="${showCard ? '' : 'display:none;'}">
        <button class="close-btn" type="button" onclick="closeCard()">×</button>

        <div class="image-box">
          <c:forEach var="img" items="${images}">
            <img src="${img.content}" alt="">
          </c:forEach>
        </div>

        <div class="content">
          <div class="place-name-big">
            ${restaurant.place_name}
            <span class="category">
              <c:choose>
                <c:when test="${fn:contains(restaurant.category_name, '>')}">
                  <c:set var="parts" value="${fn:split(restaurant.category_name, '>')}" />
                  <c:out value="${parts[fn:length(parts) - 1]}" />
                </c:when>
                <c:otherwise>
                  <c:out value="${restaurant.category_name}" />
                </c:otherwise>
              </c:choose>
            </span>
          </div>

          <div class="address">
            <svg class="icon" viewBox="0 0 24 24" aria-hidden="true">
              <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5S10.62 6.5 12 6.5s2.5 1.12 2.5 2.5S13.38 11.5 12 11.5z"/>
            </svg>
            ${restaurant.road_address_name}<br>
            ${restaurant.address_name}
          </div>

          <div class="phone">
            <svg class="icon" viewBox="0 0 24 24" aria-hidden="true">
              <path d="M6.62 10.79a15.05 15.05 0 006.59 6.59l2.2-2.2a1 1 0 011.01-.24c1.12.37 2.33.57 3.58.57a1 1 0 011 1V20a1 1 0 01-1 1C10.3 21 3 13.7 3 4a1 1 0 011-1h3.5a1 1 0 011 1c0 1.25.2 2.46.57 3.59a1 1 0 01-.25 1.01l-2.2 2.19z"/>
            </svg>
            ${restaurant.phone}
          </div>
        </div>

        <!-- ===== Reviews ===== -->
        <div class="review-area">

          <!-- 회원만 리뷰 작성 -->
          <sec:authorize access="isAuthenticated()">
            <form action="${path}/review/write" method="post" id="reviewForm">
              <input type="hidden" name="placeId" value="${restaurant.id}">
              <input type="hidden" name="rating" value="5">

              <!-- redirect 유지용 -->
              <input type="hidden" name="keyword" value="${keyword}">
              <input type="hidden" name="lat" value="${lat}">
              <input type="hidden" name="lon" value="${lon}">
              <input type="hidden" name="card" value="1">

              <div class="star-rating star-input" id="starGroup">
				  <span class="active">★</span>
				  <span class="active">★</span>
				  <span class="active">★</span>
				  <span class="active">★</span>
				  <span class="active">★</span>
			</div>

              <div class="review-input-row">
                <input type="text" name="content" placeholder="리뷰를 작성하세요" required>
                <button type="submit">등록</button>
              </div>
            </form>
          </sec:authorize>

          <!-- 비로그인 -->
          <sec:authorize access="isAnonymous()">
            <form id="fakeReviewForm">
              <div class="star-rating star-input">
                <span>☆</span><span>☆</span><span>☆</span><span>☆</span><span>☆</span>
              </div>
              <div class="review-input-row">
                <input type="text" placeholder="리뷰를 작성하세요">
                <button type="submit">등록</button>
              </div>
            </form>
          </sec:authorize>

          <!-- 리뷰 리스트 -->
          <c:forEach var="r" items="${reviews}">
            <div class="review">
              <div class="review-top">
                <div class="review-writer">
                  <c:out value="${empty r.writerNickname ? r.writer : r.writerNickname}" />
                </div>

                <span class="star">
                  <c:forEach begin="1" end="5" var="i">
                    <c:choose>
                      <c:when test="${r.rating != null and i <= r.rating}">★</c:when>
                      <c:otherwise>☆</c:otherwise>
                    </c:choose>
                  </c:forEach>
                </span>
                <span class="rating">(${r.rating})</span>
              </div>

              <div class="review-body">${r.content}</div>

              <div class="review-bottom">
                <c:if test="${not empty loginMno and (loginMno eq r.writer or isAdmin)}">
                  <form action="${path}/review/delete" method="post"
                        onsubmit="return confirm('정말 삭제하시겠습니까?');">
                    <c:if test="${not empty _csrf}">
                      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    </c:if>

                    <input type="hidden" name="me_no" value="${r.me_no}">
                    <input type="hidden" name="placeId" value="${restaurant.id}">
                    <input type="hidden" name="keyword" value="${keyword}">
                    <input type="hidden" name="lat" value="${lat}">
                    <input type="hidden" name="lon" value="${lon}">
                    <input type="hidden" name="card" value="1">

                    <button type="submit" class="btn-delete">삭제</button>
                  </form>
                </c:if>
              </div>
            </div>
          </c:forEach>

        </div>
      </div>
    </c:if>

  </div>
</div>

<!-- CURRENT_PLACE_ID는 restaurant 있을 때만 -->
<c:if test="${not empty restaurant}">
<script>
  window.CURRENT_PLACE_ID = ${restaurant.id};
</script>
</c:if>

<!-- Kakao SDK + restaurantList.js -->
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=5a128c1fae100ec246f65cb0390ad9f3&libraries=services,clusterer"></script>
<script src="${path}/js/restaurant/restaurantList.js"></script>

<!-- 분리한 JS -->
<script src="${path}/js/place/place_detail.js"></script>
<script src="${path}/js/place/place_review.js"></script>
<script src="${path}/js/place/placeEvent1.js"></script>
<script src="${path}/js/place/placeEvent2.js"></script>


</body>
</html>
