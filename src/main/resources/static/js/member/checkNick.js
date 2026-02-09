let nicknameChecked = false;

function checkNickname() {
	const nickname = document.querySelector("input[name=m_nickname]").value.trim();
    const resultSpan = document.getElementById("nickResult");

    if (nickname === "") {
        alert("닉네임을 입력하세요.");
        return;
    } 
    
    fetch("/member/checkNickname?m_nickname=" + encodeURIComponent(nickname))
    .then(res => res.json())
    .then(isAvailable => {
        const span = document.getElementById("nickResult");

        if (isAvailable) {
            span.innerText = "사용 가능한 닉네임입니다.";
            span.style.color = "green";
            nicknameChecked = true;
        } else {
            span.innerText = "이미 사용 중인 닉네임입니다.";
            span.style.color = "red";
            nicknameChecked = false;
        }

        checkSubmitEnable();
    })
    .catch(() => {
        alert("닉네임 확인 중 오류가 발생했습니다.");
    });
}