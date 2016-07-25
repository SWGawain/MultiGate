/**
 * Created by 嘉玮 on 2016-6-24.
 */
/* 保存配置 */
function saveConfig() {

    var inputs = $("#config input");
    
    $.each(inputs,function (i,ipt) {
        $ipt = $(ipt);
        var val = $ipt.val();
        if(val !="" && val !=undefined && val!=null){
            sessionStorage.setItem($ipt.attr("id"),val);
        }
    })
}

/*  确认参数 */
function confirmDetails() {
    
    var inputs_config = $("#config input");
    var inputs_params = $("#params input");
    var content ="";
    $.each(inputs_config,function (i,ipt) {
        $ipt = $(ipt);
        var val = $ipt.val();
        if(val ==""||val ==undefined||val==null){
            val = $ipt.attr("placeholder");
        }
        content += $ipt.attr("id")+"="+val+"<br>";
    });
    content += "<br>";
    $.each(inputs_params,function (i,ipt) {
        $ipt = $(ipt);
        var val = $ipt.val();
        if(val ==""||val ==undefined||val==null){
            val = $ipt.attr("placeholder");
        }
        content += $ipt.attr("id")+"="+val+"<br>";
    });

    $(".modal-body").html(content);
}

/* 发送参数 */
function send() {

    /*显示加载图标*/
    loadingImg("inline-block");

    var content = $(".modal-body").html();
    var lines = content.split("<br>");
    var params = "";
    for(var i=0 ;i<lines.length;i++){
        var line = lines[i];
        params += line ;
        if(i<lines.length-2){
            params += "&";
        }
    }

    //发送至服务器
    $.ajax({
        url:"ll",
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
