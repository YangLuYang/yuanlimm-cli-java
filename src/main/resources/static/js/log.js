var $wishLogList = $("#div_wish_log_list");

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
            add("[无数据]", "N/A", "N/A");
        }
    });
}

function add(title, amount, date) {
    $wishLogList.prepend(
        $("<div>").addClass("item").append(
            $("<div>").addClass("row").append(
                $("<div>").addClass("col-md-6").append(
                    $("<h3>").text(title)
                ).append(
                    $("<h4>").addClass("organization").text(amount)
                )
            ).append(
                $("<div>").addClass("col-md-6").append(
                    $("<span>").addClass("period").text(date)
                )
            )
        ));
}