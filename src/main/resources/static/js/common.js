/**
 * get请求
 * @param url 请求的url地址
 * @param fn 成功的回调函数
 */
function ajaxGet(url, fn) {
    $.ajax({
        url: url,
        type: 'get',
        beforeSend: function (xhr) {
        },
        success: function (result) {
            if (result.status === "success") {
                fn(result);
            } else {
                showNotify(result.message, "error");
            }
        },
        error: function () {
            console.log("Request Fail:\t" + url);
        }
    });
}

/**
 * post请求通过json数据
 * @param url 请求的url地址
 * @param data data请求数据
 * @param fn 成功的回调函数
 */
function ajaxPost(url, data, fn) {
    $.ajax({
        url: url,
        type: 'post',
        timeout: 30000,
        contentType: 'application/json',
        data: data ? JSON.stringify(data) : null,
        beforeSend: function (xhr) {
        },
        success: function (result) {
            if (result.status === "success") {
                fn(result);
            } else {
                showNotify(result.message, "error");
            }
        },
        error: function () {
            console.log("Request Fail:\t" + url);
        }
    });
}

/**
 * delete请求
 * @param url 请求的url地址
 * @param data 数据
 * @param fn 成功的回调函数

 */
function ajaxDelete(url, data, fn) {
    $.ajax({
        url: url,
        type: 'delete',
        timeout: 30000,
        contentType: 'application/json',
        data: data ? JSON.stringify(data) : null,
        beforeSend: function (xhr) {
        },
        success: function (result) {
            if (result.status === "success") {
                fn(result);
            } else {
                showNotify(result.message, "error");
            }
        },
        error: function () {
            console.log("Request Fail:\t" + url);
        }
    });
}

/**
 * put请求通过json数据
 * @param url 请求的url地址
 * @param data data请求数据
 * @param fn 成功的回调函数
 */
function ajaxPut(url, data, fn) {
    $.ajax({
        url: url,
        type: 'put',
        contentType: 'application/json',
        data: data ? JSON.stringify(data) : null,
        beforeSend: function (xhr) {
        },
        success: function (result) {
            if (result.status === "success") {
                fn(result);
            } else {
                showNotify(result.message, "error");
            }
        },
        error: function () {
            console.log("Request Fail:\t" + url);
        }
    });
}

/**
 * patch请求通过json数据
 * @param url 请求的url地址
 * @param data data请求数据
 * @param fn 成功的回调函数
 */
function ajaxPatch(url, data, fn) {
    $.ajax({
        url: url,
        type: 'patch',
        contentType: 'application/json',
        data: data ? JSON.stringify(data) : null,
        beforeSend: function (xhr) {
        },
        success: function (result) {
            if (result.status === "success") {
                fn(result);
            } else {
                showNotify(result.message, "error");
            }
        },
        error: function () {
            console.log("Request Fail:\t" + url);
        }
    });
}


Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

/**
 * 显示通知
 */
function showNotify(msg, type, delay) {
    type = type ? type : "info";
    delay = delay ? delay : 2500;
    new PNotify({title: '提醒', text: msg, type: type, delay: delay, buttons: {closer: true, sticker: false}});
}

/**
 * 获取url中的参数
 * @param name
 * @returns {*}
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}