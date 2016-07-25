/**
 * Created by 嘉玮 on 2016-6-23.
 */

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
    //加载配置页面
    $.get(
        "about.html",
        function (data) {
            $("#main").append(data);
            //加载参数页面
            $("#main input").focus(function () {
                var $this = $(this);
                var val = $this.val();
                if(val ==""||val ==undefined||val==null){
                    $this.val($this.attr("placeholder"));
                }
            })
            $.get(
                "params.html",
                function (data) {
                    $("#main").append(data);
                    //为所有的input标签添加 点击赋值 的效果
                    for (var i=0;i<sessionStorage.length;i++){
                        var key = sessionStorage.key(i);
                        var value = sessionStorage.getItem(key);
                        $("#"+key).val(value);
                    }
                }
            );
        }
    );
}


/*加载图标*/
function loadingImg(display) {
    $("[role=alert]").remove();
    $("#loading").css("display",display);
}


/* =================================================================================================  */