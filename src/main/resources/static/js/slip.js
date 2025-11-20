$(document).ready(function() {

    $(document).ajaxSend(function(e, xhr, options) {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        xhr.setRequestHeader(header, token);
    });

    let editingTempId = null;
    let editingSource = null; // "normal" or "csv"

    // ========================
    // モーダル開閉（委譲）
    // ========================
    $(document).on("click", "#openModalBtn", function() {
        // タイトル変更
        $("#detailModal h2").text("明細追加");

        // モーダル表示
        $("#detailModal").show();

        // フォーム全体リセット
        const form = $("#detailForm")[0];
        form.reset();

        // hidden 値や初期化したいフィールドを明示的にクリア
        $("#uploadFile").val("");           // ファイル入力クリア
        $("#removeFileFlag").val("false");  // 削除フラグ初期化
        $("#currentFileLink").hide();       // 既存ファイルリンク非表示
        $("#currentFileNone").show();       // 「ファイルなし」を表示

        // エラー表示クリア
        $("#errorArea").empty().hide();

        // JavaScript 管理用の編集状態クリア
        editingTempId = null;
        editingSource = null;
    });


    $(document).on("click", "#detailCancelBtn, .close", function() {
        $("#detailModal").hide();
        $("#detailForm")[0].reset();
        $("#errorArea").empty();
        $("#errorArea").hide();
        editingTempId = null;
        editingSource = null;
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
            formData.append("source", editingSource); // normal / csv
        }

        let url = "/detail/submit";

        $.ajax({
            url: url,
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function(html) {
                const hasErrors = $(html).find('#hasErrors').val() === 'true';

                if (hasErrors) {
                    $("#detailModelContainer").html(html);
                    $("#detailModal").show();
                    $("#errorArea").show();
                } else {
                    $("#detail-list-container").html(html);
                    $("#detailModal").hide();
                    $("#detailForm")[0].reset();
                    $("#errorArea").hide();
                    editingTempId = null;
                    editingSource = null;
                }
            }
        });
    });

    // ========================
    // 編集ボタン（委譲）
    // ========================
    $(document).on("click", ".editDetailBtn", function() {
        const row = $(this).closest("tr");
        editingTempId = row.data("temp-id");
        $("#detailModal h2").text("明細編集");

        $("#billingDate").val(row.find("td").eq(0).text());
        $("#reason").val(row.find("td").eq(1).text());
        $("#transportation").val(row.find("td").eq(2).text());
        $("#roundTrip").val(row.find("td").eq(3).text());
        $("#amount").val(row.find("td").eq(4).text());
        $("#subtotal").val(row.find("td").eq(5).text());
        $("#remark").val(row.find("td").eq(6).text());

        let fileName = row.data("filename");
        let fileUrl = row.data("fileurl"); // このデータ属性を追加推奨

        if (fileName) {
            $("#currentFileName").text(fileName).show();
            $("#currentFileLink").attr("href", fileUrl).show();
        } else {
            $("#currentFileName").text("なし").show();
            $("#currentFileLink").hide();
        }

        $("#storedFileName").val(row.data("storedfilename"));
        $("#originalFileName").val(row.data("originalfilename"));
        $("#fileUrl").val(row.data("fileurl"));

        // ★ファイル削除フラグを初期化
        $("#removeFileFlag").val("false");

        $("#detailModal").show();
    });

    // ========================
    // 削除ボタン（委譲）
    // ========================
    $(document).on("click", ".deleteDetailBtn", function() {

        if (!confirm("この明細を削除しますか？")) {
            return; // キャンセルした場合は中断
        }

        const row = $(this).closest("tr");
        const tempId = row.data("temp-id");

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
            success: function(nextUrl) {
                window.location.href = nextUrl;
            },
            error: function() {
                alert("一括追加に失敗しました");
            }
        });
    });

    $(document).on("click", "button[formaction='/slip/delete']", function (e) {

        if (!confirm("この伝票を削除しますか？")) {
            e.preventDefault(); // ← フォーム送信を止める
        }

    });

});
