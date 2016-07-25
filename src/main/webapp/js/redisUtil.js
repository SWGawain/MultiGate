/**
 * Created by 嘉玮 on 2016-3-31.
 */

var delValue = "<div id='plus'><hr><p id='plus'>success</p></div>";

var currentKey,subValue,opType;     //当前key ，当前修改值， 当前修改项
var currentUrl ;                    //当前连接URL
var opType_value = "modiValue";
var opType_ttl = "expire";

$(function(){

    /*初始化测试数据，可删除*/
    init();

    /*session中的链接检查*/
    checkConnection();

    /*绑定查询事件*/
    $("#searchBtn").click(function(){
        if(currentUrl=="" || currentUrl==undefined){
            showDanger("Url Error.&nbsp;&nbsp;&nbsp;","No Connection obtained yet!")
            return ;
        }

        $("#result").empty();

        var searchKey = $("#searchContent").val();
        $.post(
            "redis/search",
            {
                key:searchKey
            },
            function(data){
                if(data=="failure"){
                    showDanger("Http error.  ","please check background logs");
                }else{
                    $jsonData = $.parseJSON(data);
                    appendResult($jsonData);
                }
            }
        );

    });

    /*切换数据库按钮*/
    $("#changeDB").click(function () {
        if(currentUrl=="" || currentUrl==undefined){
            showDanger("Url Error.&nbsp;&nbsp;&nbsp;","No Connection obtained yet!");
            return ;
        }

        var database = $("#database").val();
        database = database==undefined || database=="" ? 0 : database;
        $.post(
            "redis/select",
            {
                database : database
            },
            function (data) {
                if(data=="failure"){
                    showDanger("Http error.  ","please check background logs");
                }else{
                    $("#result").empty();
                }
            }
        );
    })

    /*帮助隐藏警告*/
    $("input").focus(function(){
       hideDanger();
    });

    /*导航栏链接信息 -- 绑定重新连接事件*/
    $("#reconnect").click(function(){
        $.post(
            "redis/reconnect"
        );
        console.log("reconnect");
    });
    /*导航栏链接信息 -- 绑定断开连接事件*/
    $("#quit").click(function(){
        console.log("quit");
        $.post(
            "redis/quit"
        );
        $("#result").empty();
        //显示输入框
        addNavInputs();
        //解绑url
        currentUrl = "";

    });
    /*导航栏输入框 -- 绑定连接事件*/
    $("#subInit").click(function(){
        var url = $("#url").val();
        var pass = $("#pass").val();
        var port = $("#port").val();

        pass = pass==""||pass==undefined ? "rkylin123" : pass ;
        $("pass").val(pass);
        port = port==""||port==undefined ? "6379" : port ;
        $("port").val(port);

        if(url==""||url==undefined){
            showDanger("url error.","url can't be null");
            return ;
        }
        if(pass==""||pass==undefined){
            showDanger("pass error.","pass can't be null");
            return ;
        }
        if(port==""||port==undefined){
            showDanger("port error.","port can't be null");
            return ;
        }
        $.post(
            "redis/init",
            {
                url:url,
                pass:pass,
                port:port
            },
            function(data){
                if(data=="failure"){
                    showDanger("Http error.  ","please check background logs");
                }else{
                    //显示连接信息
                    addNavInfos(url);

                }
            }
        );
    });

    /* 清理当前页所有数据 */
    $(".CLEAR").click(function () {
        $("#plus").remove();
        $("#plusModify").remove();
        var _div_keys = $(".row.content div:first-child");
        var keys =new Array();
        $.each(_div_keys,function (i,_div_key) {
            keys.push( $(_div_key).html()+"" );
        })

        var conf = window.confirm("是否清除本页所有key");
        if(conf){
            $.post(
                "redis/clear",
                {
                    "keys[]":keys
                }
                ,
                function(data){
                    if(data=="failure"){
                        showDanger("Http error.  ","please check background logs");
                    }else{
                        $("#result").empty();
                    }
                }
            );
        }
    });
});



/*初始化测试数据*/
function init(){
    var init = new Array();
    init[0] = "Test1";
    init[1] = "Test2";
    appendResult(init);
}

/*检查session中的链接*/
function checkConnection(){
    $.post(
        "redis/qconnect",
        function(data){
            if(data=="null"){
                addNavInputs();
            }else{
                addNavInfos(data);
            }
        }
    );
}

/*扩展明细*/
function appendResult(key){
    $.each(key,function (k, v) {
        $("#result").append("<div class='row content'>" +
            "<div style='display: none'>"+k+"</div>"+
            "<div class='col-md-8'>"+v+"</div>"+
            "<div class='col-md-4'>"+
            "<button type='button' class='btn btn-xs btn-info GET'>GET</button>"+
            "<button type='button' class='btn btn-xs btn-warning TTL' style='margin-left: 20px;'>TTL</button>"+
            "<button type=\"button\" class=\"btn btn-xs btn-danger DEL\" style=\"margin-left: 20px;\">DEL</button>" +
            "<\/div><\/div>"
        );
    })
    $(".GET").click(function(){
        $("#plus").remove();
        $("#plusModify").remove();
        var $key = $(this).parent().prev();
        var key_content = $key.html();
        /*获取value*/
        var $div = $(this).parent();
        $.post(
            "redis/getValue",
            {
                key : key_content
            },
            function (data) {
                if(data=="failure"){
                    showDanger("Http error.  ","please check background logs");
                }else{
                    var value_content = "<div id='plus'><hr><p>"+data+"</p></div>";
                    $key.html(key_content+value_content);
                    appendModify($div);

                    currentKey = key_content;
                    opType = opType_value ;
                }
            }
        );

    });
    $(".TTL").click(function(){
        $("#plus").remove();
        $("#plusModify").remove();
        var $key = $(this).parent().prev();
        var key_content = $key.html();

        var $div = $(this).parent();

        $.post(
            "redis/ttl",
            {
                key : key_content
            },
            function (data) {
                if(data=="failure"){
                    showDanger("Http error.  ","please check background logs");
                }else{
                    var ttl_value = "<div id='plus'><hr><p>"+data+"</p></div>";
                    $key.html(key_content+ttl_value);
                    appendModify($div);
                    currentKey = key_content;
                    opType = opType_ttl ;
                }
            }
        );

    });
    $(".DEL").click(function(){
        $("#plus").remove();
        $("#plusModify").remove();
        var $key = $(this).parent().prev();
        var key_content = $key.html();
        $this = $(this);

        var conf = window.confirm("是否删除"+key_content);
        if(conf){
            var $row = $this.parent().parent();
            console.log($row);
            $.post(
                "redis/del",
                {
                    key : key_content
                },
                function(data){
                    if(data=="failure"){
                        showDanger("Http error.  ","please check background logs");
                    }else{
                        $row.remove();
                    }
                }
            );
        }
    });
}
//添加修改按钮
function appendModify(div){
    div.append("<div id='plusModify'><hr>" +
        "<div class='row'>" +
            "<div class='col-md-3'><button type='button' class='btn btn-xs btn-success MODIFY'>MODIFY</button></div>" +
            "<div class=\"input-group input-group-sm \"  style='display: none' id='modiContent'>"+
                "<input type=\"text\" class=\"form-control\" placeholder=\""+$("#plus p").html()+"\" aria-describedby=\"sizing-addon3\">" +
                "<span class=\"input-group-addon btn btnPointer\" id=\"sizing-addon3\">OK</span>"+
            "</div>" +
        "</div></div>");

    $(".MODIFY").click(function(){
       var dis = $("#modiContent").css("display");
        if(dis == "none"){
            $("#modiContent").css("display","table",1000);
        }else{
            $("#modiContent").css("display","none",1000);
        }
    });

    $("#sizing-addon3").click(function(){
        subValue =  $(this).prev().val();
        if(subValue==""||subValue==undefined){
            return ;
        }
        console.log(currentKey+" "+opType+"  "+subValue);
        $.post(
            "redis/"+opType,
            {
                key : currentKey ,
                value : subValue
            },
            function(data){
                if(data=="failure"){
                    showDanger("Http error.  ","please check background logs");
                }else{
                    $("#plus").remove();
                    $("#plusModify").remove();
                }
            }
        );
    });
}

/*添加导航栏 -- 链接信息*/
function addNavInfos(url){
    $("#inputs").hide();
    $("#urlDisplay").html(url);
    $("#infos").show();
    //绑定链接url
    currentUrl = url ;
}
/*添加导航栏 -- 输入框*/
function addNavInputs(){
    $("#infos").hide();
    $("#inputs").show();
}


/*警告信息*/
function showDanger(head,body){
    $("#danger").slideDown();
    $("#danger").html(
        "<strong>"+head+"</strong>"+body
    );
}
/*隐藏警告信息*/
function hideDanger(){
    $("#danger").slideUp();
}