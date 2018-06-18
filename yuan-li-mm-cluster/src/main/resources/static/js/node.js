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
            addPlaceholder();
        }
    });
}

function add(title, speed, status) {
    $workerNodeList.append(
        $("<div>").addClass("col-md-6 col-lg-4").append(
            $("<div>").addClass("project-card-no-image big").append(
                $("<h3>").text(title)
            ).append(
                $("<h4>").text(speed + " Hash/s")
            ).append(
                $("<button>")
                    .addClass("btn btn-outline-primary btn-sm btn-worker-node-config")
                    .text("Config")
            ).append(
                $("<div>").addClass("tags").append(
                    $("<a>").text(status)
                )
            )
        )
    );
}

function addPlaceholder() {
    $workerNodeList.append(
        $("<div>").addClass("col-md-6 col-lg-4").append(
            $("<div>").addClass("project-card-no-image big").append(
                $("<h3>").text("[无已连接节点]")
            ).append(
                $("<h4>").text("N/A" + " Hash/s")
            ).append(
                $("<button>")
                    .addClass("btn btn-outline-primary btn-sm btn-worker-node-config disabled")
                    .text("Config")
                    .attr("disabled", true)
            ).append(
                $("<div>").addClass("tags").append(
                    $("<a>").text("N/A")
                )
            )
        )
    );
}

