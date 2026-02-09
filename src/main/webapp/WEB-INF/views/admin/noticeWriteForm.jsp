<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 등록</title>
<link rel="stylesheet" href="/css/admin/notice.css">

</head>
<body>
<div id="layoutWrapper">
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-7">

      <!-- 공지사항 등록 카드 -->
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

          <h4 class="text-center mb-3">공지사항 등록</h4>
          <p class="text-center text-muted mb-4">
            공지사항 제목과 내용을 입력하고 등록하세요.
          </p>

          <form name="noticeWriteForm"
                method="post"
                action="/admin/noticeWrite"
                enctype="multipart/form-data">

            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}" />

            <!-- 제목 -->
            <div class="mb-3">
              <label class="form-label">제목</label>
              <input type="text" name="no_title" class="form-control" required>
            </div>

            <!-- 내용 -->
            <div class="mb-3">
              <label class="form-label">내용</label>
              <textarea name="no_content" class="form-control" rows="6" required></textarea>
            </div>

            <!-- 파일 업로드 -->
            <div class="mb-3">
              <label class="form-label">업로드</label>
              <input type="file" name="file" class="form-control">
            </div>

            <!-- 버튼 -->
            <div class="d-flex justify-content-center gap-3 mt-4">
              <button type="submit" class="btn btn-orange px-4">등록</button>
              <button type="reset" class="btn btn-orange-outline px-4">다시쓰기</button>
              <button type="button"
				        class="btn btn-outline-secondary px-4"
				        onclick="location.href='${pageContext.request.contextPath}/admin/dashboard'">
				  관리자센터
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
