<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="/css/admin/q&aManagement.css">

<div id="layoutWrapper">
<div class="container mt-5 admin-wrap">

  <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
    <h4 class="page-title mb-0">QnA 관리</h4>

    <div class="d-flex gap-2">
      <a href="${path}/admin/dashboard" class="btn btn-light btn-sm">관리자센터</a>
      <a href="${path}/member/qnaList" class="btn btn-outline-secondary btn-sm">회원 QnA 보기</a>
    </div>
  </div>

  <c:if test="${empty list}">
    <div class="alert alert-light border">등록된 문의가 없습니다.</div>
  </c:if>

  <c:forEach var="q" items="${list}">
    <c:set var="secret" value="${q.q_secret eq 'Y'}" />
    <c:set var="acnt" value="${answerCountMap[q.q_no] != null ? answerCountMap[q.q_no] : 0}" />
    <c:set var="latest" value="${latestAnswerMap[q.q_no]}" />

    <div class="qna-item shadow-sm">

      <div class="qna-head">
        <div class="flex-grow-1">

          <div class="qna-badges">
            <c:choose>
              <c:when test="${acnt > 0}">
                <span class="badge badge-done text-white">답변완료</span>
              </c:when>
              <c:otherwise>
                <span class="badge badge-wait text-white">답변대기</span>
              </c:otherwise>
            </c:choose>

            <c:if test="${secret}">
              <span class="badge badge-secret text-white">비밀글</span>
            </c:if>
          </div>

          <div class="qna-title">${q.q_title}</div>

          <div class="qna-meta">
            <span>문의번호: <b>${q.q_no}</b></span>
            <span>·</span>
            <span>작성자: <b>${q.m_nickname}</b></span>
            <span>·</span>
            <span><fmt:formatDate value="${q.q_date}" pattern="yyyy.MM.dd HH:mm"/></span>
            <c:if test="${acnt > 0}">
              <span>·</span>
              <span>답변 ${acnt}개</span>
            </c:if>
          </div>

        </div>

        <!-- ✅ 우측 상단 버튼들 -->
        <div class="d-flex flex-column gap-2 align-items-end">

          <div class="d-flex gap-2">
            <!-- ✅ 답변 작성(주황) -->
            <a class="btn btn-orange btn-mini"
               href="${path}/admin/answerWriteForm?q_no=${q.q_no}">
              답변 작성
            </a>

            <!-- ✅ 질문 삭제(회색, 관리자) -->
            <form method="post"
                  action="${path}/admin/questionDelete"
                  onsubmit="return confirm('이 문의글을 삭제할까요? (답변도 함께 삭제됩니다)');"
                  style="margin:0;">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
              <input type="hidden" name="q_no" value="${q.q_no}">
              <button type="submit" class="btn btn-gray btn-mini">질문삭제</button>
            </form>
          </div>

          <c:if test="${acnt > 0}">
            <a class="btn btn-outline-secondary btn-mini"
               href="${path}/admin/answerWriteForm?q_no=${q.q_no}">
              수정/추가
            </a>
          </c:if>
        </div>
      </div>

      <c:if test="${secret}">
        <div class="secret-hint">🔒 비밀 문의입니다. (관리자는 내용 확인 가능)</div>
      </c:if>

      <!-- ✅ 질문 내용: 3줄 요약 -->
      <div class="question-box">
        <div id="q-${q.q_no}" class="clamp-3">${q.q_content}</div>
        <button class="more-btn" type="button" onclick="toggleClamp('q-${q.q_no}', this)">더보기</button>
      </div>

      <!-- ✅ 최신 관리자 답변 + 답변 삭제(회색) -->
      <c:if test="${latest != null}">
        <div class="answer-box">
          <div class="answer-head">
            <div class="answer-title">관리자 답변</div>

            <div class="d-flex align-items-center gap-2">
              <span class="text-muted small">
                <fmt:formatDate value="${latest.a_date}" pattern="yyyy.MM.dd"/>
              </span>

              <form method="post"
                    action="${path}/admin/answerDelete"
                    onsubmit="return confirm('이 답변을 삭제할까요?');"
                    style="margin:0;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="hidden" name="a_no" value="${latest.a_no}">
                <input type="hidden" name="q_no" value="${q.q_no}">
                <input type="hidden" name="redirect" value="qnaManage">
                <button type="submit" class="btn btn-gray btn-mini">삭제</button>
              </form>
            </div>
          </div>

          <div id="a-${q.q_no}" class="clamp-3 mt-2">${latest.a_content}</div>
          <button class="more-btn" type="button" onclick="toggleClamp('a-${q.q_no}', this)">더보기</button>
        </div>
      </c:if>

    </div>
  </c:forEach>

</div>

<script src="/js/admin/q&aManagement.js"></script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>