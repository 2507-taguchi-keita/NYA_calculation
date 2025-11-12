$(document).ready(function() {

    // CSRF Token を取得
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");

    // Ajax 全体に CSRF を付与
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(csrfHeader, csrfToken);
    });

    const modal = $("#detailModal");
    const form = $("#detailForm");
    let editIndex = null;

    // モーダル開閉
    $("#openModalBtn").click(() => { modal.show(); });
    $(".close, #cancelBtn").click(() => { modal.hide(); form[0].reset(); editIndex = null; });

    // 小計自動計算
    $("#amount, #roundTrip").on("change keyup", () => {
        let amount = parseInt($("#amount").val()) || 0;
        let roundTrip = $("#roundTrip").val();
        $("#subtotal").val(roundTrip === "往復" ? amount * 2 : amount);
    });

    // 明細追加・編集 Ajax
    $("#detailSubmit").click(function(e) {
        e.preventDefault();
        let formData = new FormData(form[0]);
        if (editIndex !== null) formData.append("index", editIndex);

        $.ajax({
            url: "/detail/submit",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                $("#detailArea").html(response);
                modal.hide();
                form[0].reset();
                editIndex = null;
            },
        });
    });

    // 編集ボタン
    $(document).on("click", ".editDetailBtn", function() {
        editIndex = $(this).data("index");
        let row = $("#detailTableBody tr").eq(editIndex * 2); // 1明細2行
        $("#billingDate").val(row.find("td").eq(0).text());
        $("#reason").val(row.find("td").eq(1).text());
        $("#transportation").val(row.find("td").eq(2).text());
        $("#roundTrip").val(row.find("td").eq(3).text());
        $("#amount").val(row.find("td").eq(4).text());
        $("#subtotal").val(row.find("td").eq(5).text());
        $("#remark").val($("#detailTableBody tr").eq(editIndex*2+1).find("td").eq(0).text());
        modal.show();
    });

    // 削除ボタン Ajax
    $(document).on("click", ".deleteDetailBtn", function() {
        let index = $(this).data("index");
        $.post("/detail/delete", { index: index }, function(response) {
            $("#detailArea").html(response);
        });
    });

    // CSV取込み
    $("#csvImportBtn").click(function() {
        $("#csvInput").click();
    });

    $("#csvInput").change(function() {

        let file = this.files[0];
        if (!file) return;

        let formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "/csv/import",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                $("#detailArea").html(response);   // << 既存コードと同じ
                alert("CSV取込みが完了しました！");
            },
            error: function() {
                alert("CSV取込みに失敗しました");
            }
        });
    });

});
