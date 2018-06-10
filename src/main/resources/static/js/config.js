var systemStatus;

(function () {
    updateSystemStatus();
    getStockList();
    getSystemConfig();

    $("#btn_update").on("click", function () {
        doUpdate();
    });
    $("#btn_save").on("click", function () {
        saveConfig();
    });
    $("#btn_load").on("click", function () {
        loadConfig();
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
        updateSystemStatus();
        getSystemConfig();
        if (systemStatus === "stopped") {
            var notify = new PNotify({
                title: '信息',
                text: '配置更新成功, 是否启动服务?',
                icon: 'glyphicon glyphicon-question-sign',
                hide: false,
                confirm: {
                    confirm: true
                },
                buttons: {
                    closer: true,
                    sticker: false
                },
                history: {
                    history: false
                }
            });
            notify.get()
                .on('pnotify.confirm', function () {
                    runSystem();
                });
        } else {
            showNotify("配置更新成功", "info");
        }
    })
}

function loadConfig() {
    var config = localStorage.systemConfig;
    if (config == null) {
        showNotify("未找到已保存的配置.", "error");
    } else {
        config = JSON.parse(config);
        $("#ipt_stock_code").val(config.stockCode);
        $("#ipt_wallet_address").val(config.walletAddress);
        $("#ipt_cheer_word").val(config.cheerWord);
        $("#ipt_compute_thread").val(config.computeThread);
        showNotify("加载配置成功.", "info");
    }
}

function saveConfig() {
    var config = {
        stockCode: $("#ipt_stock_code").val(),
        walletAddress: $("#ipt_wallet_address").val(),
        cheerWord: $("#ipt_cheer_word").val(),
        computeThread: $("#ipt_compute_thread").val()
    };
    localStorage.systemConfig = JSON.stringify(config);
    showNotify("保存配置成功.", "info");
}

function updateSystemStatus() {
    ajaxGet("/api/v1/system/status", function (result) {
        systemStatus = result.data;
    }, false);
}

function runSystem() {
    ajaxPut("/api/v1/system/service/start", null, function () {
        showNotify("服务已启动.", "info");
    });
}