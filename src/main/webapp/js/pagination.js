/**
 * 分页页面内容获取，该函数只能注册到分页的a标签上，ajax成功后调用回调方法callback
 */
function clickGoto(callback, $this) {
    var url = $this.attr("url");
    $.ajax({
        type: "GET",
        url: url,
        success: function(result){
            callback(result);
        },
        error: function(result) {
            alert(result);
        }
    });
}
/**
 * 分页页面内容获取，该函数只能注册到分页的input.text标签上,ajax成功后调用回调方法callback
 */
function changeGoto(callback) {
    var url = $this.attr("url");
    var page = $this.val();
    url = url + "&page="+page;
    $.ajax({
        type: "GET",
        url: url,
        success: function(result){
            callback(result);
        },
        error: function(result) {
            alert(result);
        }
    });
}