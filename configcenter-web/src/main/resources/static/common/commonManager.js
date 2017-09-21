var managerInfo = null;
$(document).ready(function () {
    managerInfo = getLoginedManager();
});

function getLoginedManager() {
    var managerInfo;
    $.ajax({
        url: "/manage/managerLogin/getLoginedManager",
        data: {},
        async: false,
        success: function (ajaxResult) {
            if (ajaxResult.success) {
                if (ajaxResult.managerInfo == null) {
                    alert("未登录，或登录超时，请重新登陆");
                    window.location.href = "login.html";
                } else {
                    managerInfo = ajaxResult.managerInfo;
                }
            } else {
                alert("失败，原因：" + ajaxResult.message);
            }
        },
        error: function () {
            alert("请求服务失败");
        }
    });
    return managerInfo;
}
