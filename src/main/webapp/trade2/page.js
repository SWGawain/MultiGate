/**
 * Created by 嘉玮 on 2016-5-5.
 */

/*生成交易页面*/
function createTradePage() {

    clearPage();

    $.get(
        "agentPay.html",
        function (data) {
            $("#main").html(data);

            $("#confirmBtn").click(function () {
                confirmTrade();
            });

            $("#createBatchInfosBtn").click(function () {
                $this = $(this);
                var hasActive = $this.hasClass("active");
                if(hasActive){
                    $this.removeClass("active");
                    $("#batchInfo").remove();
                }else{
                    $this.addClass("active");
                    $.get(
                        "batchInfo.html",
                        function (data2) {
                            $this.parent().after(data2);
                        }
                    );
                }
            });
        }
    );
}

/* 生成配置页面 */
function createAboutPage() {

    clearPage();

    $.get(
        "config.html",
        function (html) {
            $("#main").html(html);

            $("#save").click(function () {
                saveConfig();
            });

            $("#config input").click(function () {
                var $val = $(this).val();
                if($val==null || $val==undefined || $val==""){
                    var ph = $(this).attr("placeholder");
                    $(this).val(ph);
                }
            })

            var filename = "data_"+mode+".json";
            $.getJSON(
                filename,
                function (data) {
                    $.getJSON(
                        "getTemp" ,
                        {
                            "mode":mode
                        },
                        function (tempdata) {
                            refillData(data,tempdata);
                        }
                    );
                }
            );
        }
    );
}

/*处理数据*/
function refillData(initData,tempData) {
    $("#orgNo").attr("placeholder",selNotNull(tempData.orgNo,initData.orgNo));
    $("#requestNo").attr("placeholder",selNotNull(tempData.requestNo,initData.requestNo));
    $("#transCode").attr("placeholder",selNotNull(tempData.transCode,initData.transCode));
    $("#busiCode").attr("placeholder",selNotNull(tempData.busiCode,initData.busiCode));
    $("#sysNo").attr("placeholder",selNotNull(tempData.sysNo,initData.sysNo));
    $("#sysKey").attr("placeholder",selNotNull(tempData.sysKey,initData.sysKey));
    $("#channelNo").attr("placeholder",selNotNull(tempData.channelNo,initData.channelNo));
    $("#signType").attr("placeholder",selNotNull(tempData.signType,initData.signType));
    $("#currency").attr("placeholder",selNotNull(tempData.currency,initData.currency));

    $("#dubbo_api").attr("placeholder",selNotNull(tempData.dubbo_api,initData.dubbo_api));
    $("#dubbo_method").attr("placeholder",selNotNull(tempData.dubbo_method,initData.dubbo_method));
    $("#dubbo_version").attr("placeholder",selNotNull(tempData.dubbo_version,initData.dubbo_version));
    $("#dubbo_group").attr("placeholder",selNotNull(tempData.dubbo_group,initData.dubbo_group));
}

/*比较非空数据 第一个为空，则返回第二个*/
function selNotNull(temp,init) {
    var retData;
    if(temp!=null&&temp!=undefined&&temp!=""){
        retData=temp;
    }else{
        retData=init;
    }
    return retData;
}

/*清理页面*/
function clearPage() {
    $("#main").empty();
    $("[role=alert]").remove();
}