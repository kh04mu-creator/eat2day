function mcheck(){
	   let expPw = /^[A-Za-z0-9!@#$%_]{8,12}$/;
	   let expMtel = /^\d{2,3}-\d{3,4}-\d{4}$/;
	   let expMname = /^[가-힣]+$/;
	   let expBirth = /^[0-9]{8}$/;
	   
	   let f = document.memberWriteForm;
	   
	   if(!f.m_pw.value || !expPw.test(f.m_pw.value)){
		   alert("비밀번호는 8~12자리 영문, 숫자, 특수문자(!@#$%_)만 가능합니다.")
		   f.m_pw.focus(); return false;
	   }
	   if(!f.m_name.value || !expMname.test(f.m_name.value)){
		      alert("이름은 한글만 입력하세요.");
		      f.m_name.focus(); return false;
		}
	   if(!f.m_birth.value || !expBirth.test(f.m_birth.value)){
		      alert("생년월일은 8자리 숫자로 입력하세요.");
		      f.m_birth.focus(); return false;
		   }
	   if(!f.m_tel.value || !expMtel.test(f.m_tel.value)){
		      alert("연락처 형식을 올바르게 입력하세요\n010-0000-0000");
		      f.m_tel.focus(); return false;
		   }
	   if(!f.m_addr.value){
		   alert("주소를 입력해주세요.");
		   return false;
	   }
		f.submit();
	   }