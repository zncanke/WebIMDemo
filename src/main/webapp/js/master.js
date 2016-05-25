function chat(fromUserName, toUserName) {
    $.ajax({
        type: "POST",
        url: "/chat/chat",
        data:{fromUserName:fromUserName, toUserName:toUserName},
        success: function(result){
            $("#chatDiv").html(result);
        }
    });
}

function sendMessage(fromUserName, toUserName) {
    if(!toUserName) {
        alert("没有聊天对象");
        return ;
    }

    var message = $("#messageTextarea").val();
    if(!message) {
        alert("信息不能为空");
        return ;
    }

    $.ajax({
        type: "POST",
        url: "/chat/sendMessage",
        data:{fromUserName:fromUserName, toUserName:toUserName, content:message},
        success: function(result){
            //alert(result.status);
            if(result.status == 200) {
                $("#messageTextarea").val("");
                var trObj = $("<tr></tr>");
                trObj.html('<td colspan="2" align="right">'+ message +'</td>');
                trObj.prependTo("#chatLogTbody");
            }
        },
        error: function(result) {
            alert(result);
        }
    });
}

function acceptMessage(fromUserName, toUserName) {
    $.ajax({
        type: "POST",
        url: "/chat/acceptMessage",
        data:{fromUserName:fromUserName, toUserName:toUserName},
        success: function(result){
            if(result.status == 1) {
                chatRecordList(fromUserName, toUserName);
            }
            acceptMessage(fromUserName, toUserName);
        }
    });
}
//master.js添加当有新消息时刷新消息列表
function chatRecordList(fromUserName, toUserName) {
    $.ajax({
        type: "POST",
        url: "/chat/chatRecordList",
        data:{fromUserName:fromUserName, toUserName:toUserName, page:0, limit:10},
        success: function(result){
            $("#chatRecordList").html(result);
        }
    });
}