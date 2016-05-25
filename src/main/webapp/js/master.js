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