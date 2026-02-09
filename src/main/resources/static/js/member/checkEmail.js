function checkEmail() {
  const emailEl = document.getElementById("m_email");
  const resultEl = document.getElementById("emailResult");

  const email = (emailEl.value || "").trim();
  if (!email) {
    resultEl.textContent = "이메일을 입력해주세요.";
    resultEl.style.color = "red";
    emailEl.focus();
    return;
  }

  fetch(`/member/checkEmail?m_email=${encodeURIComponent(email)}`)
    .then(res => res.json())
    .then(isAvailable => {
      if (isAvailable) {
        resultEl.textContent = "사용 가능한 이메일입니다.";
        resultEl.style.color = "green";
      } else {
        resultEl.textContent = "이미 가입된 이메일입니다.";
        resultEl.style.color = "red";
      }
    })
    .catch(() => {
      resultEl.textContent = "확인 중 오류가 발생했습니다.";
      resultEl.style.color = "red";
    });
}
