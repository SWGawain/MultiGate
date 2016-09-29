/**
 * Created by 嘉玮 on 2016-5-6.
 */
function confirmTrade() {

    $("#agentPay input").click(function () {
        var $val = $(this).val();
        if($val==null || $val==undefined || $val==""){
            var ph = $(this).attr("placeholder");
            $(this).val(ph);
        }
    })

    var filename = "data_"+mode+".json";
    /* 获取配置 */
    $.getJSON(
        filename,
        function (data) {
            $.getJSON(
                "getTemp" ,
                function (tempdata) {
                    // var jtemp = {};
                    // if(tempdata!=undefined&&tempdata!=null&&tempdata!=""){
                    //     jtemp = $.parseJSON(tempdata);
                    // }
                    // var jinit = $.parseJSON(data);

                    /*获取到所有的配置*/

                    createModal_body(tempdata,data);
                    // sendData($.parseJSON(data),jData);

                    $("#send").unbind();
                    $("#send").click(function () {
                        sendData(data,tempdata);
                    })
                }
            );
        }
    );

}
/*发送数据*/
function sendData(jinit,jtemp) {
    /*显示加载图标*/
    loadingImg("inline-block");

    if(url==""||url==undefined){
        _alert("Param error ! ","url should be settled first.","danger");
        return ;
    }
    console.log(url);

    var targetUrl = mode=="Direct" ? "singlePay":"batchPay";

    $.ajax({
        url:targetUrl,
        type:"POST",
        data:{
            "sysKey":selNotNull(jtemp.sysKey,jinit.sysKey),
            "requestNo":requstNo,
            "transCode":selNotNull(jtemp.transCode,jinit.transCode),
            "busiCode":selNotNull(jtemp.busiCode,jinit.busiCode),
            "sysNo":selNotNull(jtemp.sysNo,jinit.sysNo),
            "orgNo":selNotNull(jtemp.orgNo,jinit.orgNo),
            "channelNo":selNotNull(jtemp.channelNo,jinit.channelNo),
            "signType":selNotNull(jtemp.signType,jinit.signType),
            "currency":selNotNull(jtemp.currency,jinit.currency),

            "transNo":"3120"+new Date().getTime(),
            "payAmount":$("#amount").val(),
            "receiverAccountNo":$("#accountNo").val(),
            "receiverAccountType":$("#accountType").val(),
            "receiverAccountName":$("#accountName").val(),
            "receiverBankNo":$("#bankNo").val(),
            "receiverBankCode":$("#bankCode").val(),
            "receiverBankName":$("#bankName").val(),
            "receiverDistrictCode":"",
            "receiverProvince":$("#province").val(),
            "receiverCity":$("#city").val(),
            "summary":$("#summary").val(),
            "purpose":$("#purpose").val(),
            "remark":"",
            "expand1":"",

            "batchNo":"3120"+new Date().getTime(),
            "totalAcount":1,
            "totalAmount":$("#amount").val(),
            "accountNo":$("#batchAccountNo").val(),
            "accountType":$("#batchAccountType").val(),
            "accountName":$("#batchAccountName").val(),
            "bankNo":$("#batchBankNo").val(),
            "bankName":$("#batchBankName").val(),

            "dubboUrl":url+":2181",
            "dubboApi":selNotNull(jtemp.dubbo_api,jinit.dubbo_api),
            "dubboMethod":selNotNull(jtemp.dubbo_method,jinit.dubbo_method),
            "dubboGroup":selNotNull(jtemp.dubbo_group,jinit.dubbo_group),
            "dubboVersion":selNotNull(jtemp.dubbo_version,jinit.dubbo_version)
        },
        success:function (data) {
            loadingImg("none");
            _alert("Success ! ",data,"success");
        },
        error:function (data) {
            loadingImg("none");
            _alert("Failure ! ","check background log or call me ^_^","danger");
        }

    });
}

function createModal_body(jtemp,jinit) {
    /*拼装展示内容*/
    $("#myModalLabel").html(mode + " Details");
    var content = "";

    content += "url="+url +":2181<br><br>";

    content += "orgNo="+selNotNull(jtemp.orgNo,jinit.orgNo)+"<br>";
    requstNo = selNotNull(jtemp.requestNo,jinit.requestNo) + new Date().getTime();
    content += "requestNo="+requstNo+"<br>";
    content += "transCode="+selNotNull(jtemp.transCode,jinit.transCode)+"<br>";
    content += "busiCode="+selNotNull(jtemp.busiCode,jinit.busiCode)+"<br>";
    content += "sysNo="+selNotNull(jtemp.sysNo,jinit.sysNo)+"<br>";
    content += "sysKey="+selNotNull(jtemp.sysKey,jinit.sysKey)+"<br>";
    content += "channelNo="+selNotNull(jtemp.channelNo,jinit.channelNo)+"<br>";
    content += "signType="+selNotNull(jtemp.signType,jinit.signType)+"<br>";
    content += "currency="+selNotNull(jtemp.currency,jinit.currency)+"<br><br>";

    content += "dubbo_api="+selNotNull(jtemp.dubbo_api,jinit.dubbo_api)+"<br>";
    content += "dubbo_method="+selNotNull(jtemp.dubbo_method,jinit.dubbo_method)+"<br>";
    content += "dubbo_version="+selNotNull(jtemp.dubbo_version,jinit.dubbo_version)+"<br>";
    content += "dubbo_group="+selNotNull(jtemp.dubbo_group,jinit.dubbo_group)+"<br><br>";

    var inputs = $("#agentPay input");
    $.each(inputs,function (i, ipt) {

        /*将placeholder赋值到空值中*/
        var $val = $(ipt).val();
        if($val==null || $val==undefined || $val==""){
            var ph = $(ipt).attr("placeholder");
            $(ipt).val(ph);
        }

        content += $(ipt).attr("id")+"="+$(ipt).val()+"<br>";
    });
    $(".modal-body").html(content);
}
