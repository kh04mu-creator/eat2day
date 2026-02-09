<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오늘의 룰렛 결과</title>
</head>
<body>
<h2>🎯 오늘의 룰렛 결과</h2>

<c:if test="${not empty result}">
    <h1>${result.ro_keyword}</h1>
</c:if>

</body>
</html>