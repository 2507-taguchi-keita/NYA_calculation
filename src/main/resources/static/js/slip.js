const modal = document.getElementById('detailModal');
const openBtn = document.getElementById('openModalBtn');
const closeBtn = document.querySelector('.close');
const cancelBtn = document.getElementById('cancelBtn');

openBtn.onclick = () => modal.style.display = 'block';
closeBtn.onclick = () => modal.style.display = 'none';
cancelBtn.onclick = () => modal.style.display = 'none';
window.onclick = e => { if (e.target === modal) modal.style.display = 'none'; };

$("#detailSubmit").click(function(){
  let formData = new FormData($("#detailForm")[0]);

  $.ajax({
    url: "/detail/temp",
    type: "POST",
    data: formData,
    processData: false,
    contentType: false,
    success: function() {
      alert("明細追加しました");
      location.reload();
    }
  });
});