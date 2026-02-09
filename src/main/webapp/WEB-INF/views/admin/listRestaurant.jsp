<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>식당 목록</title>
<link rel="stylesheet" href="/css/restaurant/listRestaurant.css"/>
</head>
<body>
<div id="layoutWrapper">
<div class="title_size">
	<div class="title_inner">
		<span class="title_font">식당 관리</span>
	</div>
</div>

<div class="container mt-4 admin-wrap">

<!-- 상단 헤더 -->
<div class="d-flex justify-content-end align-items-center mb-3">
	<div class="d-flex gap-2">
	   <a href="${path}/admin/dashboard" 
	   class="btn btn-outline-secondary btn-sm btn-dashboard">관리자센터</a>
	   <a href="${path}/admin/addRestaurantForm"
	   class="btn btn-orange btn-sm btn-dashboard">등록하기</a>
	</div>
</div>

<!-- 식당 테이블 카드 -->
<div class="card shadow-sm border-0">
	<div class="card-body p-0">
		<table class="table table-hover align-middle mb-0">
			<thead class="table-light">
				<tr class="text-center">
					<th>ID</th>
					<th>카테고리 코드</th>
					<th>카테고리명</th>
					<th>카테고리 상세명</th>
					<th>식당명</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="list" items="${list}">
					<tr onclick="location.href='/admin/detailRestaurant?id=${list.id}'"
					class="cursor">
						<td>${list.id}</td>
						<td>${list.category_group_code}</td>
						<td>${list.category_group_name}</td>
						<td>${list.category_name}</td>
						<td class="place-name-col">${list.place_name}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>