var manager = null;
$(document).ready(function () {
    manager = getCurrentManager();
    if (manager == null) {
        alert("未登录，或登录超时，请重新登陆");
        window.top.location.href = "login.html";
    }
});

function getCurrentManager() {
    var manager = null;
    $.ajax({
        url: "../manager/main/current",
        data: {},
        async: false,
        success: function (ajaxResult) {
            if (ajaxResult.success) {
                manager = ajaxResult.manager;
            } else {
                alert("失败，原因：" + ajaxResult.message);
            }
        },
        error: function () {
            alert("请求服务失败");
        }
    });
    return manager;
}
