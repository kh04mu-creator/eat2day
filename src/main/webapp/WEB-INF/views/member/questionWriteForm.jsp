<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문의하기</title>
<link rel="stylesheet" href="/css/member/qWrite.css">

</head>

<body>
<div id="layoutWrapper">

<div class="container mt-5" style="max-width:900px;">
  <div class="row justify-content-center">
    <div class="col-12">

      <!-- ✅ 문의하기 카드 -->
      <div class="card shadow-sm member-card" style="margin-top:80px; margin-bottom:130px;">

        <!-- 🔶 카드 안 중앙 아이콘 -->
        <div class="profile-inside">
          <div class="profile-circle">

            <!-- ✉️ 문의(메일) 아이콘 -->
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none"
                 xmlns="http://www.w3.org/2000/svg">
              <path d="M4 6h16v12H4V6z" stroke="white" stroke-width="2" fill="none"/>
              <path d="M4 7l8 6 8-6" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>

          </div>
        </div>

        <div class="card-body p-4">

          <h4 class="text-center mb-3">문의하기</h4>
          <p class="text-center text-muted mb-4">
            문의 제목과 내용을 작성해 주세요.
          </p>

          <form method="post" action="/member/questionWrite">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <div class="mb-3">
              <label class="form-label">제목</label>
              <input type="text" name="q_title" class="form-control" placeholder="예) 음식점 문의입니다." required>
            </div>

            <div class="mb-3">
              <label class="form-label">내용</label>
              <textarea name="q_content" rows="8" class="form-control"
                        placeholder="문의 내용을 작성해주세요." required></textarea>
            </div>

            <div class="form-check mb-3">
              <input class="form-check-input" type="checkbox" id="secret" name="q_secret" value="Y">
              <label class="form-check-label" for="secret">비밀문의</label>
              <div class="form-text">비밀문의는 작성자/관리자만 확인할 수 있어요.</div>
            </div>

            <div class="d-flex justify-content-center gap-3 mt-4">
              <button type="submit" class="btn btn-orange px-4">등록</button>
              <a href="/member/qnaList" class="btn btn-gray px-4">취소</a>
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
