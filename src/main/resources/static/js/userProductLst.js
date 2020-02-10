function decCount(name) {
    var count = document.getElementById(name);
    if (parseInt(count.value) !== 0) {
        count.value = parseInt(count.value) - 1;
    }
}

function addCount(name) {
    var count = document.getElementById(name);
    count.value = parseInt(count.value) + 1;
}

function submitOrder(name) {
    var count = document.getElementById(name);
    var button = document.getElementById("sub" + name);
    var countNum = parseInt(count.value);
    var pId = name;

    if (countNum === 0) {
        alert("订单提交失败，订单数量不可为0，请重试。");
        return false;
    }

    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/addOrder",
        data: {
            pId: pId,
            count: countNum,
        },
        async: false,
        error: function (request) {
            alert("订单提交失败，请重试。");
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("订单提交失败，请重试。");
            } else {
                count.value = 0;
                alert("订单提交成功");

                return false;

            }
        }
    });
}

function submitOrderGroup() {
    var ids = [];
    var nums = [];
    $("input").each(function () {
        if (this.value !== "0" && this.value !== "登出") {
            ids.push($(this).attr('id'));
            nums.push(this.value);
        }
    });

    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/addOrderGroup",
        data: {
            productid: ids.toString(),
            productNum: nums.toString()
        },
        async: false,
        error: function (request) {
            alert("下单失败，请重试。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("下单失败，请重试。");
                return false;
            } else {
                alert("下单成功");
                window.location.reload();
                return false;
            }
        }
    });
}
