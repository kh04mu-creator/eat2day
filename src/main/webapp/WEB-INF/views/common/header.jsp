<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>eat2day</title>

<!-- SB Admin CSS -->
<link href="${path}/css/styles.css" rel="stylesheet">

<!-- FontAwesome -->
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="/css/header.css">
</head>

<body class="sb-nav-fixed sb-sidenav-toggled">

<!-- 🔶 Top Navbar -->
<nav class="sb-topnav navbar navbar-expand navbar-dark">

  <!-- ☰ 사이드바 토글 (왼쪽) -->
  <button class="btn btn-link btn-sm order-1 order-lg-0 me-3" id="sidebarToggle">
    <i class="fas fa-bars"></i>
  </button>

  <!-- 🔶 가운데 브랜드 -->
 <div class="mx-auto navbar-brand-wrapper">
  <a class="navbar-brand logo-link" href="${path}/">
    <img src="${path}/images/eat2day_logo.png" alt="eat2day 로고" class="top-logo">
  </a>
</div>
  <!-- 👤 로그인/유저 (오른쪽 끝) -->
  <ul class="navbar-nav ms-auto me-3 me-lg-4 align-items-center">

    <!-- ✅ 로그인 상태 -->
    <sec:authorize access="isAuthenticated()">
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle d-flex align-items-center gap-2"
           href="#"
           role="button"
           data-bs-toggle="dropdown"
           aria-expanded="false">
          <i class="fas fa-user fa-fw"></i>
			<span class="nav-nickname">
			  <sec:authentication property="principal.nickname"/>
			</span>
			<span class="nav-nim">님</span>
        </a>

        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" href="${path}/member/memberDetail">내정보</a></li>
          <li><hr class="dropdown-divider"></li>
          <li>
            <form method="post" action="${path}/logout" style="margin:0;">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
              <button type="submit" class="dropdown-item">Logout</button>
            </form>
          </li>
        </ul>
      </li>
    </sec:authorize>

    <!-- ✅ 비로그인 상태 -->
    <sec:authorize access="isAnonymous()">
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle anon-user"
           href="#"
           role="button"
           data-bs-toggle="dropdown"
           aria-expanded="false">
          <i class="fas fa-user fa-fw"></i>
        </a>

        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" href="${path}/memberWriteForm">Login</a></li>
        </ul>
      </li>
    </sec:authorize>

  </ul>
</nav>


<div class="searchbar-mask"></div>


<!-- 🔍 Search Bar Section -->
<div class="search-bar-wrapper">
  <div class="container-fluid px-4">
    <form class="search-bar" name="searchbar" id="searchbar" 
    	method="get" action="${path}/searchList" 
    	accept-charset="UTF-8"
    	onsubmit="event.preventDefault(); submitSearch();">

      <div class="search-input-wrap">
        <input type="text"
               class="form-control"
               id="headerKeyword"
               name="keyword"
               placeholder="음식점, 메뉴를 검색해보세요" />
        <input type="hidden" id="lat" name="lat">
        <input type="hidden" id="lon" name="lon">

        <i class="fas fa-search search-inside-icon" onclick="submitSearch();"></i>
        <div id="suggestions" class="autocompleteList"></div>
      </div>
    </form>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/header/autocomplete.js"></script>
<script src="/js/header/searchbar.js"></script>

    </div>
</div>

<div id="layoutSidenav">

  <!-- 🔷 사이드바 -->
  <div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark">
      <div class="sb-sidenav-menu">
        <div class="nav">

          <a class="nav-link sidebar-logo-link" href="${path}/">
			  <img src="${path}/images/eat2day_logo_side.png"
			       alt="eat2day"
			       class="sidebar-logo">
			</a>


          <a class="nav-link" href="${path}/member/noticeList">
            <div class="sb-nav-link-icon"><i class="fas fa-bullhorn"></i></div>
            공지사항
          </a>

          <a class="nav-link" href="${path}/member/qnaList">
            <div class="sb-nav-link-icon"><i class="fas fa-comments"></i></div>
            QnA
          </a>

          <sec:authorize access="hasRole('ADMIN')">
            <a class="nav-link" href="${path}/admin/dashboard">
              <div class="sb-nav-link-icon"><i class="fas fa-gear"></i></div>
              관리자 센터
            </a>
          </sec:authorize>

          <sec:authorize access="hasRole('USER') or hasRole('ADMIN')">
            <a class="nav-link" href="${path}/member/memberDetail">
              <div class="sb-nav-link-icon"><i class="fas fa-user"></i></div>
              내정보
            </a>
          </sec:authorize>

        </div>
      </div>
    </nav>
  </div>

  <!-- 🔷 본문 시작 -->
  <div id="layoutSidenav_content">
    <main class="container-fluid px-4">

<!-- Bootstrap core JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>

<!-- SB Admin JS -->
<script src="${path}/js/scripts.js"></script>
