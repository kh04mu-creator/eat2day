document.addEventListener("DOMContentLoaded", function() {

    const quickbar = document.getElementById("topKeywordQuickbar");
    const offsetTop = 150;

    if (quickbar) {
        window.addEventListener("scroll", () => {
            let scrollTop = window.scrollY || document.documentElement.scrollTop;
            quickbar.style.top = (offsetTop + scrollTop) + "px";
        });
    }

    quickbar?.addEventListener("click", (e) => {
        const li = e.target.closest("li");
        if (!li) return;

        // ✅ 번호 제거
        const text = li.textContent || "";
        const keyword = text.replace(/^\s*\d+\.\s*/, "").trim();
        if (!keyword) return;

        const form = document.getElementById("searchbar");
        const input = form?.querySelector('input[name="keyword"]');

        if (!form || !input) return;

        input.value = keyword;

        if (typeof window.submitSearch === "function") {
            window.submitSearch();
        } else {
            form.submit();
        }
    });
});
