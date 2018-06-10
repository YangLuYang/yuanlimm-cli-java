var statisticChart;

(function () {
    initChart();
    getStatisticInfo();
    setInterval(function () {
        getStatisticInfo();
    }, 2500);
})();

function getStatisticInfo() {
    ajaxGet("/api/v1/statistic", function (result) {
        updateChartData(result.data.wishStockInfoList);
        updateBasicDate(result.data.totalCoin / 100, result.data.totalStock);
    });
}

function initChart() {
    var dataCount = 0;
    ajaxGet("/api/v1/statistic", function (result) {
        if (result.wishStockInfoList) {
            dataCount = result.wishStockInfoList.length();
        }
    }, false);

    var end = 100;
    if (dataCount > 0) {
        end = Math.ceil(100 / (dataCount / 20));
        if (end > 100) {
            end = 100;
        }
    }

    statisticChart = echarts.init(document.getElementById('main'));
    var option = {
        "tooltip": {
            "trigger": "axis",
            "axisPointer": {"type": "shadow"}
        },
        "grid": {
            "borderWidth": 0,
            "top": 50,
            "bottom": 140
        },
        "calculable": true,
        "xAxis": [{
            "type": "category",
            "axisLine": {
                lineStyle: {
                    color: '#90979c'
                }
            },
            "splitLine": {"show": false},
            "axisTick": {"show": false},
            "splitArea": {"show": false},
            "axisLabel": {
                "interval": "0",
                "rotate": -90,
                "formatter": function (value) {
                    if (value.length >= 5) {
                        value = value.substring(0, 5);
                        if (value.match("·$")) {
                            value = value.substring(0, 4)
                        }
                    }
                    return value;
                }
            },
            "showMinLabel": true,
            "showMaxLabel": true,
            "data": null
        }],
        "yAxis": [{
            "type": "value",
            "splitLine": {"show": false},
            "axisLine": {
                lineStyle: {
                    color: '#90979c'
                }
            },
            "axisTick": {"show": false},
            "axisLabel": {"interval": 0}
        }],
        "dataZoom": [{
            show: true,
            height: 30,
            xAxisIndex: [
                0
            ],
            bottom: 30,
            start: 0,
            end: end,
            handleSize: '110%',
            handleStyle: {
                color: "#d3dee5"
            },
            textStyle: {
                color: "#90979c"
            },
            borderColor: "#90979c",
            zoomOnMouseWheel: false,
            moveOnMouseMove: true
        }],
        "series": [{
            type: "bar",
            itemStyle: {
                normal: {
                    color: "rgba(0,191,183,1)",
                    barBorderRadius: 0,
                    label: {
                        show: true,
                        position: "top",
                        formatter: function (p) {
                            return p.value > 0 ? (p.value) : '';
                        }
                    }
                }
            },
            "data": null
        }]
    };
    statisticChart.setOption(option);
}

function updateChartData(data) {
    if (data.length > 0) {
        $("#div_stock_chart").show(250);

        var xData = [], yData = [];

        $.each(data, function (i, wishStockInfo) {
            xData.push(wishStockInfo.stock.name);
            yData.push(wishStockInfo.amount);
        });

        xData = xData.reverse();
        yData = yData.reverse();

        statisticChart.setOption({
            xAxis: {data: xData},
            series: [{data: yData}]
        });
    } else {
        $("#div_stock_chart").hide(250);
    }
}

function updateBasicDate(totalCoin, totalStock) {
    $("#div_total_coin").text(totalCoin);
    $("#div_total_stock").text(totalStock);
}

window.onresize = function () {
    statisticChart.resize();
};