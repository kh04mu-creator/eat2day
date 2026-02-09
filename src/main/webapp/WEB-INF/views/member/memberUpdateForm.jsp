<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원정보수정</title>

<link rel="stylesheet" href="/css/member/memberUpdate.css">

<script src="/js/member/juso.js"></script>

</head>
<body>
<div id="layoutWrapper">
<div class="auth-container">

  <!-- ✅ 오른쪽: 회원정보 수정 폼 -->
  <div class="auth-form">
    <h2>회원정보 수정</h2>

    <!-- ✅ 소셜 계정이면 비밀번호 설정 유도 배너 -->
    <c:if test="${empty memberUpdate.m_pw}">
      <div class="social-warning">
        <div class="social-warning-title">소셜 로그인 계정 안내</div>
        <div class="social-warning-text">
          현재 계정은 <b>비밀번호가 설정되어 있지 않아요.</b><br>
          회원정보 수정/탈퇴 기능을 사용하려면 먼저 비밀번호를 설정해 주세요.
        </div>
        <a class="social-warning-btn"
           href="/member/setPasswordForm?next=/member/memberUpdateForm">
          비밀번호 설정하러 가기
        </a>
      </div>
    </c:if>

    <form name="memberUpdateForm" method="post" action="/member/memberUpdate">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      <input type="hidden" name="m_no" value="${memberUpdate.m_no}">

      <label>이메일</label>
      <div class="readonly">${memberUpdate.m_email}</div>

      <!-- ✅ 비밀번호 입력칸: 일반 계정만 입력 가능 / 소셜은 안내 -->
      <c:choose>
        <c:when test="${empty memberUpdate.m_pw}">
          <label>비밀번호</label>
          <div class="readonly">소셜 로그인 계정은 비밀번호 설정 후 변경할 수 있어요.</div>
        </c:when>
        <c:otherwise>
          <label>비밀번호</label>
          <input type="password" name="m_pw" placeholder="변경 시에만 입력">
        </c:otherwise>
      </c:choose>

      <label>이름</label>
      <input type="text" name="m_name" value="${memberUpdate.m_name}">

      <label>닉네임</label>
      <input type="text" name="m_nickname" value="${memberUpdate.m_nickname}">

      <label>생년월일</label>
      <div class="readonly">${memberUpdate.m_birth}</div>

      <label>연락처</label>
      <input type="text" name="m_tel" value="${memberUpdate.m_tel}">

      <label>주소</label>
      <button type="button" class="addr-btn" onclick="goPopup()">주소 찾기</button>
      <input type="text"
             id="roadFullAddr"
             name="m_addr"
             value="${memberUpdate.m_addr}"
             placeholder="도로명 주소">

      <div class="btn-group">
        <button type="submit" class="btn-primary-custom">수정하기</button>
        <button type="reset" class="btn-secondary-custom">다시쓰기</button>
      </div>
    </form>
  </div>

  <!-- ✅ 왼쪽: 오렌지 오버레이 -->
  <div class="overlay-container">
    <div class="overlay">
      <div class="overlay-panel">
        <div class="icon">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none"
               xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="8" r="4" fill="white"/>
            <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="white"/>
          </svg>
        </div>

        <h2>내 정보 관리</h2>
        <p>
          이름/닉네임/연락처/주소를 수정할 수 있어요.<br>
          비밀번호는 변경할 때만 입력하면 됩니다.
        </p>
      </div>
    </div>
  </div>

</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>
