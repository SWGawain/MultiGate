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
            "onclick=\"confirmDetails()\">Confirm Auth detail</button>"+
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
            content += "<input type=\"text\" class=\"form-control\" id=\""+id+"\" placeholder=\""+_placeholder+"\">";

            _divs[count] +=content;

            var dataList_json = value.dataList;
            if(dataList_json != undefined){
                $.each(dataList_json,function (key,value) {
                    _divs[count] += "<datalist id=\""+id+"\">";
                    _divs[count] += "<option label=\""+key+"\" value=\""+value+"\"/>";
                    _divs[count] += "</datalist>";
                })
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