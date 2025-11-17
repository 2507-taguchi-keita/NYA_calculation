$(document).ready(function() {

    // CSRF Token を取得
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");

    // Ajax 全体に CSRF を付与
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(csrfHeader, csrfToken);
    });

    let editingTempId = null;

    // ========================
    // モーダル開閉（委譲）
    // ========================
    $(document).on("click", "#openModalBtn", function() {
        $("#detailModal").show();
        $("#detailForm")[0].reset();
        $("#errorArea").empty();
        editingTempId = null;
    });

    $(document).on("click", "#detailCancelBtn, .close", function() {
        $("#detailModal").hide();
        $("#detailForm")[0].reset();
        $("#errorArea").empty();
        editingTempId = null;
    });

    // ========================
    // 小計自動計算（委譲）
    // ========================
    $(document).on("change keyup", "#amount, #roundTrip", function() {
        const amount = parseInt($("#amount").val()) || 0;
        const roundTrip = $("#roundTrip").val();
        $("#subtotal").val(roundTrip === "往復" ? amount * 2 : amount);
    });

    // ========================
    // 明細追加・編集 Ajax（委譲）
    // ========================
    $(document).on("click", "#detailSaveBtn", function(e) {
        e.preventDefault();

        const formData = new FormData($("#detailForm")[0]);
        formData.append("type", $("#typeHidden").val());
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
                    // モーダル部分だけ置き換え
                    $("#detailModelContainer").html(html);
                    $("#detailModal").show();
                } else {
                    // 明細テーブル部分だけ置き換え
                    $("#detail-list-container").html(html);
                    $("#detailModal").hide();
                    $("#detailForm")[0].reset();
                    editingTempId = null;
                }
            }
        });
    });

    // ========================
    // 編集ボタン（委譲）
    // ========================
    $(document).on("click", ".editDetailBtn", function() {
        editingTempId = $(this).data("temp-id");

        const row = $(this).closest("tr");

        $("#billingDate").val(row.find("td").eq(0).text());
        $("#reason").val(row.find("td").eq(1).text());
        $("#transportation").val(row.find("td").eq(2).text());
        $("#roundTrip").val(row.find("td").eq(3).text());
        $("#amount").val(row.find("td").eq(4).text());
        $("#subtotal").val(row.find("td").eq(5).text());
        $("#remark").val(row.find("td").eq(6).text());

        const newFromCsv = row.data("newfromcsv"); // tr に data-newfromcsv="true/false" をセット
        $("#newFromCsv").val(newFromCsv);

        $("#detailModal").show();
    });

    // ========================
    // 削除ボタン（委譲）
    // ========================
    $(document).on("click", ".deleteDetailBtn", function() {
        const tempId = $(this).data("temp-id");

        $.post("/detail/delete", { tempId: tempId }, function(response) {
            $("#detail-list-container").html(response);
        });
    });

    // ========================
    // CSV関連
    // ========================
    $(document).on("click", "#csvImportBtn", function() {
        $("#csvInput").click();
    });

    $(document).on("change", "#csvInput", function() {
        const file = this.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "/csv/upload",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function(html) {
                $("#slipMainContainer").html(html);
            },
            error: function() {
                alert("CSV取込みに失敗しました");
            }
        });
    });

    $(document).on("click", "#registerBtn", function() {
        $.ajax({
            url: "/slip/temp/bulk-add",
            type: "POST",
            headers: { [csrfHeader]: csrfToken },
            success: function(html) {
                $("#slipMainContainer").html(html);
                window.location.href = '/slip/new';
            },
            error: function() {
                alert("一括追加に失敗しました");
            }
        });
    });

});
