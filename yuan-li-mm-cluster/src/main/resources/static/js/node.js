var $workerNodeList = $("#div_worker_node_list");

(function () {
    getWorkerNodeList();
    setInterval(function () {
        getWorkerNodeList();
    }, 10 * 1000);

    $("#div_worker_node_list").on("click", ".btn-worker-node-config", function () {
        $("#modal_worker_node_config").modal("show");
    });
})();

function getWorkerNodeList() {
    $workerNodeList.empty();
    ajaxGet("/api/v1/workerNode/list", function (result) {
        if (result.data && result.data.length > 0) {
            $.each(result.data, function (i, info) {
                add(info.workerNode.name, info.workerMonitorInfo.hashSpeed, info.systemStatus)
            });
        } else {
            add("[无已连接节点]", "N/A", "N/A");
        }
    });
}

function add(title, speed, status) {
    $workerNodeList.prepend(
        $("<div>").addClass("col-md-6 col-lg-4").append(
            $("<div>").addClass("project-card-no-image big").append(
                $("<h3>").text(title)
            ).append(
                $("<h4>").text(speed + " Hash/s")
            ).append(
                $("<button>")
                    .addClass("btn btn-outline-primary btn-sm btn-worker-node-config disabled")
                    .text("Config")
                    .attr("disabled", true)
            ).append(
                $("<div>").addClass("tags").append(
                    $("<a>").text(status)
                )
            )
        )
    );
}