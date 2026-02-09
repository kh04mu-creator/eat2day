(function(){
	$("#headerKeyword").on("keyup", function(){
	    let q = $(this).val();
	
	    if(q.length < 1){
	        $("#suggestions").empty();
	        return;
	    }
	
	    $.ajax({
	        url: "/autocomplete",
	        data: { keyword: q },
			success: function(res) {
			    console.log("1. 서버 응답 배열 길이:", res.length);
			    const list = Array.isArray(res) ? res : [];
			    const seen = new Set();
			    let html = "";
			    
			    const q = ($("#headerKeyword").val() || "").trim().toLowerCase();
			
			    list.forEach((item, idx) => {
			        const rawContent = item.category_name || item.categoryName || "";
			        
			        if (!rawContent) {
			            console.log(idx + "번 아이템에 category_name이 없음", item);
			            return;
			        }
			
			        const parts = rawContent.split(/\s+>\s+/).map(p => p.trim());
			
			        parts.forEach(part => {
			            const lowerPart = part.toLowerCase();
			
			            if (part.includes('<em') || lowerPart.includes(q) || q.length === 0) {
			                
			                const pureText = part.replace(/<\/?[^>]+(>|$)/g, "").trim();
			                
			                if (pureText && !seen.has(pureText)) {
			                    seen.add(pureText);
			                    html += '<div class="item">' + part + '</div>';
			                }
			            }
			        });
			    });
			
			    console.log("3. 최종 생성된 HTML 결과물:", html);
			
			    if (html !== "") {
			        $("#suggestions").html(html).show();
			    } else {
			        console.log("출력할 HTML이 생성되지 않았습니다. 조건을 확인하세요.");
			        $("#suggestions").hide();
			    }
			},
	        error: function(){
	            console.log("autocomplete error");
	        }
	    });
	});

	// 추천어 클릭 시 검색창에 채움
	$(document).on("click",".item",function(){
		console.log("clicked:", $(this).html());
		
		const keyword = $(this).find(".highlight").text() || $(this).text();
	    // <em> 태그 제거 후 input에 넣기
	    $("#headerKeyword").val($(this).text());
	    $("#suggestions").empty();
	});
})();