/**
 * Created by 嘉玮 on 2016-6-23.
 */
/* 后台url配置 */

function setMode(mode,remark) {

    $("#mode").val(mode);
    $("#mode").html(remark+"<span class=\"caret\"></span>");

    if(mode == "batchPay"){
        $("#transCode").attr("placeholder","11012");
        $("#channelNo").attr("placeholder","110103");

        $("#dubboMethod").attr("placeholder","batchAgentPay");
        $("#dubboMethodlist").html(
            "<option label=\"路由\" value=\"batchAgentPay\"></option>" +
            "<option label=\"网关\" value=\"batchPayExecute\"></option>"
        )
    }
    if(mode == "singlePay"){
        $("#transCode").attr("placeholder","11011");
        $("#channelNo").attr("placeholder","110101");
        $("#dubboMethod").attr("placeholder","singleAgentPay");
        $("#dubboMethodlist").html(
            "<option label=\"路由\" value=\"singleAgentPay\"></option>" +
            "<option label=\"网关\" value=\"singlePayExecute\"></option>"
        )
    }
}

function send() {

    if(check()){
        return ;
    }

    /*显示加载图标*/
    loadingImg("inline-block");

    var content = $(".modal-body").html();
    var lines = content.split("<br>");
    var params = "";
    for(var i=0 ;i<lines.length;i++){
        var line = lines[i];
        if(line=='requestNo=Req'){
            line = line + new Date().getTime();
        }

        params += line ;
        if(i<lines.length-2){
            params += "&";
        }
    }

    //发送至服务器
    $.ajax({
        url:$("#mode").val(),
        type:"POST",
        data:params,
        success:function (data) {
            loadingImg("none");
            _alert("Success",data,"success");
        },
        error:function (data) {
            loadingImg("none");
            _alert("Failure ! ","check background log or call me ^_^","danger");
        }
    })
}