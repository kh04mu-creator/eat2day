<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="/css/member/qnaList.css">

<div id="layoutWrapper">
<div class="container mt-5 qna-wrap">

  <!-- 상단 -->
  <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
    <form method="get" action="/member/qnaList" class="d-flex align-items-center gap-2">
      <input type="checkbox" id="onlyMine" name="onlyMine" value="true"
             ${onlyMine ? "checked" : ""} onchange="this.form.submit()">
      <label for="onlyMine" class="mb-0">내 문의글 보기</label>
    </form>

    <a class="btn btn-orange btn-sm" href="/member/questionWriteForm">문의하기</a>
  </div>

  <c:if test="${empty list}">
    <div class="alert alert-light border">등록된 문의가 없습니다.</div>
  </c:if>

  <c:forEach var="q" items="${list}">
    <c:set var="secret" value="${q.q_secret eq 'Y'}" />
    <c:set var="acnt" value="${answerCountMap[q.q_no] != null ? answerCountMap[q.q_no] : 0}" />
    <c:set var="isOwner" value="${loginMno != null && loginMno eq q.m_no}" />
    <c:set var="canRead" value="${(!secret) || isOwner || isAdmin}" />

    <div class="qna-item shadow-sm" id="q-card-${q.q_no}">

      <div class="d-flex justify-content-between align-items-start gap-3">
        <div class="flex-grow-1">
          <div class="d-flex align-items-center gap-2">
            <c:choose>
              <c:when test="${acnt > 0}">
                <span class="badge badge-done text-white">답변완료</span>
              </c:when>
              <c:otherwise>
                <span class="badge badge-wait text-white">답변대기</span>
              </c:otherwise>
            </c:choose>

            <c:if test="${secret}">
              <span>🔒</span>
            </c:if>
          </div>

          <p class="qna-title mt-2">
            <c:choose>
              <c:when test="${secret}">
                비밀문의입니다.
              </c:when>
              <c:otherwise>
                ${q.q_title}
              </c:otherwise>
            </c:choose>
          </p>

          <div class="qna-meta">
            <span>${q.m_nickname}</span>
            <span>·</span>
            <span><fmt:formatDate value="${q.q_date}" pattern="yyyy.MM.dd"/></span>
          </div>
        </div>

        <div class="d-flex align-items-start gap-2">
  <!-- ✅ 작성자일 때만 삭제 버튼 표시 -->
  <c:if test="${isOwner}">
    <form method="post"
          action="${path}/member/questionDelete"
          onsubmit="return confirm('이 문의글을 삭제할까요?');"
          style="margin:0;">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
      <input type="hidden" name="q_no" value="${q.q_no}">
      <button type="submit" class="btn btn-gray btn-mini">삭제</button>
    </form>
  </c:if>

  <button class="toggle-btn"
          type="button"
          onclick="togglePanel('${q.q_no}', ${secret ? 'true':'false'}, ${canRead ? 'true':'false'}, ${acnt})"
          id="btn-${q.q_no}">
    ▾
  </button>
</div>

      </div>

      <!-- 펼침 패널 -->
      <div class="panel" id="panel-${q.q_no}" data-loaded="0">
        <div class="panel-title">문의 내용</div>

        <c:choose>
          <c:when test="${canRead}">
            <div style="white-space:pre-wrap;">
              ${q.q_content}
            </div>
          </c:when>
          <c:otherwise>
            <div class="alert alert-light border mb-0">
              🔒 비밀문의입니다. 작성자/관리자만 확인할 수 있어요.
            </div>
          </c:otherwise>
        </c:choose>

        <hr class="my-3"/>

        <div class="panel-title">답변</div>
        <div id="ans-list-${q.q_no}">
          <c:if test="${acnt == 0}">
            <div class="text-muted">아직 답변이 없습니다.</div>
          </c:if>
          <c:if test="${acnt > 0}">
            <div class="text-muted">답변을 불러오는 중...</div>
          </c:if>
        </div>
      </div>
    </div>

  </c:forEach>
</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>

<script>
async function togglePanel(qNo, isSecret, canRead, answerCount) {
  const panel = document.getElementById('panel-' + qNo);
  const btn = document.getElementById('btn-' + qNo);
  const ansList = document.getElementById('ans-list-' + qNo);

  if (panel.style.display === 'block') {
    panel.style.display = 'none';
    btn.textContent = '▾';
    return;
  }

  panel.style.display = 'block';
  btn.textContent = '▴';

  if (isSecret && !canRead) {
    ansList.innerHTML = '<div class="text-muted">비밀문의 답변은 작성자/관리자만 볼 수 있어요.</div>';
    return;
  }

  if (!answerCount || answerCount <= 0) return;
  if (panel.dataset.loaded === '1') return;

  try {
    const res = await fetch('/member/qnaAnswers?q_no=' + encodeURIComponent(qNo));
    const data = await res.json();

    if (!data || data.length === 0) {
      ansList.innerHTML = '<div class="text-muted">답변이 없거나 접근할 수 없습니다.</div>';
      panel.dataset.loaded = '1';
      return;
    }

    let html = '';
    for (const a of data) {
      const dateText = a.a_date ? a.a_date : '';
      const contentText = a.a_content ? a.a_content : '';

      html += '<div class="answer-one">';
      html += '  <div class="text-muted small mb-1">' + escapeHtml(dateText) + '</div>';
      html += '  <div style="white-space:pre-wrap;">' + escapeHtml(contentText) + '</div>';
      html += '</div>';
    }

    ansList.innerHTML = html;
    panel.dataset.loaded = '1';
  } catch (e) {
    ansList.innerHTML = '<div class="text-danger">답변을 불러오지 못했습니다.</div>';
  }
}

function escapeHtml(str) {
  return String(str)
    .replaceAll('&','&amp;')
    .replaceAll('<','&lt;')
    .replaceAll('>','&gt;')
    .replaceAll('"','&quot;')
    .replaceAll("'","&#039;");
}
</script>


