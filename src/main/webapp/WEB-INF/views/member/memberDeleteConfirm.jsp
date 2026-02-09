<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원탈퇴</title>
<link rel="stylesheet" href="/css/member/memberDetail.css">

</head>
<body>
<div id="layoutWrapper">
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-5">

      <!-- 🔶 회원탈퇴 카드 (passwordCheckForm 스타일) -->
      <div class="card shadow-sm member-card"
           style="margin-top: 80px; margin-bottom: 130px;">

        <!-- 중앙 자물쇠 아이콘 -->
        <div class="profile-inside">
          <div class="profile-circle">

            <!-- WARNING ICON -->
            <svg width="64" height="64" viewBox="0 0 24 24"
                 fill="white" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 3L2 21h20L12 3z"/>
              <rect x="11" y="9" width="2" height="6" fill="#ff6600"/>
              <rect x="11" y="16.5" width="2" height="2" fill="#ff6600"/>
            </svg>

          </div>
        </div>

        <div class="card-body p-4">

          <h4 class="text-center mb-3">회원 탈퇴</h4>

          <!-- 오렌지 경고 -->
          <div class="alert alert-orange text-center fw-bold mb-4">
            탈퇴 시 모든 정보가 삭제되며<br>
            <span class="fs-6">복구할 수 없습니다.</span>
          </div>

          <form method="post" action="/member/memberDelete">
            <!-- CSRF -->
            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}" />

            <!-- 회원 번호 -->
            <input type="hidden" name="m_no" value="${loginUser.m_no}">

            <!-- 버튼 -->
            <div class="d-grid gap-2 mt-4">
			  <button type="submit"
			          class="btn btn-orange"
			          onclick="return confirm('탈퇴하면 모든 정보가 삭제되고 복구할 수 없습니다.\n정말 진행하시겠습니까?');">
			    탈퇴하기
			  </button>
			
			  <button type="button"
			          class="btn btn-orange-outline"
			          onclick="history.back()">
			    취소
			  </button>
			</div>

          </form>

        </div>
      </div>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>
