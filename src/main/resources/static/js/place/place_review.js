document.addEventListener("DOMContentLoaded", () => {
  // 로그인 폼 별점
  const reviewForm = document.getElementById("reviewForm");
  if (reviewForm) {
    const group = reviewForm.querySelector(".star-input");
    const ratingInput = reviewForm.querySelector("input[name='rating']");
    const stars = group ? Array.from(group.querySelectorAll("span")) : [];

    // ⭐ 기본 5점 세팅 (중요: hidden input도 같이!)
    const DEFAULT_RATING = 5;
    if (ratingInput) ratingInput.value = DEFAULT_RATING;
    stars.forEach((s, i) => {
      s.textContent = i < DEFAULT_RATING ? "★" : "☆";
      s.classList.toggle("active", i < DEFAULT_RATING);
    });

    // 클릭 시 변경
    stars.forEach((star, idx) => {
      star.addEventListener("click", () => {
        const value = idx + 1;
        if (ratingInput) ratingInput.value = value;

        stars.forEach((s, i) => {
          s.textContent = i < value ? "★" : "☆";
          s.classList.toggle("active", i < value);
        });
      });
    });
  }

  // 비로그인 폼
  const fakeForm = document.getElementById("fakeReviewForm");
  if (fakeForm) {
    const group = fakeForm.querySelector(".star-input");
    const stars = group ? Array.from(group.querySelectorAll("span")) : [];

    // 비로그인도 기본 5점처럼 보이게(선택사항)
    const DEFAULT_RATING = 5;
    stars.forEach((s, i) => {
      s.textContent = i < DEFAULT_RATING ? "★" : "☆";
      s.classList.toggle("active", i < DEFAULT_RATING);
    });

    stars.forEach((star, idx) => {
      star.addEventListener("click", () => {
        const value = idx + 1;
        stars.forEach((s, i) => {
          s.textContent = i < value ? "★" : "☆";
          s.classList.toggle("active", i < value);
        });
      });
    });

    fakeForm.addEventListener("submit", (e) => {
      e.preventDefault();
      const go = confirm("리뷰를 작성하려면 로그인이 필요합니다.\n로그인 페이지로 이동할까요?");
      if (go) {
        const returnUrl = encodeURIComponent(location.pathname + location.search);
        window.location.href = `${window.APP_PATH}/loginForm?returnUrl=${returnUrl}`;
      }
    });
  }
});
