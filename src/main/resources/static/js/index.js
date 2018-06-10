var updateMonitorInfoTask;
var systemStatus;
(function () {
    updateSystemStatus();

    $("#btn_run").on("click", function () {
        if (systemStatus === "needConfig") {
            showNotify("需要先配置系统", "warn");
            setTimeout(function () {
                window.location.href = "/config.html";
            }, 1500);
            return;
        }
        runSystem();
    });

    $("#btn_stop").on("click", function () {
        stopSystem();
    });
})();

function updateMonitorInfo() {
    ajaxGet("/api/v1/monitor", function (result) {
        updateMonitorInfoView(result.data.hashSpeed, result.data.hard, result.data.totalHash);
    })
}

function updateMonitorInfoView(hashSpeed, hard, totalHash) {
    $("#div_hash_speed").text(hashSpeed);
    $("#div_hard").text(hard);
    $("#div_total_hash").text(totalHash);
}

function updateSystemStatus() {
    ajaxGet("/api/v1/system/status", function (result) {
        $("#p_system_status").text(result.data);
        systemStatus = result.data;
        if (result.data === "running") {
            $("#btn_stop").show(250);
            $("#btn_run").hide(250);

            if (updateMonitorInfoTask == null) {
                updateMonitorInfoTask = setInterval(function () {
                    updateMonitorInfo();
                }, 1000);
            }
        } else {
            $("#btn_stop").hide(250);
            $("#btn_run").show(250);

            if (updateMonitorInfoTask != null) {
                clearInterval(updateMonitorInfoTask);
                updateMonitorInfoTask = null;

                updateMonitorInfoView("N/A", "N/A", "N/A");
            }
        }
    })
}

function runSystem() {
    ajaxPut("/api/v1/system/service/start", null, function () {
        updateSystemStatus();
        showNotify("服务已启动.", "info");
    });
}

function stopSystem() {
    ajaxPut("/api/v1/system/service/stop", null, function () {
        updateSystemStatus();
        showNotify("服务已停止.", "warn");
    })
}