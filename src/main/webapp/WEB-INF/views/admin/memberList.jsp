<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 목록</title>
<link rel="stylesheet" href="/css/admin/memberList.css">
</head>

<body>
<div id="layoutWrapper">
<div class="title_size">
	<div class="title_inner">
		<span class="title_font">회원목록</span>
	</div>
</div>


<div class="container mt-4 admin-wrap">
  <!-- 상단 헤더 -->
	<div class="d-flex justify-content-end align-items-center mb-3">
	  <a href="${pageContext.request.contextPath}/admin/dashboard"
	     class="btn btn-outline-secondary btn-sm btn-dashboard">
	    관리자센터
	  </a>
	</div>


  <!-- 회원 테이블 카드 -->
  <div class="card shadow-sm border-0">
    <div class="card-body p-0">

      <table class="table table-hover align-middle mb-0">
        <thead class="table-light">
          <tr class="text-center">
            <th>번호</th>
            <th>이메일</th>
            <th>이름</th>
            <th>닉네임</th>
            <th>성별</th>
            <th>생년월일</th>
            <th>전화번호</th>            
            <th>가입일</th>
            <th>권한</th>
            <th>관리</th>
          </tr>
        </thead>

        <tbody>
          <c:forEach var="dto" items="${m_list}">
            <tr class="text-center">

              <td class="text-muted">${dto.m_no}</td>

              <!-- ✅ 이메일 클릭 → 회원 상세 -->
              <td>
                <a href="${pageContext.request.contextPath}/member/memberDetail?m_no=${dto.m_no}"
                   class="email-link">
                  ${dto.m_email}
                </a>
              </td>

              <td>${dto.m_name}</td>
              <td>${dto.m_nickname}</td>
              <td>${dto.m_gender}</td>
              <td>${dto.m_birth}</td>
              <td>${dto.m_tel}</td>             

              <td>
                <fmt:formatDate value="${dto.m_date}" pattern="yyyy-MM-dd" />
              </td>

              <td>
				  <c:choose>
				    <c:when test="${dto.m_auth == 'ROLE_ADMIN'}">
				      <span class="badge badge-admin">ADMIN</span>
				    </c:when>
				    <c:otherwise>
				      <span class="badge badge-user">USER</span>
				    </c:otherwise>
				  </c:choose>
				</td>


              <td>
                <form action="${pageContext.request.contextPath}/admin/member/memberDelete"
                      method="post"
                      onsubmit="return confirm('정말 이 회원을 강퇴하시겠습니까?');"
                      class="d-inline">

                  <input type="hidden"
                         name="${_csrf.parameterName}"
                         value="${_csrf.token}" />
                  <input type="hidden" name="m_no" value="${dto.m_no}" />

                  <button type="submit"
                          class="btn btn-outline-danger btn-sm">
                    강퇴
                  </button>
                </form>
              </td>

            </tr>
          </c:forEach>
        </tbody>
      </table>

    </div>
  </div>

</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
</body>
</html>
