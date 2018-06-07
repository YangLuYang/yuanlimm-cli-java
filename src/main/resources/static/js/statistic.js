var $statisticList = $("#div_statistic_list");

(function () {
    getStatisticInfo();
    setInterval(function () {
        getStatisticInfo();
    }, 5000);
})();

function getStatisticInfo() {
    $statisticList.empty();
    ajaxGet("/api/v1/statistic", function (result) {
        $.each(result.data.wishStockInfoList, function (i, wishStockInfo) {
            addInfo("[股票] " + wishStockInfo.stock.name, wishStockInfo.amount);
        });
        addInfo("[援力]", result.data.coinAmount / 100);
    });
}

function addInfo(title, number) {
    $statisticList.prepend(
        $("<div>").addClass("col-md-6 col-lg-4").append(
            $("<div>").addClass("project-card-no-image").append(
                $("<h3>").text(title)
            ).append(
                $("<h4>").text(number)
            )
        )
    );
}