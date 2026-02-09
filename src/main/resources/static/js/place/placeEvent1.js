window.addEventListener("DOMContentLoaded", () => {
    const overlay = document.getElementById("page-transition");
    if (!overlay) return;

    // ✅ 처음엔 켜져있고(enter), 잠깐 유지했다가 걷는다
    setTimeout(() => {
      overlay.classList.remove("enter");
      overlay.style.opacity = "";      // inline style 제거
      overlay.style.background = "";   // inline style 제거
    }, 120); // 0.12초 정도면 자연스러움
  });

  window.addEventListener("pageshow", () => {
    const overlay = document.getElementById("page-transition");
    if (!overlay) return;
    overlay.classList.remove("on");
    overlay.classList.remove("enter");
    overlay.style.opacity = "";
    overlay.style.background = "";
  });