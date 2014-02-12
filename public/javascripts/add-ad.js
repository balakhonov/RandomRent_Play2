$(function () {
    var type = 0;
    var period = 0;

    $(".rent-filter a").click(function () {
        $(".rent-filter a").removeClass("selected");
        $(".rent-filter2 a").removeClass("selected");
        $(this).addClass("selected");

        $(".rent-filter2").hide();
        $(".rent-filter3").hide();
    });
    $(".rent-filter .rent-want").click(function () {
        type = 1;
        $(".rent-filter2").show();
    });
    $(".rent-filter .rent-offer").click(function () {
        type = 2;
        $(".rent-filter2").show();
    });
    $(".rent-filter .rent-buy").click(function () {
        type = 3;
        showResult(type);
    });
    $(".rent-filter .rent-sell").click(function () {
        type = 4;
        showResult(type);
    });

    $(".rent-filter2 a").click(function () {
        $(".rent-filter2 a").removeClass("selected");
        $(this).addClass("selected");
    });
    $(".rent-filter2 .lt").click(function () {
        period = 1;
        showResult(type, period);
    });
    $(".rent-filter2 .st").click(function () {
        period = 2;
        showResult(type, period);
    });

    function showResult(type, period) {
        $(".rent-filter3").show();

        if (type == 1 || type == 2) {
            var param = "?type=" + type + "&period=" + period + "&cid=1";

            $("#ro-1").attr("href", LANG_PREFIX + "/add-apartment" + param);
            $("#ro-2").attr("href", LANG_PREFIX + "/add-room" + param);
            $("#ro-3").attr("href", LANG_PREFIX + "/add-house" + param);
            $("#ro-4").attr("href", LANG_PREFIX + "/add-garage" + param);
            $("#ro-5").attr("href", LANG_PREFIX + "/add-com-place" + param);
            $("#ro-6").attr("href", LANG_PREFIX + "/add-stead" + param).hide();
        } else {
            $("#ro-1").attr("href", LANG_PREFIX + "/add-apartment?type=" + type);
            $("#ro-2").attr("href", LANG_PREFIX + "/add-room?type=" + type);
            $("#ro-3").attr("href", LANG_PREFIX + "/add-house?type=" + type);
            $("#ro-4").attr("href", LANG_PREFIX + "/add-garage?type=" + type);
            $("#ro-5").attr("href", LANG_PREFIX + "/add-com-place?type=" + type);
            $("#ro-6").attr("href", LANG_PREFIX + "/add-stead?type=" + type);
        }
    }
});