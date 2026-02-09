<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 상세</title>
<link rel="stylesheet" href="/css/member/noticeDetail.css">

</head>
<body>
<div id="layoutWrapper">
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-7">

      <!-- 공지 상세 카드 -->
      <div class="card shadow-sm member-card"
           style="margin-top: 80px; margin-bottom: 130px;">

        <!-- 🔶 카드 안 중앙 아이콘 -->
        <div class="profile-inside">
          <div class="profile-circle">

            <!-- 📣 공지(메가폰) 아이콘 -->
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none"
                 xmlns="http://www.w3.org/2000/svg">
              <path d="M3 11v2c0 1.1.9 2 2 2h2l3 4h2l-2-4h2l7 3V6l-7 3H5c-1.1 0-2 .9-2 2z"
                    fill="white"/>
              <path d="M18 8.5v7" stroke="white" stroke-width="2" stroke-linecap="round"/>
            </svg>

          </div>
        </div>

        <div class="card-body p-4">

          <h4 class="text-center mb-2">${notice.no_title}</h4>

          <p class="text-center text-muted mb-4">
            작성일 :
            <fmt:formatDate value="${notice.no_date}" pattern="yyyy-MM-dd"/>
          </p>

          <hr>

          <div class="notice-content mt-3">
            ${notice.no_content}
          </div>

          <!-- 이미지 (있을 때만) -->
          <c:if test="${not empty notice.no_upload}">
            <div class="text-center mt-4">
              <img class="notice-img"
                   src="${pageContext.request.contextPath}/upload/notice/${notice.no_upload}"
                   alt="공지 첨부 이미지">
            </div>
          </c:if>

          <!-- 버튼 -->
          <div class="d-flex justify-content-center gap-3 mt-5">

            <!-- 목록 -->
            <button type="button"
                    class="btn btn-orange-outline px-4"
                    onclick="location.href='${pageContext.request.contextPath}/member/noticeList'">
              목록
            </button>

            <!-- 관리자만 -->
            <sec:authorize access="hasRole('ADMIN')">
              <button type="button"
                      class="btn btn-orange px-4"
                      onclick="location.href='${pageContext.request.contextPath}/admin/noticeUpdateForm?no_no=${notice.no_no}'">
                수정
              </button>

              <button type="button"
                      class="btn btn-outline-danger px-4"
                      onclick="if(confirm('정말 삭제하시겠습니까?')){ location.href='${pageContext.request.contextPath}/admin/noticeDelete?no_no=${notice.no_no}'; }">
                삭제
              </button>
            </sec:authorize>

          </div>

        </div>
      </div>

    </div>
  </div>
</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>
