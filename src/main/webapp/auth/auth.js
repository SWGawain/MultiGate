/**
 * Created by 嘉玮 on 2016-6-23.
 */
/* 后台url配置 */
var post_url = "vali"

$(function () {
    //加载页面
    loadPage();

    //绑定页面切换按钮
    $("#auth").click(function () {
        $("#config").hide();
        $("#params").show();
        _alertRemove();
    })

    $("#about").click(function () {
        $("#config").show();
        $("#params").hide();
        _alertRemove();
    })
    //绑定reset键
    $("#reset").click(function () {
        sessionStorage.clear();
        //重新加载页面
        loadPage();
    })
})


/*提示*/
function _alert(head,text, type) {
    _alertRemove();
    var expand = "<div class=\"alert alert-"+type+" alert-dismissible fade in\" role=\"alert\" style='display: none'>"+
        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
        "<span aria-hidden=\"true\">&times;</span>" +
        "</button>"+
        "<strong>"+head+"</strong> "+ text +
        "</div>";

    $("#alert").html(expand);
    $("[role=alert]").fadeIn("slow");
}
function _alertRemove() {
    $("[role=alert]").remove();
}

//加载页面
function loadPage() {
    $("#main").empty();

    createPage();
}

/*加载图标*/
function loadingImg(display) {
    $("[role=alert]").remove();
    $("#loading").css("display",display);
}