const modal = document.getElementById('detailModal');
const openBtn = document.getElementById('openModalBtn');
const closeBtn = document.querySelector('.close');
const cancelBtn = document.getElementById('cancelBtn');

openBtn.onclick = () => modal.style.display = 'block';
closeBtn.onclick = () => modal.style.display = 'none';
cancelBtn.onclick = () => modal.style.display = 'none';
window.onclick = e => { if (e.target === modal) modal.style.display = 'none'; };

$(function() {
    function calculateSubtotal() {
        let amount = parseFloat($('#amount').val()) || 0;
        let roundTrip = $('#roundTrip').val();
        let subtotal = roundTrip === '往復' ? amount * 2 : amount;
        $('#subtotal').val(subtotal);
    }

    $('#amount, #roundTrip').on('input change', calculateSubtotal);
});

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

// ページ読み込み時に合計を計算
window.addEventListener("DOMContentLoaded", function() {
    calculateTotalAmount();
});

function calculateTotalAmount() {
    let total = 0;

    // 明細一覧（tbody内）を走査して金額（5列目）を集計
    document.querySelectorAll(".slip-main table tbody tr:nth-child(1)").forEach(row => {
        const amountCell = row.querySelector("td:nth-child(5)");
        if (amountCell) {
            const value = Number(amountCell.textContent.replace(/,/g, "")) || 0;
            total += value;
        }
    });

    // 合計をフォームの totalAmount(hidden or 表示用 input) にセット
    const totalField = document.querySelector("input[name='totalAmount']");
    if (totalField) {
        totalField.value = total.toLocaleString();
    }
}
