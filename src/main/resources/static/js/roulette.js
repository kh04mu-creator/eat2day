// roulette.js
let currentRotation = Math.floor(Math.random() * 360);
let isSpinning = false;
let rouletteResult = null;
let isNavigating = false;

// 기본 위치 (권한 거부 시)
const DEFAULT_LAT = 35.148629689617;
const DEFAULT_LON = 129.05901534147;

/* =========================
   ✅ 시작 전(대기 상태) 팔레트 (6색)
========================= */
const START_PALETTE = [
  "#845EC2",
  "#D65DB1",
  "#FF6F91",
  "#FF9671",
  "#FFC75F",
  "#F9F871"
];

/* =========================
   ✅ 스핀 시 사용할 "톤업 빈티지" 팔레트들 (밝은 톤만)
========================= */
const VINTAGE_LIGHT_1 = [
  "#F8C3A6","#F5B58B","#F2A680","#F6C89A",
  "#F7D6B7","#F3B9A6","#F0AFA0","#F6BBAE",
  "#D9DBA8","#CFCF94","#E6E0AE","#D8D6B8",
  "#BFD6C8","#AFC9BA","#B8D0C5","#A9C0B5"
];

const VINTAGE_LIGHT_2 = [
  "#FFD2B8","#FFBF9C","#F6B08B","#FFD9C8",
  "#F7C6B8","#FFC8B0","#F3B8A8","#FFD0C4",
  "#E1E3B7","#D7D7A3","#EEE8BC","#E0DEBF",
  "#C7DDCF","#B8D1C3","#C0D8CC","#B2C7BC"
];

const VINTAGE_LIGHT_3 = [
  "#FFCCB3","#FFBFA1","#FFB08F","#FFD1BF",
  "#FFDCCC","#F7C9B8","#F5BDB0","#FFD7CF",
  "#DDE0B0","#D2D4A0","#ECE6B6","#DCDABF",
  "#C2DACD","#B4D0C2","#BBD6CB","#ADC6BB"
];

const PALETTES_LIGHT = [VINTAGE_LIGHT_1, VINTAGE_LIGHT_2, VINTAGE_LIGHT_3];

/* =========================
   ✅ 휠(원판) 그리기
========================= */
function buildWheel(count, colors) {
  const angle = 360 / count;

  const stops = Array.from({ length: count }, (_, i) => {
    const start = i * angle;
    const end = (i + 1) * angle;
    return `${colors[i % colors.length]} ${start}deg ${end}deg`;
  }).join(", ");

  return `conic-gradient(${stops})`;
}

/* ✅ 톤업 팔레트 랜덤 + 셔플 */
function pickPaletteLight() {
  const base = PALETTES_LIGHT[Math.floor(Math.random() * PALETTES_LIGHT.length)];
  const arr = [...base];

  for (let i = arr.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [arr[i], arr[j]] = [arr[j], arr[i]];
  }
  return arr;
}

/* =========================
   ✅ 스핀 중 "조각 수" 줄이기 (색 너무 많아 보이는 문제 해결)
========================= */
function getSpinSliceCount(itemsLength) {
  // 여기 숫자만 취향대로 조절하면 됨!
  const MAX_SLICES = 12; // 스핀 중 최대 12조각까지만 보이게
  const MIN_SLICES = 6;  // 최소 6조각
  return Math.max(MIN_SLICES, Math.min(MAX_SLICES, itemsLength));
}

function buildWheelForSpin(itemsLength) {
  const displayCount = getSpinSliceCount(itemsLength);
  return buildWheel(displayCount, pickPaletteLight());
}

/* =========================
   ✅ 페이드 전환용 Overlay 레이어
========================= */
function ensureOverlay(roulette) {
  if (!roulette) return null;

  let overlay = roulette.querySelector(".wheel-overlay");
  if (overlay) return overlay;

  overlay = document.createElement("div");
  overlay.className = "wheel-overlay";

  Object.assign(overlay.style, {
    position: "absolute",
    inset: "0",
    borderRadius: "50%",
    pointerEvents: "none",
    opacity: "0",
    transition: "opacity 700ms ease",
    willChange: "opacity",
    zIndex: "3"
  });

  roulette.appendChild(overlay);
  return overlay;
}

function fadeOverlayTo(overlay, backgroundStr) {
  if (!overlay) return;

  overlay.style.background = backgroundStr;

  // 0에서 시작 → 다음 프레임에 1로
  overlay.style.opacity = "0";
  void overlay.offsetWidth;
  requestAnimationFrame(() => {
    overlay.style.opacity = "1";
  });
}

/* =========================
   ✅ 스핀 시 또렷해지는 효과 (어둡게 X)
========================= */
function startEnhanceTone(roulette) {
  roulette.style.filter = "saturate(1.05) brightness(1.02) contrast(1.0)";
  void roulette.offsetWidth;
  roulette.style.filter = "saturate(1.22) brightness(1.02) contrast(1.10)";
}

function resetTone(roulette) {
  if (!roulette) return;
  roulette.style.transition = "filter .25s ease";
  roulette.style.filter = "none";
}

/* =========================
   ✅ 초기(대기) 원판 적용
   ✅ "6개만 색상 보이게" = 무조건 6조각 고정
========================= */
function applyStartWheel() {
  const roulette = document.getElementById("roulette");
  if (!roulette) return;

  // ✅ 시작 원판은 항상 6조각
  roulette.style.background = buildWheel(6, START_PALETTE);

  // overlay는 항상 준비(숨김 상태)
  const overlay = ensureOverlay(roulette);
  if (overlay) overlay.style.opacity = "0";
}

/* =========================
   ✅ spin()
========================= */
function spin() {
  if (isSpinning) return;
  isSpinning = true;

  const roulette = document.getElementById("roulette");
  const centerBox = document.getElementById("centerResultBox");
  const centerResult = document.getElementById("centerResult");
  const spinBtn = document.getElementById("spinBtn");

  if (!roulette) {
    isSpinning = false;
    return;
  }

  const overlay = ensureOverlay(roulette);

  // 시작 상태
  centerBox.classList.remove("show");
  centerResult.innerText = "";
  spinBtn.disabled = true;

  fetch("/roulette/spin")
    .then(res => res.json())
    .then(data => {
      const items = data.items;
      const result = data.result;

      if (!items || items.length === 0) throw new Error("룰렛 항목이 없습니다");
      if (!items.includes(result)) throw new Error("결과 없음");

      // ✅ 1) 스핀 시작: "시작 6조각" → "스핀용(조각 수 제한) 톤업" 페이드
      fadeOverlayTo(overlay, buildWheelForSpin(items.length));

      // 각도 계산(정확도는 items.length 그대로 유지)
      const anglePerItem = 360 / items.length;
      const index = items.indexOf(result);

      const spins = Math.floor(Math.random() * 4) + 5; // 5~8
      const targetAngle =
        360 * spins + (360 - (index * anglePerItem + anglePerItem / 2));

      // 시작 위치 고정
      roulette.style.transition = "none";
      roulette.style.transform = `rotate(${currentRotation}deg)`;

      requestAnimationFrame(() => {
        roulette.style.transition =
          "transform 3s cubic-bezier(0.17, 0.67, 0.23, 0.99), filter 3s ease-in-out";

        // ✅ 2) 또렷해지는 톤업 효과
        startEnhanceTone(roulette);

        // 회전 시작
        roulette.style.transform = `rotate(${currentRotation + targetAngle}deg)`;
      });

      // ✅ 3) 스핀 중간에 팔레트 1~2번 더 페이드 교체(조각 수 제한 유지)
      setTimeout(() => {
        fadeOverlayTo(overlay, buildWheelForSpin(items.length));
      }, 900);

      setTimeout(() => {
        fadeOverlayTo(overlay, buildWheelForSpin(items.length));
      }, 1600);

      currentRotation = (currentRotation + targetAngle) % 360;

      setTimeout(() => {
        centerResult.innerText = result;
        rouletteResult = result;
        centerBox.classList.add("show");

        centerBox.style.cursor = "pointer";
        centerBox.title = "클릭해서 주변 맛집 보기";

        spinBtn.disabled = false;
        isSpinning = false;

        // ✅ 스핀 끝나면 filter만 정리 (overlay는 유지 → 예쁜 톤업 상태 유지)
        resetTone(roulette);
      }, 3000);
    })
    .catch(err => {
      console.error(err);

      centerResult.innerText = "❌ 실패";
      centerBox.classList.add("show");

      spinBtn.disabled = false;
      spinBtn.innerText = "다시 돌리기";
      isSpinning = false;

      resetTone(roulette);
    });
}

/* =========================
   ✅ 결과 클릭 → 위치 요청 → 리스트 이동
========================= */
document.addEventListener("DOMContentLoaded", () => {
  const centerBox = document.getElementById("centerResultBox");

  // ✅ 페이지 로드시: 시작 원판은 항상 6조각
  applyStartWheel();

  centerBox.addEventListener("click", () => {
    if (!rouletteResult) return;
    if (isNavigating) return;

    isNavigating = true;

    if (!navigator.geolocation) {
      moveWithDefaultLocation();
      return;
    }

    navigator.geolocation.getCurrentPosition(
      pos => moveToList(rouletteResult, pos.coords.latitude, pos.coords.longitude),
      () => {
        console.warn("위치 권한 거부 → 기본 위치 사용");
        moveWithDefaultLocation();
      }
    );
  });
});

function moveWithDefaultLocation() {
  moveToList(rouletteResult, DEFAULT_LAT, DEFAULT_LON);
}

function moveToList(keyword, lat, lon) {
  fetch("/search/record", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: `keyword=${encodeURIComponent(keyword)}`
  }).catch(() => {});

  location.href =
    `/searchList?keyword=${encodeURIComponent(keyword)}&lat=${lat}&lon=${lon}`;
}
