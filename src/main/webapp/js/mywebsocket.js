var websocket = null;

//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
    websocket = new WebSocket("ws://localhost:8080/websocket");
}
else{
    alert('Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function(){
    setMessageInnerHTML("error");
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
        console.log("Status: " + type);
        $(".recent").attr("src", "img/recent_unread.png");
    }
    if (type == 100 || type == 102)
        setMessageInnerHTML(jsonObject);
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