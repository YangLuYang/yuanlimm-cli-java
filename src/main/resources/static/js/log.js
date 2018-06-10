(function () {
    getWishLogList();
    setInterval(function () {
        getWishLogList();
    }, 10 * 1000);
})();

function getWishLogList() {
    var $wishLogList = $("#div_wish_log_list");
    $wishLogList.empty();
    ajaxGet("/api/v1/wishLog/list", function (result) {
        $.each(result.data, function (i, wishLog) {
            console.log(wishLog);
            if (wishLog.type === "stock") {
                $wishLogList.prepend(
                    $("<div>").addClass("item").append(
                        $("<div>").addClass("row").append(
                            $("<div>").addClass("col-md-6").append(
                                $("<h3>").text("[股票] " + wishLog.stock.name)
                            ).append(
                                $("<h4>").addClass("organization").text(wishLog.amount)
                            )
                        ).append(
                            $("<div>").addClass("col-md-6").append(
                                $("<span>").addClass("period").text(new Date(wishLog.date).Format("yyyy-MM-dd hh:mm:ss"))
                            )
                        )
                    ));
            } else if (wishLog.type === "coin") {
                $wishLogList.prepend(
                    $("<div>").addClass("item").append(
                        $("<div>").addClass("row").append(
                            $("<div>").addClass("col-md-6").append(
                                $("<h3>").text("[援力]")
                            ).append(
                                $("<h4>").addClass("organization").text(wishLog.amount / 100)
                            )
                        ).append(
                            $("<div>").addClass("col-md-6").append(
                                $("<span>").addClass("period").text(new Date(wishLog.date).Format("yyyy-MM-dd hh:mm:ss"))
                            )
                        )
                    ));
            }
        });
    });
}