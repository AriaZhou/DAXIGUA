function deleteOrder(name, state) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/deleteOrder",
        data: {
            orderid: name,
        },
        async: false,
        error: function (request) {
            alert("删除失败，请重试。");
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("删除失败，请重试。");
            } else {

                alert("删除成功");
                window.location.reload();
                return false;

            }
        }
    });
}

function checkedAll(obj) {
    $("input[name='oItem']").each(function (index) {
        if (document.getElementById('orderTable').rows[index + 1].style.display === '')
            this.checked = obj.checked;
    });
}

function checkedAllFreight(obj) {
    $("input[name='foItem']").each(function (index) {
        if (document.getElementById('orderTable').rows[index + 1].style.display === '')
            this.checked = obj.checked;
    });
}

function paymentForm() {
    if ($("input[name='oItem']:checked").length === 0) {
        alert('请先勾选你需要支付的订单');
        return;
    }

    var totalPrice = 0;
    var remark = "";
    $("input[name='oItem']").each(function (index) {
        if ($(this).is(':checked')) {
            remark += parseInt($(this).attr('id')) + " ";
            totalPrice += parseFloat(document.getElementById('orderTable').rows[index + 1].cells[6].getElementsByTagName('p')[0].innerHTML.split('￥')[1]);
        }
    });

    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/addPayment",
        data: {
            ids: remark,
            price: totalPrice
        },
        async: false,
        error: function (request) {
            alert("好像出问题了，重试一下吧。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("好像出问题了，重试一下吧。");
                return false;
            } else {
                document.getElementById('totPrice').innerHTML = totalPrice + " 元";
                document.getElementById('remark').innerHTML = data;
                document.getElementById('zfbImg').src='../picture/img_0006.jpg';
                document.getElementById('wxImg').src='../picture/img_0007.jpg';
                document.getElementById('payment').style.display = 'block';
                return false;
            }
        }
    });
}

function freightForm() {
    if ($("input[name='foItem']:checked").length === 0) {
        alert('请先勾选你需要支付的订单');
        return false;
    }

    var totalPrice = 0,
        pstate = parseInt($("input[name='foItem']:checked").attr('id').toString().split(' ')[1]);
    var remark = "";
    $("input[name='foItem']").each(function (index) {
        if ($(this).is(':checked')) {
            if (pstate !== parseInt($(this).attr('id').toString().split(' ')[1])) {
                alert("请选择同一个清货人的订单");
                pstate = -1;
                return false;
            }
            remark += parseInt($(this).attr('id').toString().split(' ')[0]) + " ";
            totalPrice += parseFloat(document.getElementById('orderTable').rows[index + 1].cells[6].getElementsByTagName('p')[0].innerHTML.split('￥')[1]);
        }
    });

    if (pstate === -1)
        return false;

    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/addFreightPayment",
        data: {
            ids: remark
        },
        async: false,
        error: function (request) {
            alert("好像出问题了，重试一下吧。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("好像出问题了，重试一下吧。");
                return false;
            } else {
                document.getElementById('totPrice').innerHTML = data.split(" ")[0] + " 元";
                document.getElementById('remark').innerHTML = data.split(" ")[1];
                if(pstate===4){
                    document.getElementById('zfbImg').src='../picture/ddzfb.png';
                    document.getElementById('wxImg').src='../picture/ddweixin.png';
                }else{
                    document.getElementById('zfbImg').src='../picture/gyzfb.png';
                    document.getElementById('wxImg').src='../picture/gyweixin.png';
                }
                document.getElementById('payment').style.display = 'block';

                return false;
            }
        }
    });
}

function singlepaymentForm(price, id) {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/addPayment",
        data: {
            ids: id,
            price: price
        },
        async: false,
        error: function (request) {
            alert("好像出问题了，重试一下吧。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("有人买了这个商品了，不能删了哦。");
                return false;
            } else {
                document.getElementById('totPrice').innerHTML = price + " 元";
                document.getElementById('remark').innerHTML = data;
                document.getElementById('zfbImg').src='../picture/img_0006.jpg';
                document.getElementById('wxImg').src='../picture/img_0007.jpg';
                document.getElementById('payment').style.display = 'block';

                return false;
            }
        }
    });
}

function singleFreightpaymentForm(price, id, pstate) {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/addFreightPayment",
        data: {
            ids: id,
            type: parseInt(pstate)-3
        },
        async: false,
        error: function (request) {
            alert("好像出问题了，重试一下吧。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("有人买了这个商品了，不能删了哦。");
                return false;
            } else {
                document.getElementById('totPrice').innerHTML = price + " 元";
                document.getElementById('remark').innerHTML = data.split(" ")[1];
                if(pstate===4){
                    document.getElementById('zfbImg').src='../picture/ddzfb.png';
                    document.getElementById('wxImg').src='../picture/ddweixin.jpg';
                }else{
                    document.getElementById('zfbImg').src='../picture/gyzfb.png';
                    document.getElementById('wxImg').src='../picture/gyweixin.png';
                }
                document.getElementById('payment').style.display = 'block';
                return false;
            }
        }
    });
}

function copyText() {
    var copyText = document.getElementById("remark");
    copyText.select();
    copyText.setSelectionRange(0, 99999);
    document.execCommand("copy");
    return false;
}
