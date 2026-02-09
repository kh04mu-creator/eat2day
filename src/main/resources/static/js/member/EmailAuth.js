// ✅ 이메일 인증 상태(너 코드에서 이미 쓰고 있는 변수)
let emailVerified = false;

// ✅ 타이머 관련
let verifyTimerInterval = null;
let verifyExpireAt = 0; // 만료 시각(ms)

// ✅ (선택) 이미 만들었던 함수가 있다면 그대로 사용됨
// checkSubmitEnable()는 기존 memberWrite.js 쪽에 있을 가능성이 큼

function formatTime(ms) {
  const totalSec = Math.floor(ms / 1000);
  const mm = String(Math.floor(totalSec / 60)).padStart(2, "0");
  const ss = String(totalSec % 60).padStart(2, "0");
  return `${mm}:${ss}`;
}

function startVerifyTimer(minutes = 5) {
  const timerEl = document.getElementById("verifyTimer");
  if (!timerEl) return;

  // 만료 시간 설정
  verifyExpireAt = Date.now() + minutes * 60 * 1000;

  // 기존 타이머 종료
  if (verifyTimerInterval) clearInterval(verifyTimerInterval);

  const tick = () => {
    const remain = verifyExpireAt - Date.now();

    if (remain <= 0) {
      clearInterval(verifyTimerInterval);
      verifyTimerInterval = null;

      timerEl.textContent = "인증시간이 만료되었습니다. 이메일 인증을 다시 요청해주세요.";
      timerEl.style.color = "red";

      // 만료되면 인증상태 false
      emailVerified = false;
      if (typeof checkSubmitEnable === "function") checkSubmitEnable();
      return;
    }

    timerEl.textContent = `남은 시간 ${formatTime(remain)}`;
    timerEl.style.color = "#ff3b30";
  };

  tick();
  verifyTimerInterval = setInterval(tick, 1000);
}

function resetVerifyUI() {
  const resultEl = document.getElementById("verifyResult");
  const timerEl = document.getElementById("verifyTimer");
  const codeEl = document.getElementById("e_code");

  if (resultEl) {
    resultEl.innerText = "";
    resultEl.style.color = "";
  }
  if (timerEl) {
    timerEl.innerText = "";
    timerEl.style.color = "";
  }
  if (codeEl) codeEl.value = "";
}

function showVerifyArea() {
  const area = document.getElementById("verifyArea");
  if (area) area.style.display = "block";
}

function sendEmail() {
  const emailInput = document.querySelector("input[name=m_email]");
  const email = (emailInput?.value || "").trim();

  if (email === "") {
    alert("이메일을 입력하세요.");
    emailInput?.focus();
    return;
  }

  // 간단한 이메일 형식 체크
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    alert("이메일 형식이 올바르지 않습니다.");
    emailInput?.focus();
    return;
  }

  // ✅ 매번 재요청할 때 초기화
  emailVerified = false;
  if (typeof checkSubmitEnable === "function") checkSubmitEnable();
  resetVerifyUI();

  // ✅ CSRF (meta에서 읽기)
  const csrfToken = document.querySelector("meta[name=_csrf]")?.content;
  const csrfHeader = document.querySelector("meta[name=_csrf_header]")?.content;

  fetch("/email/send", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      ...(csrfToken && csrfHeader ? { [csrfHeader]: csrfToken } : {})
    },
    body: "e_email=" + encodeURIComponent(email)
  })
    .then(res => {
      if (!res.ok) throw new Error("메일 발송 실패");
      return res.text();
    })
    .then(() => {
      // ✅ 인증번호 영역 보여주기 + 5분 타이머 시작
      showVerifyArea();
      startVerifyTimer(5);

      // UX: 인증번호 입력칸 포커스
      const codeEl = document.getElementById("e_code");
      if (codeEl) codeEl.focus();

      alert("인증메일이 발송되었습니다.");
    })
    .catch(err => {
      alert("메일 발송에 실패했습니다.");
      console.error(err);
    });
}

function verifyCode() {
  const email = (document.querySelector("input[name=m_email]")?.value || "").trim();
  const code = (document.getElementById("e_code")?.value || "").trim();

  if (!code) {
    alert("인증번호를 입력하세요.");
    return;
  }

  // ✅ 만료 체크 (클라이언트 기준)
  if (verifyExpireAt && Date.now() > verifyExpireAt) {
    const resultEl = document.getElementById("verifyResult");
    const timerEl = document.getElementById("verifyTimer");

    if (resultEl) {
      resultEl.innerText = "인증시간이 만료되었습니다. 다시 요청해주세요.";
      resultEl.style.color = "red";
    }
    if (timerEl) {
      timerEl.innerText = "인증시간이 만료되었습니다. 이메일 인증을 다시 요청해주세요.";
      timerEl.style.color = "red";
    }
    emailVerified = false;
    if (typeof checkSubmitEnable === "function") checkSubmitEnable();
    return;
  }

  const csrfToken = document.querySelector("meta[name=_csrf]")?.content;
  const csrfHeader = document.querySelector("meta[name=_csrf_header]")?.content;

  fetch("/email/verify", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      ...(csrfToken && csrfHeader ? { [csrfHeader]: csrfToken } : {})
    },
    body:
      "e_email=" + encodeURIComponent(email) +
      "&e_code=" + encodeURIComponent(code)
  })
    .then(res => {
      if (!res.ok) throw new Error("인증 실패");
      return res.text();
    })
    .then(() => {
      const resultEl = document.getElementById("verifyResult");
      const timerEl = document.getElementById("verifyTimer");

      if (resultEl) {
        resultEl.innerText = "인증 완료";
        resultEl.style.color = "green";
      }

      // ✅ 인증 완료되면 타이머 멈추고 문구 제거
      if (verifyTimerInterval) clearInterval(verifyTimerInterval);
      verifyTimerInterval = null;
      verifyExpireAt = 0;
      if (timerEl) timerEl.innerText = "";

      // 가입 버튼 활성화
      emailVerified = true;
      if (typeof checkSubmitEnable === "function") checkSubmitEnable();
    })
    .catch(err => {
      const resultEl = document.getElementById("verifyResult");
      if (resultEl) {
        resultEl.innerText = "❌ 인증 실패";
        resultEl.style.color = "red";
      }
      console.error(err);
    });
}
