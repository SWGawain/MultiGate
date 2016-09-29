/**
 * Created by 嘉玮 on 2016-9-28.
 */
/**
 * Created by 嘉玮 on 2016-7-22.
 */
/* 加载页面 */
function createPage() {

    createAboutPage() ;
}

//加载配置页面
function createAboutPage() {
    var content_about = "<div id=\"config\" style=\"display: none\">";

    $.getJSON(
        "data_about.json",
        function (data) {

            content_about = generateContent(content_about,data);

            content_about += "<button type=\"button\" class=\"btn btn-primary\" style=\"margin-bottom: 20px\" " +
                "onclick=\"saveConfig()\">save</button>"+
                "</div>";

            $("#main").append(content_about);

            //加载参数页面
            $("#main input").focus(function () {
                var $this = $(this);
                var val = $this.val();
                if(val ==""||val ==undefined||val==null){
                    $this.val($this.attr("placeholder"));
                }
            });

            /* 配置页面加载完成后，加载参数页面 */
            createParamPage();

        }
    );

}

//加载参数页面
function createParamPage() {
    var content_param = "<div id=\"params\">";

    $.getJSON(
        "data_param.json",
        function (data) {

            content_param = generateContent(content_param,data);

            content_param += "<button type=\"button\" class=\"btn btn-primary btn-lg\" data-toggle=\"modal\" data-target=\"#myModal\" " +
                "onclick=\"confirmDetails()\">Confirm detail</button>"+
                "<img src=\"../res/css/loading.jpg\" style=\"width: 30px;margin-left: 20px; display: none\" id=\"loading\">";

            $("#main").append(content_param);

            //为所有的input标签添加 点击赋值 的效果
            for (var i=0;i<sessionStorage.length;i++){
                var key = sessionStorage.key(i);
                var value = sessionStorage.getItem(key);
                $("#"+key).val(value);
            }
        }
    );

}

/* 生成加载页html内容 */
function generateContent(content,data) {

    $.each(data,function (n, jsonData) {
        content += "<div class=\"row plus\">";

        var count = 0 ;
        var _div_0 = "<div class=\"col-md-4\">";
        var _div_1 = "<div class=\"col-md-4\">";
        var _div_2 = "<div class=\"col-md-4\">";

        var _divs = [_div_0,_div_1,_div_2];
        $.each(jsonData,function (id,value) {

            var content = "<div class=\"form-group\">"+
                "<label for=\""+id+"\">"+value.name+"</label>";

            var _placeholder = value.placeholder==undefined?"":value.placeholder;
            content += "<input type=\"text\" class=\"form-control\" id=\""+id+"\" list='"+id+"list' placeholder=\""+_placeholder+"\">";

            _divs[count] +=content;

            var dataList_json = value.dataList;
            if(dataList_json != undefined){
                _divs[count] += "<datalist id=\""+id+"list\">";
                $.each(dataList_json,function (key,value) {
                    _divs[count] += "<option label=\""+key+"\" value=\""+value+"\"/>";
                })
                _divs[count] += "</datalist>";
            }

            _divs[count] += "</div>" ;
            count = ++count % 3 ;
        })
        _divs[0] += "</div>";
        _divs[1] += "</div>";
        _divs[2] += "</div>";

        content += _divs[0];
        content += _divs[1];
        content += _divs[2];

        content += "</div><hr>";

    })
    return content ;
}


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

function confirmDetails() {

    var inputs_config = $("#config input");
    var inputs_params = $("#params input");
    var marked_params = $(".MarkedParam");
    var content ="";
    $.each(marked_params,function (i,ipt) {
        $ipt = $(ipt);
        var val = $ipt.val();
        content += $ipt.attr("id")+"="+val+"<br>";
    });
    content += "<br>";
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
        val = val.trim();

        content += $ipt.attr("id")+"="+val+"<br>";
    });

    $(".modal-body").html(content);
}

//检查*是否填入
function check() {
    var $phs = $("[placeholder='*']");
    var ph = $phs.get(0);
    if($phs.get(0) != undefined){
        var $val = $(ph).val();
        if($val==undefined || $val=='*' || $val==""){
            var $id = $(ph).attr("id");
            _alert("Warning!",$id+" cannot be null","warning");
            return true ;
        }
    }
    return false ;
}