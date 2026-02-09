function toggleClamp(id, btn){
  const el = document.getElementById(id);
  el.classList.toggle('open');
  btn.textContent = el.classList.contains('open') ? '접기' : '더보기';
}