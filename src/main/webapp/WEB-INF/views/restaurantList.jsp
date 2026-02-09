<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>당신 주변의 ${param.keyword} 맛집</title>
<link rel="stylesheet" href="/css/restaurant/RestaurantList.css">
</head>
<body>
<div id="layoutWrapper">

<h2 class="search-title" style="text-align:center;">주변의 ${param.keyword} 맛집</h2>

<input type="hidden" id="keyword" value="${param.keyword}">
<input type="hidden" id="lat" value="${empty lat ? '35.1479' : lat}">
<input type="hidden" id="lon" value="${empty lon ? '129.0596' : lon}">

	
<div class="place-container">	
	<div id="map" class="place-map"></div>
	<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=5a128c1fae100ec246f65cb0390ad9f3&libraries=services,clusterer"></script>
	<c:set var="path" value="${pageContext.request.contextPath}" />
	<script>
	  window.APP_PATH = "${path}";
	</script>
	<script src="${path}/js/restaurant/restaurantList.js"></script>

	
	<div class="place-list">
		<table>
			<tbody id="resultBody"></tbody>
		</table>
		<div id="pagination" class="pagination-wrap"></div>

	</div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>

</body>

</html>