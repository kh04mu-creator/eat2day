// ✅ 새 페이지 로딩 후 오버레이를 부드럽게 걷어냄
  window.addEventListener("DOMContentLoaded", () => {
    const overlay = document.getElementById("page-transition");
    if (!overlay) return;

    overlay.classList.add("enter");
    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        overlay.classList.remove("enter");
      });
    });
  });

  // bfcache(뒤로가기)에서도 오버레이가 남지 않게
  window.addEventListener("pageshow", () => {
    const overlay = document.getElementById("page-transition");
    if (!overlay) return;
    overlay.classList.remove("on");
    overlay.classList.remove("enter");
  });