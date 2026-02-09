<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="/css/admin/rouletteList.css">

<div id="layoutWrapper">
<div class="title_size">
	<div class="title_inner">
		<span class="title_font">룰렛 키워드 관리</span>
	</div>
</div>


<div class="container mt-5 admin-wrap">

  <div class="d-flex justify-content-end align-items-center mb-3">
	  <a href="${pageContext.request.contextPath}/admin/dashboard"
	     class="btn btn-outline-secondary btn-sm btn-dashboard">
	    관리자센터
	  </a>
	</div>

  <!-- ✅ 추가 폼 -->
  <div class="card-soft p-3 mb-3 shadow-sm">
    <form method="post" action="${path}/admin/rouletteInsert" class="d-flex gap-2 flex-wrap">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      <input type="text" name="ro_keyword" class="form-control"
             placeholder="추가할 키워드 입력 (예: 치킨)" style="max-width:420px;" required>
      <button type="submit" class="btn btn-orange btn-mini">추가</button>
    </form>
  </div>

  <!-- ✅ 리스트 -->
  <div class="card-soft shadow-sm">
    <div class="p-3">
      <div class="text-muted mb-2">총 <b>${fn:length(list)}</b>개</div>

      <c:if test="${empty list}">
        <div class="alert alert-light border mb-0">등록된 키워드가 없습니다.</div>
      </c:if>

      <c:if test="${not empty list}">
        <table class="table table-hover mb-0">
          <thead>
            <tr>
              <th style="width:110px;">번호</th>
              <th>키워드</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="r" items="${list}">
              <tr>
                <td><b>${r.ro_no}</b></td>

                <!--  수정 폼(인라인) -->
               <td>
				  <div class="d-flex gap-2 align-items-center">
				
				    <!-- ✅ 수정 폼: 바로 저장 -->
				    <form method="post"
				          action="${pageContext.request.contextPath}/admin/rouletteUpdate"
				          class="d-flex gap-2 align-items-center"
				          style="margin:0; flex:1;">
				      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				      <input type="hidden" name="ro_no" value="${r.ro_no}" />
				
				      <input type="text" name="ro_keyword"
				             class="form-control form-control-sm"
				             value="${r.ro_keyword}" required>
				
				      <button type="submit" class="btn btn-orange btn-mini">수정</button>
				    </form>
				
				    <!-- ✅ 삭제 폼: 별도 form (중첩 금지) -->
				    <form method="post"
				          action="${pageContext.request.contextPath}/admin/rouletteDelete"
				          onsubmit="return confirm('삭제할까요?');"
				          style="margin:0;">
				      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
				      <input type="hidden" name="ro_no" value="${r.ro_no}">
				      <button type="submit" class="btn btn-gray btn-mini">삭제</button>
				    </form>
				
				  </div>
				</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:if>

    </div>
  </div>

</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>