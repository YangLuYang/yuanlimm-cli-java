(function () {
    getStockList();
    getSystemConfig();

    $("#btn_update").on("click", function () {
        doUpdate();
    });
})();

function getStockList() {
    var $stockCode = $("#ipt_stock_code");
    $stockCode.empty();
    ajaxGet("/api/v1/stock/list", function (result) {
        $.each(result.data, function (i, stock) {
            $stockCode.append("<option value='" + stock.code + "'>" + stock.name + "</option>");
        });
    }, false);
    $stockCode.prepend("<option value=\"\" selected=\"\">Please Select...</option>");
}

function getSystemConfig() {
    ajaxGet("/api/v1/system/config", function (result) {
        $("#ipt_stock_code").val(result.data.stockCode);
        $("#ipt_wallet_address").val(result.data.walletAddress);
        $("#ipt_cheer_word").val(result.data.cheerWord);
        $("#ipt_compute_thread").val(result.data.computeThread);
    });
}

function doUpdate() {
    ajaxPut("/api/v1/system/config", {
        stockCode: $("#ipt_stock_code").val(),
        walletAddress: $("#ipt_wallet_address").val(),
        cheerWord: $("#ipt_cheer_word").val(),
        computeThread: $("#ipt_compute_thread").val()
    }, function () {
        getSystemConfig();
        showNotify("更新成功", "info");
    })
}