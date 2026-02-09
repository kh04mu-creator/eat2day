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
<title>공지사항</title>
<link rel="stylesheet" href="/css/member/noticeList.css">

</head>

<body>
<div id="layoutWrapper">
<!-- 상단 타이틀 -->
<div class="title_size">
  <div class="title_inner">
    <span class="title_font">공지사항</span>
  </div>
</div>

<div class="container mt-4 admin-wrap">

  <!-- 상단 헤더(우측 버튼 영역) -->
  <div class="d-flex justify-content-end align-items-center gap-2 mb-3">

    <!-- 글쓰기(관리자만) -->
    <sec:authorize access="hasRole('ADMIN')">
      <a href="${pageContext.request.contextPath}/admin/dashboard"
	       class="btn btn-outline-secondary btn-sm btn-dashboard">
	      관리자센터
	    </a>
	
	    <!-- 글쓰기 -->
	    <a href="${pageContext.request.contextPath}/admin/noticeWriteForm"
	       class="btn btn-orange btn-sm btn-dashboard">
	      글쓰기
	    </a>
    </sec:authorize>

  </div>

  <!-- 공지사항 테이블 카드 -->
  <div class="card shadow-sm border-0">
    <div class="card-body p-0">

      <table class="table table-hover align-middle mb-0">
        <thead class="table-light">
          <tr class="text-center">
            <th style="width: 12%;">번호</th>
            <th>제목</th>
            <th style="width: 18%;">작성일</th>
          </tr>
        </thead>

        <tbody>
          <c:forEach var="dto" items="${no_list}">
            <tr class="text-center">
              <td class="text-muted">${dto.no_no}</td>

              <td class="text-start px-3">
                <a class="notice-link"
                   href="${pageContext.request.contextPath}/member/noticeDetail?no_no=${dto.no_no}">
                  ${dto.no_title}
                </a>
              </td>

              <td>
                <fmt:formatDate value="${dto.no_date}" pattern="yy/MM/dd"/>
              </td>
            </tr>
          </c:forEach>

          <!-- 목록 없을 때 -->
          <c:if test="${empty no_list}">
            <tr>
              <td colspan="3" class="text-center text-muted py-4">
                등록된 공지사항이 없습니다.
              </td>
            </tr>
          </c:if>

        </tbody>
      </table>

    </div>
  </div>

</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>
