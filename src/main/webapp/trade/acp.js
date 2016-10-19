/**
 * Created by 嘉玮 on 2016-10-17.
 */

$(function () {
    initFileInput("file-0a", "batchAcp/upload",true);
    $("#limitAmount").on("blur",function () {
        var $this = $(this);
        var amount = $this.val();
        if(amount == "" || amount ==null || amount ==undefined || amount <= 0){
            $this.val("5,000.00");
        }else{
            $this.val(amount);
        }
    })
})

function setOrgNo(orgNo,remark) {
    $("#orgNo").val(orgNo);
    $("#orgNo").html(remark+"<span class=\"caret\"></span>");
    
}

function setMode(mode,remark) {

    $("#mode").val(mode);
    $("#mode").html(remark+"<span class=\"caret\"></span>");

    if(mode == "dev"){
        $("#transCode").attr("placeholder","11012");
        $("#channelNo").attr("placeholder","110103");

        $("#dubboMethod").attr("placeholder","batchAgentPay");
        $("#dubboMethodlist").html(
            "<option label=\"路由\" value=\"batchAgentPay\"></option>" +
            "<option label=\"网关\" value=\"batchPayExecute\"></option>"
        )
    }
    if(mode == "uat"){
        $("#transCode").attr("placeholder","11011");
        $("#channelNo").attr("placeholder","110101");
        $("#dubboMethod").attr("placeholder","singleAgentPay");
        $("#dubboMethodlist").html(
            "<option label=\"路由\" value=\"singleAgentPay\"></option>" +
            "<option label=\"网关\" value=\"singlePayExecute\"></option>"
        )
    }
}
var batchInfo  ;
//初始化fileinput控件（第一次初始化）
function initFileInput(ctrlName, uploadUrl,dropZone) {
    var control = $('#' + ctrlName);

    control.fileinput({
        uploadUrl: uploadUrl, //上传的地址
        allowedFileExtensions : ['xls','xlsx'],
        uploadExtraData:function (previewId, index) {
            var data = {limitAmount:$("#limitAmount").val()};
            return data ;
        },
        dropZoneEnabled:dropZone
    }).on('fileuploaded',function (event, data) {
        batchInfo = data.response;
        var batch_params = "<table class=\"table table-striped\">";
        batch_params += "<thead><tr><th>#</th><th>户名</th><th>金额</th><th>卡号</th><th>总行号</th>" +
            "<th>银行名称</th><th>证件类型</th><th>证件号</th><th>账户类型</th><th>备注</th>" +
            "</tr></thead><tbody>";

        $.each(batchInfo.agentAcpDtoList,function (i) {
            $("#file").html("<input id=\"file-0a\" type=\"file\" name=\"file\">");
            initFileInput("file-0a", "batchAcp/upload",false);
            batch_params += "<tr>" +
                "<td>"+i+"</td>"+
                "<td>"+batchInfo.agentAcpDtoList[i].payerAccountName+"</td>" +
                "<td>"+batchInfo.agentAcpDtoList[i].payAmount+"</td>" +
                "<td>"+batchInfo.agentAcpDtoList[i].payerAccountNo+"</td>" +
                "<td>"+batchInfo.agentAcpDtoList[i].payerBankNo+"</td>" +
                "<td>"+batchInfo.agentAcpDtoList[i].payerBankName+"</td>" +
                "<td>"+$.parseJSON(batchInfo.agentAcpDtoList[i].expand1).idtype+"</td>" +
                "<td>"+$.parseJSON(batchInfo.agentAcpDtoList[i].expand1).id+"</td>" +
                "<td>"+batchInfo.agentAcpDtoList[i].payerAccountType+"</td>" +
                "<td>"+batchInfo.agentAcpDtoList[i].purpose+"</td>" +
                "</tr>";
        })
        batch_params += "</tbody></table>";
        $("#params").html(batch_params);

        $("#subData").show();
    });
}

function subData() {

    var env = $("#mode").val();
    var orgNo = $("#orgNo").val();

    var dev_url = "121.40.236.90:38094";
    var uat_url = "120.26.104.20:38094";

    var url;
    if(env == "dev"){
        url = dev_url;
    }else{
        url = uat_url;
    }
    var confirm = window.confirm("是否提交数据到通联？\n"+url+"\n"+orgNo);
    if(!confirm) return ;

    $("#subData").hide();

    //发送至服务器
    $.ajax({
        url:"batchAcp/send",
        type:"POST",
        data:{
            json:JSON.stringify(batchInfo),
            dubboUrl:url,
            orgNo:$("#orgNo").val()
        },
        success:function (data) {
            _alert("Success",data,"success");
        },
        error:function (data) {
            _alert("Failure ! ","check background log or call me ^_^","danger");
        }
    })
    
}

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