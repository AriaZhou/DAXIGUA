function deletePayment(name, state) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/deletePayment",
        data: {
            payid: name,
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

function singlepaymentForm(price, id, type) {

    document.getElementById('totPrice').innerHTML = price + " 元";
    document.getElementById('remark').innerHTML = id;
    document.getElementById('payment').style.display = 'block';

    if(type===0){
        document.getElementById('zfbImg').src='../picture/img_0006.jpg';
        document.getElementById('wxImg').src='../picture/img_0007.jpg';
    }
    else if(type===1) {
        document.getElementById('zfbImg').src = '../picture/ddzfb.png';
        document.getElementById('wxImg').src = '../picture/ddweixin.png';
    }
    else {
        document.getElementById('zfbImg').src = '../picture/gyzfb.png';
        document.getElementById('wxImg').src = '../picture/gyweixin.png';
    }
}

function copyText() {
    var copyText = document.getElementById("remark");
    copyText.select();
    copyText.setSelectionRange(0, 99999);
    document.execCommand("copy");
    return false;
}

function showDetail(id) {
    console.log(id);
    $.ajax({
        cache: true,
        type: "POST",
        url: "/searchPaymentDetail",
        data: {
            payid: id
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
                document.getElementById("detailValue").innerText = data;
                document.getElementById("orderDetial").style.display = "block";
                return false;

            }
        }
    });
}
