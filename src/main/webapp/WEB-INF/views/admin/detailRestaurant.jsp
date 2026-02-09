<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>식당 상세정보</title>
<link rel="stylesheet" href="/css/restaurant/detailRestaurant.css"/>
</head>
<body>
<div id="layoutWrapper">
<div class="title_size">
	<div class="title_inner">
		<span class="title_font">식당 정보</span>
	</div>
</div>

<div class="container mt-5">

	<div class="row justify-content-center">
		<div class="col-md-7">
		<div class="card shadow-sm member-card"
           style="margin-top: 80px; margin-bottom: 130px;">
           
		<table class="table table-hover align-middle mb-0">
			<tr>
				<th>식당 이름</th>
				<td class="place-name-col">${view.place_name}</td>
			</tr>
			<tr>
				<th>식당 ID</th>
				<td>${view.id}</td>
			</tr>
			<tr>
				<th>도로명 주소</th>
				<td>${view.road_address_name}</td>
			</tr>
			<tr>
				<th>지번 주소</th>
				<td>${view.address_name}</td>
			</tr>
			<tr>
				<th>카테고리 그룹 코드</th>
				<td>${view.category_group_code}</td>
			</tr>
			<tr>
				<th>카테고리 그룹명</th>
				<td>${view.category_group_name}</td>
			</tr>
			<tr>
				<th>카테고리명</th>
				<td>${view.category_name}</td>
			</tr>
			<tr>
				<th>연락처</th>
				<td>${view.phone}</td>
			</tr>
			<tr>
				<th>식당 url</th>
				<td>${view.place_url}</td>
			</tr>
			<tr>
				<th>X 좌표</th>
				<td>${view.x}</td>
			</tr>
			<tr>
				<th>Y 좌표</th>
				<td>${view.y}</td>
			</tr>
		</table>
			<div class="d-flex justify-content-center gap-2 btn-margin">
				<button onclick="location.href='/admin/updateRestaurantForm?id=${view.id}'"
				class="btn btn-orange px-4">수정</button>
					
				<form action="${pageContext.request.contextPath}/admin/deleteRestaurant?id=${view.id}"
			     method="post"
			     onsubmit="return confirm('정말 이 식당 정보를 삭제하시겠습니까?');"
			     class="d-inline">
			
					<input type="hidden"
					name="${_csrf.parameterName}"
					value="${_csrf.token}" />
					
					<button type="submit" class="btn btn-orange-outline px-4">
					  삭제
					</button>
				</form>
				<button type="button"
				        class="btn btn-outline-secondary px-4"
				        onclick="location.href='${pageContext.request.contextPath}/admin/dashboard'">
				  관리자센터
				</button>
				</div>			
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>
