<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="/css/admin/dashboard.css">

<div id="layoutWrapper">


 <div class="title_size">
	<div class="title_inner">
		<span class="title_font">관리자 센터</span>
	</div>
</div>
<div class="container mt-4 admin-wrap">
  <!-- 상단 요약 4개 -->
  <div class="row g-3 mb-4">
    <div class="col-12 col-md-6 col-lg-3">
      <div class="stat-card">
        <div class="stat-label">전체 회원수</div>
        <div class="stat-value"><c:out value="${totalMembers}"/>명</div>
      </div>
    </div>

    <div class="col-12 col-md-6 col-lg-3">
      <div class="stat-card">
        <div class="stat-label">오늘 신규가입</div>
        <div class="stat-value"><c:out value="${todayNewMembers}"/>명</div>
      </div>
    </div>

    <div class="col-12 col-md-6 col-lg-3">
      <div class="stat-card">
        <div class="stat-label">새로운 게시물</div>
        <div class="stat-value"><c:out value="${todayNewPosts}"/>건</div>
      </div>
    </div>

    <div class="col-12 col-md-6 col-lg-3">
      <div class="stat-card">
        <div class="stat-label">QnA</div>
        <div class="stat-value"><c:out value="${qnaCount}"/>건</div>
      </div>
    </div>
  </div>

  <!-- 하단 메뉴 3개 -->
  <div class="row g-3">
    <div class="col-12 col-md-4">
      <a class="card-link" href="/admin/memberList">
        <div class="menu-card">
          <div class="menu-title">회원관리</div>
          <div class="menu-desc">회원 목록 조회 / 상세 확인</div>
        </div>
      </a>
    </div>

    <div class="col-12 col-md-4">
      <a class="card-link" href="${path}/admin/qnaManage">
        <div class="menu-card">
          <div class="menu-title">QnA 관리</div>
          <div class="menu-desc">문의글 확인 / 답변 작성</div>
        </div>
      </a>
    </div>

    <div class="col-12 col-md-4">
      <a class="card-link" href="/admin/noticeWriteForm">
        <div class="menu-card">
          <div class="menu-title">공지사항 관리</div>
          <div class="menu-desc">공지 등록 / 수정 / 삭제</div>
        </div>
      </a>
    </div>
  </div>
  
   <!-- 하단 메뉴 3개 -->
 <div class="row g-3">
  <div class="col-12 col-md-4">
      <a class="card-link" href="/admin/rouletteList">
        <div class="menu-card">
          <div class="menu-title">룰렛</div>
          <div class="menu-desc">룰렛 음식 추가</div>
        </div>
      </a>
    </div>
    
    <div class="col-12 col-md-4">
      <a class="card-link" href="/admin/listRestaurant">
        <div class="menu-card">
          <div class="menu-title">식당 관리</div>
          <div class="menu-desc">식당 등록 / 수정 / 삭제</div>
        </div>
      </a>
    </div>
    
    <div class="col-12 col-md-4">
      <a class="card-link" href="http://192.168.10.31:5601/app/dashboards#/view/850cdd30-02f2-11f1-b604-4de094359758?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(),gridData:(h:14,i:'3a818a7f-8c27-42e8-abee-84ad06e1d496',w:15,x:0,y:0),id:'1026e750-026a-11f1-8bb1-b3a817ecb4ea',panelIndex:'3a818a7f-8c27-42e8-abee-84ad06e1d496',type:lens,version:'7.10.1'),(embeddableConfig:(),gridData:(h:14,i:'0f8d5129-9e65-48b1-98a7-c7d472b4030f',w:14,x:15,y:0),id:'70ce8e50-026a-11f1-8bb1-b3a817ecb4ea',panelIndex:'0f8d5129-9e65-48b1-98a7-c7d472b4030f',type:lens,version:'7.10.1'),(embeddableConfig:(),gridData:(h:14,i:'08e229b8-5fb0-40fd-a53e-2c0f3c50a951',w:13,x:29,y:0),id:'6a281930-02f2-11f1-b604-4de094359758',panelIndex:'08e229b8-5fb0-40fd-a53e-2c0f3c50a951',type:visualization,version:'7.10.1'),(embeddableConfig:(),gridData:(h:12,i:'79dc15b9-d409-4c36-a7b1-d83c5b57b35c',w:15,x:0,y:14),id:bf809520-026a-11f1-8bb1-b3a817ecb4ea,panelIndex:'79dc15b9-d409-4c36-a7b1-d83c5b57b35c',type:visualization,version:'7.10.1'),(embeddableConfig:(),gridData:(h:12,i:'4482e436-0e84-45ce-a862-a4957eadc9a7',w:14,x:15,y:14),id:ddd0c070-02f3-11f1-b604-4de094359758,panelIndex:'4482e436-0e84-45ce-a862-a4957eadc9a7',type:visualization,version:'7.10.1'),(embeddableConfig:(),gridData:(h:12,i:f1bd0521-526f-42af-ac5c-09804b4ef5d8,w:13,x:29,y:14),id:'59198b60-02f7-11f1-b604-4de094359758',panelIndex:f1bd0521-526f-42af-ac5c-09804b4ef5d8,type:visualization,version:'7.10.1')),query:(language:kuery,query:''),timeRestore:!f,title:eat2day,viewMode:edit)">
        <div class="menu-card">
          <div class="menu-title">통계</div>
          <div class="menu-desc">검색어 및 가입자 통계</div>
        </div>
      </a>
    </div>
  </div>


</div>


<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>