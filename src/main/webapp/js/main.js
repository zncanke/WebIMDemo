function claerResizeScroll() {
    $("#texxt").val("");
    $(".messages").getNiceScroll(0).resize();
    return $(".messages").getNiceScroll(0).doScrollTop(999999, 999);
};

function insertI(username) {
  var innerText;
  innerText = $.trim($("#texxt").val());
  if (innerText !== "") {
    $(".messages").append("" +
        "<li class=\"i\">" +
        "<div class=\"head\">" +
        "<span class=\"time\">" + (new Date().getHours()) + ":" + (new Date().getMinutes()) + " AM, Today</span>" +
        "<span class=\"name\">"+ username +"</span>" +
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
    $(".messages").niceScroll(lol);
    $("#texxt").keypress(function(e) {
      if (e.keyCode === 13) {
        insertI();
        return false;
      }
    });
    /*return $(".send").click(function() {
      return insertI();
    });*/
  });

}).call(this);
