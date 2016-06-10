//滑动条到最后
function claerResizeScroll() {
    $("#texxt").val("");
    $(".messages").getNiceScroll(0).resize();
    return $(".messages").getNiceScroll(0).doScrollTop(999999, 999);
};

//发送消息给对方,包括发给服务器及页面中聊天框添加内容
function insertI(fromEmail, toEmail, fromUsername, toUsername) {
  var innerText;
  innerText = $.trim($("#texxt").val());
  if (innerText !== "") {
      send(JSON.stringify({type: "sendMessage",
                           fromEmail: fromEmail, toEmail: toEmail, content: innerText,
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



//页面载入时需要做的操作
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
      //好友栏滚动条设置
      $(".list-friends").niceScroll(conf);
      //添加好友点击动作
      $(".add").click(function() {
          $.ajax({
            type: "GET",
             url: "/friend/searchInterface",
            success: function (result) {
                $("#chat").html(result);
            }
          });
      });
      //添加分组点击动作
      $("#addGroup").click(function () {
          $.ajax({
              type: "POST",
              url: "/user/groupManager",
              success: function (result) {
                  $("#chat").html(result);
              }
          });

      });

      $("#myhead").click(function () {
          $.ajax({
              type: "POST",
              url: "/user/detailinfo",
              data: {email: "self"},
              success: function (result) {
                  $("#chat").html(result);
              }
          });
      });

      $(".exit").click(function () {
          window.location.href = "/login";
      });

      $(".recent").click(function () {
          $.ajax({
              type: "GET",
              url: "/user/recent",
              success: function (result) {
                  $(".list-friends").html(result);
              }
          });
      });

      $(".friends").click(function () {
          $.ajax({
              type: "GET",
              url: "/user/friendList",
              success: function (result) {
                  $(".list-friends").html(result);
              }
          });
      })

  });

}).call(this);


//将消息显示在网页上
function setMessageInnerHTML(jsonObject){
    document.getElementById('messages').innerHTML +=
        "<li class=\"friend-with-a-SVAGina\">" +
        "<div class=\"head\"> <span class=\"time\">10:13 AM, Today</span>" +
        "<span class=\"name\">" + jsonObject.fromUsername + "</span>" +
        "</div>" +
        "<div class=\"message\">" + jsonObject.content + "</div>" +
        "</li>"
    claerResizeScroll();
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function(){
    send(JSON.stringify({type:"userLogout"}));
    closeWebSocket();
};

