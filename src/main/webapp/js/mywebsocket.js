var websocket = null;

//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
    websocket = new ReconnectingWebSocket("ws://localhost:8080/websocket");
}
else{
    alert('Not support websocket');
}

//连接发生错误的回调方法
websocket.onerror = function(){
    console.log("error");
};

//连接成功建立的回调方法
websocket.onopen = function(){
    // alert("connection succeeded");
    websocket.send(JSON.stringify({type: "userLogin"}));
};

//接收到消息的回调方法
websocket.onmessage = function(){
    var jsonObject = JSON.parse(event.data);
    var type = jsonObject['type'];
    if (type >= 100 && type < 110)
        textMessage(type, jsonObject);
    else if (type >= 110 && type < 120)
        applyMessage(type, jsonObject);
    else if (type == 120)
        allowApply(jsonObject);
    else if (type >= 130 && type < 140)
        deleteFriend(type, jsonObject);
    else if (type >= 140 && type < 160)
        inOutMessage(type, jsonObject);
};

//连接关闭的回调方法
websocket.onclose = function(){
    console.log("websocket closed");
};

//关闭连接
function closeWebSocket(){
    websocket.close();
}

//发送消息
function send(message){
    websocket.send(message);
}

function textMessage(type, jsonObject) {
    if (type >= 100 && type <=101) {
        $.ajax({
            type: "GET",
            url: "/user/recent",
            success: function (result) {
                $("#list-friends").html(result);
            }
        });
    }
    if (type == 103) {
        // console.log("Status: " + type);
        $(".recent").attr("src", "img/recent_unread.png");
    }
    if (type == 100 || type == 102)
        setMessageInnerHTML(jsonObject);
}

function applyMessage(type, jsonObject) {
    if (type >= 110 && type <=111) {
        $.ajax({
            type: "GET",
            url: "/user/recent",
            success: function (result) {
                $("#list-friends").html(result);
            }
        });
    }
    if (type == 113) {
        // console.log("Status: " + type);
        $(".recent").attr("src", "img/recent_unread.png");
    }
    if (type == 110 || type == 112) {
        // alert("110 or 113");
        $.ajax({
            type: "POST",
            url: "/chat/chatbox",
            data: {toEmail: "systemInfo@sys.com"},
            success: function(result) {
                $("#chat").html(result);
            }
        });
    }
}

function allowApply(jsonObject) {
    $.ajax({
        type: "GET",
        url: "/user/friendList",
        success: function (result) {
            $(".list-friends").html(result);
        }
    });
}

function deleteFriend(type, jsonObject) {
    var email = jsonObject['toEmail'];
    var id1 = email;
    var id2 = "recent:" + email;
    if (type == 130 || type == 132) {
        $("#chat").html("");
    };
    if (type == 132 || type == 133) {
        var obj = document.getElementById(id1);
        if (obj != null)
            obj.parentNode.removeChild(obj);
    } else {
        var obj = document.getElementById(id2);
        if (obj != null)
            obj.parentNode.removeChild(obj);
    }
}

function inOutMessage(type, jsonObject) {
    var email = jsonObject["email"];
    if (type % 10 == 0)
        email = "recent:" + email;
    var obj = document.getElementById(email);
    if (type < 150) {
        obj.getElementsByClassName("status off")[0].setAttribute("class", "status on");
    } else {
        obj.getElementsByClassName("status on")[0].setAttribute("class", "status off");
    }
}