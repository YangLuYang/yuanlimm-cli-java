(function () {
    updateMonitorInfo();
    setInterval(function () {
        updateMonitorInfo();
    }, 1000);
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