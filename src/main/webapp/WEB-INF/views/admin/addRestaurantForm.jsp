<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>식당 추가하기</title>
<link rel="stylesheet" href="/css/restaurant/addRestaurant.css"/>
</head>
<body>
<div id="layoutWrapper">
<div class="title_size" >
	<div class="title_inner">
		<span class="title_font">식당 등록</span>
	</div>
</div>

<div class="container mt-4 admin-wrap">

	<div class="d-flex justify-content-end align-items-center mb-3">
	  <a href="${pageContext.request.contextPath}/admin/dashboard"
	     class="btn btn-outline-secondary btn-sm btn-dashboard">
	    관리자센터
	  </a>
	</div>
<div class="card-soft shadow-sm">
	<div class="p-3">	
		<form name="addRestaurantForm" method="post" action="/admin/addRestaurant">
			<table class="table table-hover mb-0">
				<tr>
					<td class="th-font td-width">식당 ID</td>
					<td><input type="text" name="id" id="id" 
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">식당 이름</td>
					<td><input type="text" name="place_name" id="place_name" 
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">도로명 주소</td>
					<td><input type="text" name="road_address_name" id="road_address_name"
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">지번 주소</td>
					<td><input type="text" name="address_name" id="address_name"
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">카테고리 그룹 코드</td>
					<td><input type="text" name="category_group_code" id="category_group_code"
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">카테고리 그룹명</td>
					<td><input type="text" name="category_group_name" id="category_group_name"
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">카테고리명</td>
					<td><input type="text" name="category_name" id="category_name"
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">연락처</td>
					<td><input type="text" name="phone" id="phone"
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">식당 url</td>
					<td><input type="text" name="place_url" id="place_url"
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">X 좌표</td>
					<td><input type="text" name="x" id="x"
					class="form-control form-control-sm"></td>
				</tr>
				<tr>
					<td class="th-font td-width">Y 좌표</td>
					<td><input type="text" name="y" id="y"
					class="form-control form-control-sm"></td>
				</tr>
			</table>
			<div class="d-flex justify-content-center gap-2 btn-margin">
				<input type="submit" value="등록" class="btn btn-orange px-4">
				<input type="reset" value="다시쓰기" class="btn btn-orange-outline px-4">
			</div>
		</form>
	</div>
</div>
</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>