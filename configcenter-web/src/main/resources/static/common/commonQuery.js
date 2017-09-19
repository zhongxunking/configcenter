/**
 * Created by zhongxun on 2017.9.15.
 */

$(document).ready(function () {
    query();
});

function requestServer(formName) {
    var form = $("#" + formName);
    $.ajax({
        url: form.attr("action"),
        data: form.serialize(),
        success: function (ajaxResult) {
            if (ajaxResult.success) {
                query();
            } else {
                alert("失败，原因：" + ajaxResult.description);
            }
        },
        error: function () {
            alert("请求服务失败");
        }
    });
}

var pageNo = 1;
var pageSize = 10;
var totalPage = 0;

function query() {
    var form = $("#queryForm");
    $.ajax({
        url: form.attr("action"),
        data: form.serialize() + "&pageNo=" + pageNo + "&pageSize=" + pageSize,
        success: function (ajaxResult) {
            if (!ajaxResult.success) {
                alert("查询失败，原因：" + ajaxResult.message);
                return;
            }
            createTable(ajaxResult.infos);
            refreshPageFoot(ajaxResult.totalCount);
        },
        error: function () {
            alert("请求服务失败");
        }
    });
}

function createTable(infos) {
    var table = "<table width='80%'>";
    table += createTableHead();
    for (var i = 0; i < infos.length; i++) {
        table += creteTableRow(infos[i]);
    }
    table += "</table>";

    $("#tableDiv").html(table);
}

function previousPage() {
    if (pageNo > 1) {
        pageNo--;
        query();
    }
}

function nextPage() {
    if (pageNo < totalPage) {
        pageNo++;
        query();
    }
}

function refreshPageFoot(totalCount) {
    totalPage = parseInt((totalCount + pageSize - 1) / pageSize);
    $("#currentPageNo").html(pageNo);
    $("#totalPage").html(totalPage);
}
