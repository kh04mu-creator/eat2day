<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 확인</title>
<link rel="stylesheet" href="/css/member/pwcheck.css">

</head>
<body>
<div id="layoutWrapper">
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-5">

      <!-- 🔶 카드 -->
      <div class="card shadow-sm member-card"
           style="margin-top: 80px; margin-bottom: 130px;">

        <!-- 중앙 자물쇠 아이콘 -->
        <div class="profile-inside">
          <div class="profile-circle">

            <!-- 🔒 LOCK ICON -->
			<svg width="64" height="64" viewBox="0 0 24 24"
			     fill="white" xmlns="http://www.w3.org/2000/svg">
			  <rect x="5" y="11" width="14" height="9" rx="2"/>
			  <path d="M8 11V8a4 4 0 118 0v3" stroke="white" stroke-width="2" fill="none"/>
			  <circle cx="12" cy="15" r="1.3" fill="#FF6600"/>
			</svg>


          </div>
        </div>

        <div class="card-body p-4">

          <h4 class="text-center mb-3">비밀번호 확인</h4>

          <p class="text-center text-muted mb-4">
            회원정보 수정 및 탈퇴를 위해<br>
            비밀번호를 입력하세요.
          </p>

          <form method="post" action="/member/passwordCheck">
            <!-- CSRF -->
            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}" />

            <input type="hidden" name="mode" value="${mode}">

            <!-- 비밀번호 -->
            <div class="mb-3">
              <label class="form-label">비밀번호</label>
              <input type="password"
                     name="m_password"
                     class="form-control"
                     placeholder="비밀번호를 입력하세요"
                     required>
            </div>

            <!-- 버튼 -->
            <div class="d-grid mt-4">
              <button type="submit" class="btn btn-orange">
                확인
              </button>
            </div>
          </form>

          <!-- 에러 -->
          <c:if test="${not empty msg}">
            <div class="alert alert-danger text-center mt-3">
              ${msg}
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
