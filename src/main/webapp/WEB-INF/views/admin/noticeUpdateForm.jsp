<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 수정</title>
<link rel="stylesheet" href="/css/admin/notice.css">

</head>
<body>
<div id="layoutWrapper">
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-7">

      <div class="card shadow-sm member-card"
           style="margin-top: 80px; margin-bottom: 130px;">

        <!-- 중앙 아이콘 -->
        <div class="profile-inside">
          <div class="profile-circle">

            <!-- ✏️ 수정 아이콘 -->
            <svg width="74" height="74" viewBox="0 0 24 24" fill="none"
			     xmlns="http://www.w3.org/2000/svg">
			  <path d="M4 20h4l10-10-4-4L4 16v4z"
			        stroke="white"
			        stroke-width="2"
			        stroke-linejoin="round"/>
			  <path d="M13 7l4 4"
			        stroke="white"
			        stroke-width="2"
			        stroke-linecap="round"/>
			</svg>

          </div>
        </div>

        <div class="card-body p-4">

          <h4 class="text-center mb-3">공지사항 수정</h4>
          <p class="text-center text-muted mb-4">
            내용을 수정하고 저장하세요.
          </p>

          <form method="post"
                action="/admin/noticeUpdate"
                enctype="multipart/form-data">

            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}" />

            <input type="hidden" name="no_no" value="${notice.no_no}" />

            <!-- 제목 -->
            <div class="mb-3">
              <label class="form-label">제목</label>
              <input type="text"
                     name="no_title"
                     class="form-control"
                     value="${notice.no_title}"
                     required>
            </div>

            <!-- 내용 -->
            <div class="mb-3">
              <label class="form-label">내용</label>
              <textarea name="no_content"
                        class="form-control"
                        rows="6"
                        required>${notice.no_content}</textarea>
            </div>

            <!-- 현재 파일 -->
            <div class="mb-3">
              <label class="form-label">현재 파일</label><br>
              <c:if test="${not empty notice.no_upload}">
                <span class="text-muted">${notice.no_upload}</span>
              </c:if>
              <c:if test="${empty notice.no_upload}">
                <span class="text-muted">없음</span>
              </c:if>
            </div>

            <!-- 파일 변경 -->
            <div class="mb-3">
              <label class="form-label">파일 변경</label>
              <input type="file" name="file" class="form-control">
            </div>

            <!-- 버튼 -->
            <div class="d-flex justify-content-center gap-3 mt-4">
              <button type="submit" class="btn btn-orange px-4">수정</button>
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
