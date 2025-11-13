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
    let editingTempId = null;

    // モーダル開閉
    $("#openModalBtn").click(() => {
        modal.show();
        form[0].reset();
        editingTempId = null;
    });

    $(".close, #cancelBtn").click(() => {
        modal.hide();
        form[0].reset();
        editingTempId = null;
    });

    // 小計自動計算
    $("#amount, #roundTrip").on("change keyup", () => {
        let amount = parseInt($("#amount").val()) || 0;
        let roundTrip = $("#roundTrip").val();
        $("#subtotal").val(roundTrip === "往復" ? amount * 2 : amount);
    });

    // 明細追加・編集 Ajax
    $("#detailSubmit").click(function (e) {
        e.preventDefault();

        let formData = new FormData($("#detailForm")[0]);

        if (editingTempId) {
            formData.append("tempId", editingTempId);
        }

        $.ajax({
            url: "/detail/submit",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function(html) {
                const hasErrors = $(html).find('#hasErrors').val() === 'true';
                if (hasErrors) {
                    $("#modalContent").html(html);
                } else {
                    $("#detailArea").html(html);
                    modal.hide();
                    form[0].reset();
                    editingTempId = null;
                }
            }
        });
    });

    // 編集ボタン
    $(document).on("click", ".editDetailBtn", function() {
        editingTempId = $(this).data("temp-id");

        // 対応する行データを抽出
        let row = $(this).closest("tr");
        $("#billingDate").val(row.find("td").eq(0).text());
        $("#reason").val(row.find("td").eq(1).text());
        $("#transportation").val(row.find("td").eq(2).text());
        $("#roundTrip").val(row.find("td").eq(3).text());
        $("#amount").val(row.find("td").eq(4).text());
        $("#subtotal").val(row.find("td").eq(5).text());
        $("#remark").val(row.find("td").eq(6).text());

        modal.show();
    });

    // 削除ボタン
    $(document).on("click", ".deleteDetailBtn", function() {
        $.post("/detail/delete", { tempId: $(this).data("temp-id") }, function(response) {
            $("#detailArea").html(response);
        });
    });

    $("#csvImportBtn").click(() => $("#csvInput").click());

    $("#csvInput").change(function() {
        let file = this.files[0];
        if(!file) return;
        let formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "/csv/upload",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function(html) {
                $("#detailArea").html(html);
                alert("CSV取込み完了！");
            },
            error: function() {
                alert("CSV取込みに失敗しました");
            }
        });
    });

    // CSV一括追加
    $("#registerBtn").click(function() {
        $.ajax({
            url: "/slip/temp/bulk-add",
            type: "POST",
            headers: { [csrfHeader]: csrfToken },
            success: function(html) {
                $("#detailArea").html(html);
                alert("明細を伝票に追加しました！");
            },
            error: function() { alert("一括追加に失敗しました"); }
        });
    });

});
