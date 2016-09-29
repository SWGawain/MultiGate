/**
 * Created by 嘉玮 on 2016-5-5.
 */
var url="121.40.236.90";
var mode = "Indirect";
var requstNo = "";

$(function () {
    createTradePage();

    
});

/*设置url*/
function setUrl() {
    url = $("#url").val();
    console.log(url);

}
/*设置直连间连*/
function setMode(value) {
    $("#mode").html(value);
    mode = value ;
}
/*保存配置*/
function saveConfig() {
    var inputs = $("#config input");
    $.each(inputs,function (i,input) {
        var id = $(input).attr("id");
        var placeHolder = $(input).attr("placeholder");
        var val = $(input).val();

       saveTemp(id,val);

    })
}

/*向后台发送临时数据*/
function saveTemp(id,val) {
    if(val !=null && val != undefined && val!=""){
        /*保存值*/
        $.ajax({
            url:"saveTemp",
            data:{
                key:id,
                value:val,
                "mode":mode
            },
            type:"POST",
            success:function (data) {
                _alert("Send "+data,"have saved your tempdata ...","success");
            },
            error:function (data) {
                _alert("Send failed ! " ,"check background log or call me ^_^","danger");
            }
        });
    }
}

/*提示*/
function _alert(head,text, type) {
    $("[role=alert]").remove();
    var expand = "<div class=\"alert alert-"+type+" alert-dismissible fade in\" role=\"alert\" style='display: none'>"+
                    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                        "<span aria-hidden=\"true\">&times;</span>" +
                    "</button>"+
                    "<strong>"+head+"</strong> "+ text +
                "</div>";

    $("#alert").html(expand);
    $("[role=alert]").fadeIn("slow");
}

/*加载图标*/
function loadingImg(display) {
    $("[role=alert]").remove();
    $("#loading").css("display",display);
}