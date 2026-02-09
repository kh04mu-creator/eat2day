function submitSearch() {
    	  const form = document.getElementById("searchbar");

    	  // ✅ id가 꼬일 경우 대비: form 안의 name=keyword를 우선 찾기
    	  const input = form ? form.querySelector('input[name="keyword"]') : null;

    	  if (!form || !input) {
    	    console.warn("searchbar/input not found");
    	    return;
    	  }

    	  const keyword = (input.value || "").trim();
    	  if (!keyword || keyword.toLowerCase() === "null") {
    	    alert("검색어를 입력하세요");
    	    input.focus();
    	    return;
    	  }

    	  // keyword 확정 재주입
    	  input.value = keyword;
    	  
    	// 검색어 기록
          const data = new URLSearchParams();
		    data.append("keyword", keyword);
		    navigator.sendBeacon("/search/record", data);
    	  
    	 
    	  const latEl = form.querySelector('#lat');
    	  const lonEl = form.querySelector('#lon');

    	  if (!navigator.geolocation) {
    	    latEl.value = 35.1479;
    	    lonEl.value = 129.0596;
    	    form.submit();
    	    return;
    	  }

    	  navigator.geolocation.getCurrentPosition(
    	    pos => {
    	      latEl.value = pos.coords.latitude;
    	      lonEl.value = pos.coords.longitude;
    	      form.submit();
    	    },
    	    () => {
    	      latEl.value = 35.1479;
    	      lonEl.value = 129.0596;
    	      form.submit();
    	    }
    	  );
    	}

	
	  function moveWithDefaultLocation(keyword) {
	    const form = document.getElementById("searchbar");
	    const input = document.getElementById("headerKeyword");
	    const latEl = document.getElementById("lat");
	    const lonEl = document.getElementById("lon");
	
	    if (!form || !input || !latEl || !lonEl) return;
	
	    input.value = (keyword || "").trim();
	    latEl.value = 35.1479;   // ✅ 범내골역 기본좌표
	    lonEl.value = 129.0596;
	    form.submit();
	  }