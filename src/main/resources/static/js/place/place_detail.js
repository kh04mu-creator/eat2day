// "오른쪽 상세 카드 UI"

// 검색 submit에서 lat/lon 채우기 + 카드 닫기 + 리스트 자동 로딩

(function () {
  window.closeCard = function () {
    const card = document.getElementById("place-card");
    if (card) card.style.display = "none";
  };

  function setActiveCardByPlaceId(placeId) {
    if (!placeId) return;

    // restaurantList.js가 보통 place-card에 data-id 혹은 onclick을 심어둠
    // 최대한 범용적으로 잡기: data-place-id / data-id 둘 다 시도
    const cards = document.querySelectorAll(".place-card");
    cards.forEach((el) => el.classList.remove("active"));

    const target =
      document.querySelector(`.place-card[data-place-id="${placeId}"]`) ||
      document.querySelector(`.place-card[data-id="${placeId}"]`) ||
      null;

    if (target) target.classList.add("active");
  }

  document.addEventListener("click", (e) => {
    const card = e.target.closest(".place-card");
    if (!card) return;

    // 클릭 시 active 표시
    document.querySelectorAll(".place-card.active").forEach((el) => el.classList.remove("active"));
    card.classList.add("active");
  });

  document.addEventListener("DOMContentLoaded", () => {
    const DEFAULT_LAT = 35.1479;
    const DEFAULT_LON = 129.0596;

    // 검색 폼 lat/lon 자동 채우기
    const form = document.getElementById("placeDetailSearchForm");
    if (form) {
      form.addEventListener("submit", function (e) {
        const placeCard = document.getElementById("place-card");
        if (placeCard) placeCard.style.display = "none";

        const latEl = document.getElementById("pd-lat");
        const lonEl = document.getElementById("pd-lon");
        if (!latEl || !lonEl) return;

        if (latEl.value && lonEl.value) return;

        e.preventDefault();

        const sLat = sessionStorage.getItem("myLat");
        const sLon = sessionStorage.getItem("myLon");
        if (sLat && sLon) {
          latEl.value = sLat;
          lonEl.value = sLon;
          form.submit();
          return;
        }

        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(
            (pos) => {
              latEl.value = pos.coords.latitude;
              lonEl.value = pos.coords.longitude;
              sessionStorage.setItem("myLat", latEl.value);
              sessionStorage.setItem("myLon", lonEl.value);
              form.submit();
            },
            () => {
              latEl.value = DEFAULT_LAT;
              lonEl.value = DEFAULT_LON;
              form.submit();
            }
          );
        } else {
          latEl.value = DEFAULT_LAT;
          lonEl.value = DEFAULT_LON;
          form.submit();
        }
      });
    }

    // keyword가 있으면 리스트 자동 로딩 (restaurantList.js의 search 사용)
    const keyword = document.getElementById("keyword")?.value?.trim();
    if (keyword && typeof window.search === "function") {
      window.search(0);
    }

    // 리스트가 렌더된 다음 active 표시가 필요하므로, 약간 지연해서 한 번 시도
    // (restaurantList.js가 비동기로 그리는 경우 대비)
    setTimeout(() => {
      if (window.CURRENT_PLACE_ID) setActiveCardByPlaceId(window.CURRENT_PLACE_ID);
    }, 200);
  });

  document.addEventListener("place:selected", (e) => {
    const { placeId, lat, lon } = e.detail || {};
    if (!placeId || !isFinite(lat) || !isFinite(lon)) return;

    // active 표시 보강(혹시 렌더 타이밍 꼬여도)
    document.querySelectorAll(".place-card.active").forEach(el => el.classList.remove("active"));
    const target = document.querySelector(`.place-card[data-place-id="${placeId}"]`);
    if (target) target.classList.add("active");

    // 지도 이동 (restaurantList.js가 map 변수를 전역에 노출해야 함)
    if (window.__KAKAO_MAP__) {
      const moveLatLng = new kakao.maps.LatLng(lat, lon);
      window.__KAKAO_MAP__.panTo(moveLatLng);
    }
  });

  
  // restaurantList.js가 리스트를 다시 그릴 때도 active 유지하고 싶으면,
  // restaurantList.js에서 렌더 후 아래 이벤트를 디스패치하도록 한 줄 추가하면 완벽해:
  // document.dispatchEvent(new CustomEvent("places:rendered"));
  document.addEventListener("places:rendered", () => {
    if (window.CURRENT_PLACE_ID) setActiveCardByPlaceId(window.CURRENT_PLACE_ID);
  });
})();
