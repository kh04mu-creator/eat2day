<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내정보</title>
<link rel="stylesheet" href="/css/member/memberDetail.css">

</head>
<body>
<div id="layoutWrapper">
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-7">
      

      <!-- 내정보 카드 -->
      <div class="card shadow-sm member-card"
     style="margin-top: 80px; margin-bottom: 130px;">

		  <!-- 🔶 카드 안 중앙 프로필 아이콘 -->
		  <div class="profile-inside">
		    <div class="profile-circle">
			
			  <!-- 👑 ADMIN 왕관 -->
			  <c:if test="${memberDetail.m_auth == 'ROLE_ADMIN'}">
			    <svg class="crown-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
				
				  <!-- 👑 기본 왕관 -->
				  <path d="M4 16h16l-1.5-7-4 3-2.5-5-2.5 5-4-3L4 16z"
				        fill="#FFD700"
				        stroke="#E6B800"
				        stroke-width="1.2"
				        stroke-linejoin="round"/>
				
				  <!-- ✨ 빛나는 선 (애니메이션 대상) -->
				  <!-- ✨ 반짝선 -->
					<line x1="6.5" y1="6" x2="4" y2="2"
					      stroke="#FFD700"
					      stroke-width="1.12"
					      stroke-linecap="round"
					      class="spark"/>
					
					<line x1="12" y1="5" x2="12" y2="1"
					      stroke="#FFD700"
					      stroke-width="1.12"
					      stroke-linecap="round"
					      class="spark spark-delay"/>
					
					<line x1="17.5" y1="6" x2="20" y2="2"
					      stroke="#FFD700"
					      stroke-width="1.12"
					      stroke-linecap="round"
					      class="spark spark-delay2"/>

				
				</svg>




			  </c:if>
			
			  <!-- 👤 사람 아이콘 -->
			  <svg width="64" height="64" viewBox="0 0 24 24" fill="none"
			       xmlns="http://www.w3.org/2000/svg">
			    <circle cx="12" cy="8" r="4" fill="white"/>
			    <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="white"/>
			  </svg>
			
			</div>

		  </div>
		
		  <div class="card-body p-4">
		    <table class="table table-bordered align-middle" style="margin-bottom: 50px;">
		      <tbody>
		        <tr>
		          <th class="table-light" style="width:20%">이메일</th>
		          <td style="width:30%">${memberDetail.m_email}</td>
		          <th class="table-light">생일</th>
		          <td>${memberDetail.m_birth}</td>
		        </tr>
		        <tr>
		          <th class="table-light">성명</th>
		          <td>${memberDetail.m_name}</td>
		          <th class="table-light">닉네임</th>
		          <td>${memberDetail.m_nickname}</td>
		        </tr>
		        <tr>
		          <th class="table-light">전화번호</th>
		          <td>${memberDetail.m_tel}</td>
		          <th class="table-light">가입일</th>
		          <td>
		            <fmt:formatDate value="${memberDetail.m_date}" pattern="yy/MM/dd" />
		          </td>
		        </tr>
		        <tr>
		          <th class="table-light">주소</th>
		          <td colspan="3">${memberDetail.m_addr}</td>
		        </tr>
		      </tbody>
		    </table>
		
		    <!-- 🔶 버튼 영역 -->
		    <c:if test="${loginUser.m_email == memberDetail.m_email || loginUser.m_auth == 'ROLE_ADMIN'}">
		      <div class="d-flex justify-content-center gap-3 mt-4">
		        <button class="btn btn-orange px-4"
		                onclick="location.href='/member/passwordCheckForm?mode=update'">
		          정보 수정
		        </button>
		        <button class="btn btn-orange-outline px-4"
		                onclick="location.href='/member/passwordCheckForm?mode=delete'">
		          회원 탈퇴
		        </button>
		      </div>
		    </c:if>
		
		  </div>
		</div>

    </div>
  </div>
</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>