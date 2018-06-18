var $workerNodeList = $("#div_worker_node_list");

(function () {
    getWishLogList();
    setInterval(function () {
        getWishLogList();
    }, 10 * 1000);
})();

function getWishLogList() {
    $wishLogList.empty();
    ajaxGet("/api/v1/wishLog/list", function (result) {
        if (result.data && result.data.length > 0) {
            $.each(result.data, function (i, wishLog) {
                console.log(wishLog);
                if (wishLog.type === "stock") {
                    add("[股票] " + wishLog.stock.name,
                        wishLog.amount,
                        new Date(wishLog.date).Format("yyyy-MM-dd hh:mm:ss"));
                } else if (wishLog.type === "coin") {
                    add("[援力]",
                        wishLog.amount / 100,
                        new Date(wishLog.date).Format("yyyy-MM-dd hh:mm:ss"));
                }
            });
        } else {
            add("[无已连接节点]", "N/A", "N/A");
        }
    });
}

function add(title, speed, status) {
    $workerNodeList.prepend(
        $("<div>").addClass("col-md-6 col-lg-4").append(
            $("<div>").addClass("project-card-no-image").append(
                $("<h3>").text(title)
            ).append(
                $("<h4>").text(speed + " Hash/s")
            ).append(
                $("<button>").addClass("btn btn-outline-primary btn-sm").text("Config")
            ).append(
                $("<div>").addClass("tags").append(
                    $("<a>").text(status)
                )
            )
        )
    );
}