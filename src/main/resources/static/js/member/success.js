document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);

  if (params.get("signup") === "success") {
    // ✅ 로그인 패널로 전환 (회원가입 패널 열려있을 수 있음)
    const container = document.getElementById("authContainer");
    if (container) container.classList.remove("right-panel-active");

    const toastEl = document.getElementById("signupToast");
    if (toastEl && window.bootstrap && bootstrap.Toast) {
      // ✅ 전환 애니메이션 끝나고 토스트 띄우기
      setTimeout(() => {
        new bootstrap.Toast(toastEl, {
          delay: 3000
        }).show();
      }, 150);
    }

    // ✅ 새로고침 시 다시 안 뜨게
    history.replaceState(null, "", window.location.pathname);
  }
});

