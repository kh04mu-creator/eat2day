<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<script src="/js/member/memberState.js"></script>
<script src="/js/member/EmailAuth.js"></script>
<script src="/js/member/checkNick.js"></script>
<script src="/js/member/memberWrite.js"></script>
<script src="/js/member/juso.js"></script>
<script src="/js/member/success.js"></script>
<script src="/js/member/checkEmail.js"></script>


<link rel="stylesheet" href="/css/memberWrite.css"/>

</head>

<body>
<div id="layoutWrapper">
<div class="auth-container" id="authContainer">

    <!-- ✅ 회원가입 폼 (전환 후 오른쪽에 보임) -->
    <div class="auth-form sign-up">
    <h2>REGISTER</h2>

    <form name="memberWriteForm" method="post" action="/memberWrite" onsubmit="return mcheck();">
        <input type="hidden"
               name="${_csrf.parameterName}"
               value="${_csrf.token}" />

        <label>이메일</label>

		<div class="email-row">
		  <input type="text" name="m_email" id="m_email" placeholder="email@example.com">
		  <input type="button" value="중복확인" onclick="checkEmail();">
		</div>
		
		<span id="emailResult"></span>
		
		<!-- ✅ 이메일 인증 + 타이머 (같은 줄) -->
		<div class="email-auth-row">
		  <input type="button" value="이메일 인증" onclick="sendEmail();">
		  <span id="verifyTimer" class="verify-timer-inline"></span>
		</div>


        
        <!-- 인증번호 입력 (처음엔 숨김) -->
		<div id="verifyArea">
		  <label>인증번호</label>
		
		  <div class="verify-row">
		    <input type="text" id="e_code" placeholder="인증번호를 입력하세요">
		    <input type="button" onclick="verifyCode();" value="확인">
		  </div>
		
		  <span id="verifyResult"></span>
		</div>


        <label>비밀번호</label>
        <input type="password" name="m_pw" id="m_pw">

        <label>이름</label>
        <input type="text" name="m_name" id="m_name">

       <label>닉네임</label>

		<div class="nickname-row">
		  <input type="text"
		         name="m_nickname"
		         id="m_nickname"
		         placeholder="닉네임을 입력하세요">
		  <input type="button"
		         value="중복확인"
		         onclick="checkNickname();">
		</div>
		
		<span id="nickResult"></span>


        <label>성별</label>
        <div class="radio-group">
            <label><input type="radio" name="m_gender" value="남" checked> 남</label>
            <label><input type="radio" name="m_gender" value="여"> 여</label>
        </div>

        <label>생년월일</label>
        <input type="text" name="m_birth" id="m_birth" placeholder="20010101">

        <label>연락처</label>
        <input type="text" name="m_tel" id="m_tel" placeholder="010-0000-0000">

        <label>주소</label>
        <input type="button" value="주소 찾기" onclick="goPopup()">
        <input type="text" id="roadFullAddr" name="m_addr" placeholder="도로명 주소">

        <div class="btn-group">
            <input type="submit" value="SUBMIT" disabled>
            <input type="reset" value="RESET">
        </div>
    </form>
</div>


    <!-- ✅ 로그인 폼 (처음엔 왼쪽) -->
    <div class="auth-form sign-in">
        <h2>LOGIN</h2>
        
        <!-- ✅ 회원가입 성공 Toast (로그인 카드 내부) -->
	<div class="toast-container position-relative p-0" style="z-index: 5;">
	  <div id="signupToast"
	       class="toast align-items-center border-0 shadow mb-3"
	       role="alert"
	       aria-live="assertive"
	       aria-atomic="true"
	       style="
	         background: linear-gradient(135deg, #ff7a18, #ffb347);
	         color: #fff;
	         border-radius: 14px;
	         font-weight: 700;
	       ">
	       
	       <div class="d-flex align-items-center">
			  <div class="toast-body text-center w-100">
			    가입성공! eat2day와 맛있는 하루 시작해요♡
			  </div>
			</div>
	  </div>
	</div>
        

        <c:if test="${param.error != null}">
            <div class="alert">이메일 또는 비밀번호가 틀렸습니다.</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}" />

            <label>이메일</label>
            <input type="text" name="userId" placeholder="email">

            <label>비밀번호</label>
            <input type="password" name="userPw" placeholder="password">

            <button type="submit" class="btn">LOGIN</button>
            
            <!-- ✅ 소셜 로그인 -->
			<div class="social-divider">
			  <span>또는</span>
			</div>
			
			<div class="social-login">
			 <a class="social-btn kakao"
				   href="${pageContext.request.contextPath}/oauth2/authorization/kakao">
				  <span class="social-icon kakao"></span>
				  카카오톡으로 로그인
				</a>
				
				<a class="social-btn google"
				   href="${pageContext.request.contextPath}/oauth2/authorization/google">
				  <span class="social-icon google"></span>
				  구글로 로그인
				</a>

			</div>
            
        </form>
    </div>

    <!-- ✅ 오버레이(그라데이션 카드) -->
    <div class="overlay-container">
        <div class="overlay">

            <!-- 전환 후: 왼쪽에 위치 -->
            <div class="overlay-panel overlay-left">
                <div class="login-icon">
                    <svg width="48" height="48" viewBox="0 0 24 24" fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                        <circle cx="12" cy="8" r="4" fill="white"/>
                        <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="white"/>
                    </svg>
                </div>
                <h2>Welcome back!</h2>
                <p>이미 계정이 있나요?<br>로그인해서 계속하세요</p>
                <button type="button" class="ghost" id="btnToLogin">LOGIN</button>
            </div>

            <!-- 처음: 오른쪽에 위치 -->
            <div class="overlay-panel overlay-right">
                <div class="login-icon">
                    <svg width="48" height="48" viewBox="0 0 24 24" fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                        <circle cx="12" cy="8" r="4" fill="white"/>
                        <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="white"/>
                    </svg>
                </div>

                <h2>Hello, friend!</h2>
                <p>Enter your personal details<br>and start journey with us</p>
                <button type="button" class="ghost" id="btnToRegister">REGISTER</button>
            </div>

        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>


<script>
const container = document.getElementById('authContainer');
document.getElementById('btnToRegister').addEventListener('click', () => {
    container.classList.add('right-panel-active');
});
document.getElementById('btnToLogin').addEventListener('click', () => {
    container.classList.remove('right-panel-active');
});

function goPopup(){
	  window.open("/jusoPopup","pop","width=570,height=420, scrollbars=yes, resizable=yes");
	}
	function jusoCallBack(roadFullAddr){
	  document.getElementById("roadFullAddr").value = roadFullAddr;
	}
</script>


</body>
</html>
