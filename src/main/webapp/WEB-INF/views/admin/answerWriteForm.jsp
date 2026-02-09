<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="/css/admin/answer.css">

<div id="layoutWrapper">
<div class="container mt-5" style="max-width:900px;">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="page-title mb-0">관리자 답변 작성</h4>
    

    <!-- ✅ 오른쪽 버튼 묶음 -->
    <div class="d-flex gap-2">
      <a class="btn btn-light btn-sm" href="/admin/qnaManage">QnA 관리</a>
      <a class="btn btn-light btn-sm" href="/member/qnaList">회원 QnA</a>
    </div>
  </div>


  <div class="card shadow-sm mb-3">
    <div class="card-body">
      <div class="d-flex align-items-center gap-2 mb-2">
        <c:if test="${q.q_secret eq 'Y'}">
          <span>🔒</span>
        </c:if>
        <h5 class="mb-0">${q.q_title}</h5>
      </div>

      <div class="text-muted small mb-3">
        작성자: ${q.m_no} ·
        <fmt:formatDate value="${q.q_date}" pattern="yyyy.MM.dd"/>
      </div>

      <div style="white-space:pre-wrap;">${q.q_content}</div>
    </div>
  </div>

  <h5 class="mb-2">기존 답변</h5>
  <c:if test="${empty answers}">
    <div class="alert alert-light border">아직 답변이 없습니다.</div>
  </c:if>

  <c:forEach var="a" items="${answers}">
    <div class="card mb-2">
      <div class="card-body">
        <div class="text-muted small mb-2">
          <fmt:formatDate value="${a.a_date}" pattern="yyyy.MM.dd"/>
        </div>
        <div style="white-space:pre-wrap;">${a.a_content}</div>

        <!-- 🔥 삭제 버튼 (카드 안으로 넣는 게 UI가 깔끔함) -->
        <form method="post"
              action="${path}/admin/answerDelete"
              onsubmit="return confirm('이 답변을 삭제할까요?');"
              class="mt-2">

          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <input type="hidden" name="a_no" value="${a.a_no}">
          <input type="hidden" name="q_no" value="${q.q_no}">

          <button type="submit" class="btn btn-sm btn-danger">
            삭제
          </button>
        </form>
      </div>
    </div>
  </c:forEach>

  <div class="card shadow-sm mt-3">
    <div class="card-body">
      <form method="post" action="/admin/answerWrite">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="q_no" value="${q_no}" />

        <div class="mb-2">
          <label class="form-label">답변 내용</label>
          <textarea name="a_content" rows="5" class="form-control" required></textarea>
        </div>

        <button class="btn btn-orange btn-sm">답변 등록</button>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
