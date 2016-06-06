function claerResizeScroll() {
    $("#texxt").val("");
    $(".messages").getNiceScroll(0).resize();
    return $(".messages").getNiceScroll(0).doScrollTop(999999, 999);
};

function insertI(fromEmail, toEmail, fromUsername, toUsername) {
  var innerText;
  innerText = $.trim($("#texxt").val());
  if (innerText !== "") {
      send(JSON.stringify({fromEmail: fromEmail, toEmail: toEmail, content: innerText,
                            fromUsername: fromUsername, toUsername: toUsername}));
  }
  if (innerText !== "") {
    $(".messages").append("" +
        "<li class=\"i\">" +
        "<div class=\"head\">" +
        "<span class=\"time\">" + (new Date().getHours()) + ":" + (new Date().getMinutes()) + " AM, Today</span>" +
        "<span class=\"name\"> "+ fromUsername +" </span>" +
        "</div>" +
        "<div class=\"message\">" + innerText + "</div>" +
        "</li>");
    claerResizeScroll();
    return;
  }
};

(function() {
  var conf, lol;

  conf = {
    cursorcolor: "#696c75",
    cursorwidth: "4px",
    cursorborder: "none"
  };

  lol = {
    cursorcolor: "#cdd2d6",
    cursorwidth: "4px",
    cursorborder: "none"
  };

  $(document).ready(function() {
      $(".list-friends").niceScroll(conf);
      $(".add").click(function() {
          $.ajax({
            type: "GET",
             url: "/friend/searchInterface",
            success: function (result) {
                $("#chat").html(result);
            }
          });
      });
  });

}).call(this);



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
websocket.onopen = function(event){
    setMessageInnerHTML("open");
}

//接收到消息的回调方法
websocket.onmessage = function(){
    
    setMessageInnerHTML(event.data);
}

//连接关闭的回调方法
websocket.onclose = function(){
    setMessageInnerHTML("close");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function(){
    websocket.close();
}



//将消息显示在网页上
function setMessageInnerHTML(innerHTML){
    var jsonObject = JSON.parse(innerHTML);
    document.getElementById('messages').innerHTML +=
        "<li class=\"friend-with-a-SVAGina\">" +
            "<div class=\"head\"> <span class=\"time\">10:13 AM, Today</span>" +
                "<span class=\"name\">" + jsonObject.fromUsername + "</span>" +
            "</div>" +
            "<div class=\"message\">" + jsonObject.content + "</div>" +
        "</li>"
    claerResizeScroll();
}

//关闭连接
function closeWebSocket(){
    websocket.close();
}

//发送消息
function send(message){
    websocket.send(message);
}

