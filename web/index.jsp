<%--
  Created by IntelliJ IDEA.
  User: myoatm
  Date: 2017-12-24
  Time: 오후 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
    function getRequest() {
        var req = null;
        try {
            req = new XMLHttpRequest();
        } catch (trymicrosoft) {
            try {
                req = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (othermicrosoft) {
                try {
                    req = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (failed) {

                }
            }
        }
        if (req == null){
            alert("Error creating request object!");
        }

        return req;
    }

    function needToParse(){
        var selectedVal = $("#select_mediaProvider")[0].selectedIndex;

        var val = null;
        switch(selectedVal){

            case 1: // facebook 공개
                val = $("#sourceForm_1").find("input").val();
                break;

            case 2: // facebook 비공개
                $("#tempRener").text( $("#sourceForm_2").find("textarea").val() );
                //val = $("#tempRener").html();
                val = json_quote($("#sourceForm_2").find("textarea").val());
                break;

            case 3: // instagram 공개
                val = $("#sourceForm_1").find("input").val();
                break;

            case 4: // instagram 비공개
                $("#tempRener").text( $("#sourceForm_2").find("textarea").val() );
                //val = $("#tempRener").html();
                val = json_quote($("#sourceForm_2").find("textarea").val());
                break;
            case 5: // youtube
                break;
        }

        parsingData(val, $("#select_mediaProvider").val() );

    }

    function parsingData(data, media){

        /*
        xhr = getRequest();
        var url = "proc/parsing_proc.jsp";
        var query = "data=" + data;
        xhr.open("POST", url, true); // is Async?
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded,charset=UTF-8");
        var _xhr = xhr;
        xhr.onreadystatechange = function(){
            parsingProcess(_xhr);
        };
        xhr.send(query);
        */
        $.ajax({
            type:"POST",
            url:"proc/parsing_proc.jsp",
            data: { "data" : data, "media" : media },
            dataType:"json",
            success: [function(result){
                createResultElem(result);
            }]
        });




    }

    function createResultElem(data){
        /*
        var _out = $('<form method="get" action="' + data + '">' +
            '<input type="submit" class="btn btn-primary" value="다운로드">' +
        '</form>');
        */
        //data = JSON.parse(data);
        $("#doPostBtn").remove();

        var refreshBtn = $('<p> <input TYPE="button" class="btn btn-primary" onClick="history.go(0)" VALUE="새로운 다운로드 시작"> </p>');
        $("#resultRender").append(refreshBtn);


        if(data == null){
            var _out = $('<h1>파일을 찾을 수 없습니다</h1>');
            $("#resultRender").append(_out);
            return;
        }


        var p = $('<p></p>');
        var _user = data["user"];
        var _title = data["title"];

        var user = $('<label>' + _user + '</label> - ');
        var title = $('<label>' + _title + '</label>');
        p.append(user);
        p.append(title);
        $("#resultRender").append(p);

        var resultData = data["data"];
        var resultDataLength = Object.keys(resultData).length;

        fileId = [];

        for(i=0; i<resultDataLength; i++){
            p2 = $('<p></p>');

            dataThumb = resultData[i]["thumb"];
            dataUrl   = resultData[i]["url"];

            var thumb = $('<img src=' + dataThumb + ' style="width:400px;"><br>');


            var link = $('<a href=' + dataUrl + ' download="' + _user + "_" + _title + "_" + i + '">' +
                '<br>' +
                '<input type="button" class="btn btn_warning" value="다운로드"> ' +
                '</a>');

            p2.append(thumb);
            p2.append(link);
            $("#resultRender").append(p2);
            //afterMainAjaxRequest(dataThumb);
            fileId.push(dataThumb);
        }
        afterMainAjaxRequest(fileId);

    }

    function allHideInputForm(){
        $("#sourceForm_1").hide();
        $("#sourceForm_2").hide();
        //$("#sourceForm_1").show();
    }

    function afterMainAjaxRequest(delFileList){ //all components will be disabled.
        $("#sourceForm_1").prop( "disabled", true );
        $("#sourceForm_2").prop( "disabled", true );
        $("#select_mediaProvider").prop( "disabled", true );
        $("#txt_url").prop( "disabled", true );
        $("#textarea_source").prop( "disabled", true );


        if(delFileList ==null || delFileList == [] ){
            return
        }


        $.ajax({
            type:"POST",
            url:"proc/delete_thumb.jsp",
            data: { "thumb" : JSON.stringify(delFileList)},
            dataType:"json",
            success: [function(result){
            }]
        });

    }

    function change_select_mediaProvider(e){
        //selectedVal = e.options[e.selectedIndex].value;
        var selectedVal = e.selectedIndex;
        allHideInputForm();
        switch(selectedVal){
            case 1:
                $("#sourceForm_1").show();
                break;
            case 2:
                $("#sourceForm_2").show();
                break;
            case 3:
                $("#sourceForm_1").show();
                break;
            case 4:
                $("#sourceForm_2").show();
                break;
            case 5:
                $("#sourceForm_1").show();
                break;
            case 6:
                $("#sourceForm_1").show();
                break;
        }
    }
</script>

<script>
    var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        meta = { // table of character substitutions
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        };

    function json_quote(string) {
        escapable.lastIndex = 0;
        return escapable.test(string) ? '"' + string.replace(escapable, function (a) {
            var c = meta[a];
            return typeof c === 'string' ? c :
                '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
        }) + '"' : '"' + string + '"';
    }
</script>

<html>
<head>
    <title>웹 미디어 다운로더</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>

    <script src="json2.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
</head>
<body>

<div class="container">
    <!-- Content here -->
    <section>
        <div class="form-group">
            <label for="select_mediaProvider">미디어를 선택해주세요</label>
            <select class="form-control" id="select_mediaProvider" onchange="change_select_mediaProvider(this)" >
                <option disabled selected value>옵션을 선택해주세요</option>
                <option value="facebook_public">Facebook 공개</option>
                <option value="facebook_private">Facebook 비공개</option>
                <option value="instagram_public">Instagram 공개</option>
                <option value="instagram_private">Instagram 비공개</option>
                <option value="youtube">Youtube</option>
                <option value="youtube_playlist">Youtube 플레이리스트</option>
                <!--
                <option>3</option>
                <option>4</option>
                <option>5</option>
                -->
            </select>
        </div>
        <div class="form-group" id="sourceForm_1" style="display:none;">
            <label for="textarea_source">페이지 URL을 입력해주세요</label>
            <input type="text" class="form-control" id="txt_url">
        </div>
        <div class="form-group" id="sourceForm_2" style="display:none;">
            <label for="textarea_source">페이지 소스를 입력해주세요</label>
            <textarea class="form-control" id="textarea_source" rows="7"></textarea>
        </div>
        <input class="btn btn-primary" type="button" value="서버요청" id="doPostBtn" onclick="needToParse()">
    </section>
    <section id="resultRender">

    </section>
    <section id="tempRener" style="display:none;">

    </section>
</div>

</body>
</html>
