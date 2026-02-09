//공용 상태
window.emailVerified = false;
window.nicknameChecked = false;

// SUBMIT 버튼 제어
window.checkSubmitEnable = function () {
    const submitBtn = document.querySelector("input[value='SUBMIT']");
    if (!submitBtn) return;

    submitBtn.disabled = !(emailVerified && nicknameChecked);
};