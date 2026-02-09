<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오늘 뭐 먹지?</title>
<link rel="stylesheet" href="/css/home.css">

</head>
<div id="layoutWrapper">
<body>
<div class="home-center">

		<div class="roulette-wrapper">
		
		<button id="spinBtn" class="spin-start-btn spin-in-wheel" type="button"
		        onclick="spin()" aria-label="룰렛 돌리기">
		  <span>START</span>
		</button>

		
		  <div id="pointer"></div>
		  <div id="roulette"></div>
		
		  <div class="center-result-box" id="centerResultBox">
		    <div class="center-result" id="centerResult"></div>
		  </div>
		</div>

    <!-- ✅ 퀵바를 home-center "안"으로 이동 -->
    <div id="topKeywordQuickbar">
      <h5>🔥 인기 검색어 TOP 10</h5>
      <ul>
        <c:forEach var="item" items="${topKeywords}" varStatus="status">
          <li class="topkw-item" data-keyword="${item.s_keyword}" data-rank="${status.index + 1}">
            ${item.s_keyword}
          </li>
        </c:forEach>
      </ul>
    </div>
  </div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>

<script src="${path}/js/roulette.js"></script>
<script src="${path}/js/favQuick.js"></script>

</body>
</html>
