// restaurantList.js
// 지도 + 검색 리스트 + 마커 + 페이징

// 기본 좌표 (권한 거부 시 사용) 범내골역
const DEFAULT_LAT = 35.148629689617;
const DEFAULT_LON = 129.05901534147;

let map;
let markers = [];
let selectedOverlay = null; // ✅ 선택된 가게 라벨(오버레이)
let myLabelOverlay = null; 

// 내 위치 마커
let myMarker = null;

let lat = DEFAULT_LAT;
let lon = DEFAULT_LON;

let currentPage = 0;
const pageSize = 10;

// 키워드
let keyword = "";

function smoothMove(url){
  const overlay = document.getElementById("page-transition");
  if (!overlay) {
    location.href = url;
    return;
  }

  overlay.classList.add("on");

  // transition이 적용될 시간 확보
  setTimeout(() => {
    location.href = url;
  }, 1500);
}


/** URL에서 선택된 placeId 추출: /place/2037610346 */
function getSelectedPlaceIdFromUrl() {
  const m = location.pathname.match(/\/place\/(\d+)/);
  return m ? m[1] : null;
}

document.addEventListener("DOMContentLoaded", () => {
  const keywordEl = document.getElementById("keyword");
  if (!keywordEl) {
    console.error("keyword element not found");
    return;
  }

  initKeyword();
  initLocation();
  initMap();

  // 리스트 로딩
  search(0);

  // 위치 권한 있으면 더 정확히 갱신(선택)
  requestUserLocation();
});

// 키워드 초기화
function initKeyword() {
  const el = document.getElementById("keyword");
  if (!el || !el.value) {
    console.warn("keyword 없음");
    return;
  }
  keyword = el.value;
}

// 위치 초기화
function initLocation() {
  const latEl = document.getElementById("lat");
  const lonEl = document.getElementById("lon");

  if (latEl?.value && lonEl?.value) {
    lat = Number(latEl.value);
    lon = Number(lonEl.value);
  }

  // sessionStorage 위치 우선
  const savedLat = sessionStorage.getItem("myLat");
  const savedLon = sessionStorage.getItem("myLon");

  if (savedLat && savedLon) {
    lat = Number(savedLat);
    lon = Number(savedLon);
  }

  // NaN 방지
  if (!Number.isFinite(lat) || !Number.isFinite(lon)) {
    lat = DEFAULT_LAT;
    lon = DEFAULT_LON;
  }
}

// 지도 초기화
function initMap() {
  map = new kakao.maps.Map(document.getElementById("map"), {
    center: new kakao.maps.LatLng(lat, lon),
    level: 5
  });

  // (옵션) 디버깅용
  window.__KAKAO_MAP__ = map;

  drawMyLocationMarker();
}

// 위치 요청(선택)
function requestUserLocation() {
  if (!navigator.geolocation) return;

  navigator.geolocation.getCurrentPosition(
    (pos) => {
      lat = pos.coords.latitude;
      lon = pos.coords.longitude;

      sessionStorage.setItem("myLat", lat);
      sessionStorage.setItem("myLon", lon);

      if (map) {
        map.setCenter(new kakao.maps.LatLng(lat, lon));
      }
      drawMyLocationMarker();

      // 내 위치 갱신된 기준으로 다시 검색
      search(0);
    },
    () => {
      console.info("위치 권한 거부 → 기본 위치 유지");
    }
  );
}

// 내 위치 마커 생성
function drawMyLocationMarker() {
  if (!map) return;

  if (myMarker) myMarker.setMap(null);
  if (myLabelOverlay) myLabelOverlay.setMap(null);

  const base = window.APP_PATH ?? "";
  const pos = new kakao.maps.LatLng(lat, lon);

  const markerImage = new kakao.maps.MarkerImage(
    `${base}/images/marker-orange.svg`,
    new kakao.maps.Size(38, 45),
    { offset: new kakao.maps.Point(19, 62) }
  );

  // ⭐ 내 위치 마커
  myMarker = new kakao.maps.Marker({
    map,
    position: pos,
    image: markerImage,
    zIndex: 1000
  });

  // ⭐ 내 위치 라벨
  myLabelOverlay = new kakao.maps.CustomOverlay({
    position: pos,
    yAnchor: 2.8,
    zIndex: 9999,
    content: `
      <div class="selected-label1">
        내 위치
      </div>
    `
  });

  myLabelOverlay.setMap(map);

  // ⭐ 클릭 시 중앙 이동
  kakao.maps.event.addListener(myMarker, "click", () => {
    map.panTo(pos);
  });
}

// 검색
function search(page) {
  if (!keyword || lat == null || lon == null) {
    console.warn("검색 조건 미충족");
    return;
  }

  currentPage = page;

  const base = window.APP_PATH ?? "";

  fetch(
    `${base}/searchKeyword?keyword=${encodeURIComponent(keyword)}`
    + `&lat=${lat}&lon=${lon}`
    + `&page=${page}&size=${pageSize}`
  )
    .then((res) => res.json())
    .then((data) => {
      const list = data.list || [];
      renderList(list);                 // ✅ 왼쪽 리스트
      renderMarkersAndSelectedLabel(list); // ✅ 마커 + 선택 라벨
      renderPagination(data.totalCount || 0);
    })
    .catch((err) => console.error("검색 실패", err));
}

// ✅ 리스트 렌더링 (원래대로: 클릭하면 상세 이동)
function renderList(list) {
  const tbody = document.getElementById("resultBody");
  if (!tbody) return;

  tbody.innerHTML = "";

  const base = window.APP_PATH ?? "";
  const fallback = `${base}/img/no-image.png`;

  list.forEach(item => {
    const tr = document.createElement("tr");
    tr.classList.add("place-card");
    tr.dataset.placeId = item.id; // ✅ active 복구용

    tr.style.cursor = "pointer";
    tr.addEventListener("click", () => {
      const k = keyword ?? "";
      const la = lat ?? "";
      const lo = lon ?? "";

	  const url =
	    `${base}/place/${item.id}`
	    + `?keyword=${encodeURIComponent(k)}&lat=${la}&lon=${lo}&card=1`;

	  smoothMove(url);
    });

    let raw = (item.thumb && String(item.thumb).trim()) ? String(item.thumb).trim() : "";
    if (!raw && item.thumbnailUrl) raw = String(item.thumbnailUrl).trim();

    const thumbUrl = raw ? (raw.startsWith("http") ? raw : `${base}${raw}`) : fallback;

    tr.innerHTML = `
      <td class="thumb-wrap">
        <img src="${thumbUrl}" alt=""
          onerror="
            if (this.dataset.fallbackApplied) { this.style.display='none'; return; }
            this.dataset.fallbackApplied='1';
            this.onerror=null;
            this.src='${fallback}';
          ">
      </td>

      <td class="place-info">
        <div class="place-name">${item.place_name || ""}</div>
        <div class="place-address">${item.road_address_name || ""}</div>
        <div class="place-phone">${item.phone || ""}</div>
      </td>
    `;

    tbody.appendChild(tr);
  });

  // ✅ 현재 상세(/place/{id})인 경우 해당 항목 빨간 강조
  const selectedId = getSelectedPlaceIdFromUrl();
  if (selectedId) setActiveCard(selectedId);
}

// ✅ 마커 전체 표시 + 선택된 가게 라벨(빨간 이름표) 1개 표시
function renderMarkersAndSelectedLabel(list) {
  // 기존 마커 제거
  markers.forEach((m) => m.setMap(null));
  markers = [];

  // 기존 선택 라벨 제거
  if (selectedOverlay) {
    selectedOverlay.setMap(null);
    selectedOverlay = null;
  }

  const selectedId = getSelectedPlaceIdFromUrl();
  const base = window.APP_PATH ?? "";

  list.forEach((item) => {
    if (!item.x || !item.y) return;

    const pos = new kakao.maps.LatLng(item.y, item.x);

    const marker = new kakao.maps.Marker({
      map,
      position: pos
    });

    // 마커 클릭도 원래대로 상세 이동
    kakao.maps.event.addListener(marker, "click", () => {
      const k = keyword ?? "";
      const la = lat ?? "";
      const lo = lon ?? "";

	  const url =
	    `${base}/place/${item.id}`
	    + `?keyword=${encodeURIComponent(k)}&lat=${la}&lon=${lo}&card=1`;

	  smoothMove(url);
    });

    markers.push(marker);

    // ✅ 선택된 가게면 지도에 빨간 라벨 표시
    if (selectedId && String(item.id) === String(selectedId)) {		
      selectedOverlay = new kakao.maps.CustomOverlay({
        position: pos,
        yAnchor: 2,
		zIndex: 9999,
        content: `<div class="selected-label">${item.place_name || ""}</div>`
      });
	  // ✅ 선택된 가게를 지도 중앙으로 부드럽게 이동
	    map.panTo(pos);

	    selectedOverlay = new kakao.maps.CustomOverlay({
	      position: pos,
	      yAnchor: 2,
	      zIndex: 9999,
	      content: `<div class="selected-label">${item.place_name || ""}</div>`
	    });
	  
      selectedOverlay.setMap(map);
    }
  });
}

function setActiveCard(placeId) {
  document.querySelectorAll(".place-card.active").forEach(el => el.classList.remove("active"));
  const target = document.querySelector(`.place-card[data-place-id="${placeId}"]`);
  if (target) target.classList.add("active");
}

// 페이징 렌더링
function renderPagination(totalCount) {
  const totalPage = Math.ceil(totalCount / pageSize);
  const container = document.getElementById("pagination");
  if (!container) return;

  container.innerHTML = "";
  if (totalPage <= 1) return;

  const maxVisible = 5;

  function addBtn(pageIndex, text = null) {
    const btn = document.createElement("button");
    btn.className = "page-btn";
    btn.innerText = text ?? (pageIndex + 1);

    if (pageIndex === currentPage) {
      btn.classList.add("active");
    }

    btn.onclick = () => search(pageIndex);
    container.appendChild(btn);
  }

  function addDots() {
    const span = document.createElement("span");
    span.className = "page-dots";
    span.innerText = "...";
    container.appendChild(span);
  }

  let start = Math.max(0, currentPage - Math.floor(maxVisible / 2));
  let end = start + maxVisible - 1;

  if (end >= totalPage) {
    end = totalPage - 1;
    start = Math.max(0, end - (maxVisible - 1));
  }

  if (start > 0) {
    addBtn(0);
    if (start > 1) addDots();
  }

  for (let i = start; i <= end; i++) {
    addBtn(i);
  }

  if (end < totalPage - 1) {
    if (end < totalPage - 2) addDots();
    addBtn(totalPage - 1);
  }
}
