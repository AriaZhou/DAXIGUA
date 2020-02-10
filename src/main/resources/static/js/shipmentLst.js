function showDetail(id) {
    console.log(id);
    $.ajax({
        cache: true,
        type: "POST",
        url: "/searchPaymentDetail",
        data: {
            payid: id,
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

function setShipmentTrue(id, btn) {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/setShipmentTrue",
        data: {
            paymentid: id
        },
        async: false,
        error: function (request) {
            alert("删除失败，请重试。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("修改失败，请重试。");
                return false;
            } else {
                alert("修改成功");
                btn.style.background = "gray";
                btn.style.borderColor = "gray";
                btn.onclick = "return false";
                return false;
            }
        }
    });
}
