function onSearch(obj) {
    var table = document.getElementById('paymentTable'); //獲取table的id標識
    var rowsLength = table.rows.length; //表格總共有多少行
    var columnsLength = table.rows.item(0).cells.length; //表格總共有多少列
    var key = obj.value; //獲取輸入框的值
    for (var i = 1; i < rowsLength; i++) { //按表的行數進行迴圈，本例第一行是標題，所以i=1，從第二行開始篩選（從0數起）
        for (var j = 1; j < columnsLength - 1; j++) {
            var searchText = table.rows[i].cells[j].getElementsByTagName('p')[0].innerHTML; //取得table行，列的值
            if (searchText.match(key)) { //用match函式進行篩選，如果input的值，即變數 key的值為空，返回的是ture，
                table.rows[i].style.display = ''; //顯示行操作，
                break;
            } else {
                table.rows[i].style.display = 'none'; //隱藏行操作
            }
        }
    }
}

function sendSearch() {
    var param = document.getElementById('searchBar');
    window.location.href="/admin/revealPaymentByParam?param=" + param.value;
}

var page = 0;
var deleteLst = [];

function showConfirm(id){
    deleteLst = [];

    if(id != null)
    {
        deleteLst.push(id);
    }
    else
    {
        $("input[name = 'pItem']:checked").each(function () {
            deleteLst.push($(this).attr('id'));
        });

        if(deleteLst.length === 0){
            alert("您选一个啊，点checkbox！");
            return false;
        }
    }

    document.getElementById("confirmBox").style.display='block';
}

function deletePayment() {
    document.getElementById("confirmBox").style.display='none';

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deletePayment",
        data: {
            paymentid: deleteLst.toString(),
        },
        async: false,
        error: function (request) {
            alert("删除失败，请重试。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            if (data === "0") {
                alert("删除失败，请重试。");
                return false;
            } else {
                alert("删除成功");
                window.location.reload();
                return false;
            }
        }
    });
}

function checkedAll(obj) {
    $("input[name='pItem']").each(function (index) {
        if (document.getElementById('paymentTable').rows[index + 1].style.display === '')
            this.checked = obj.checked;
    });
}

function showExport() {
    var ids = [];
    $("input[name='pItem']:checked").each(function (index) {
        ids.push($(this).attr('id'));
    });

    if (ids.length > 0) {
        console.log(ids);
        location.href = "/admin/exportSelectedProductData?ids=" + ids;
    } else {
        location.href = "/admin/exportProductData";
    }

}

function dispalyPriceBox(pid) {
    document.getElementById("insertPrice").style.display = "block";
    document.getElementById("paidId").value = pid;
}

function setPaymentTrue() {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/setPaymentTrue",
        data: {
            paymentid: document.getElementById("paidId").value,
            actualPrice: document.getElementById("paidPrice").value,
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
                window.location.reload();
                return false;

            }
        }
    });
}


function setPaymentFalse(name) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/setPaymentFalse",
        data: {
            paymentid: name,
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
                window.location.reload();
                return false;

            }
        }
    });
}

function importPayment() {
    window.location.reload();
}

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

function loadMore() {
    var param = document.getElementById('searchBar');
    page = page + 1;

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/loadMorePayment",
        data: {
            param: param.value,
            page: page
        },
        async: false,
        error: function (request) {
            alert("修改失败，请重试。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function (data) {
            var div = document.createElement('table');
            div.innerHTML = data.trim();
            if(div.childNodes.length != 1){
                $('#loadMoreData').append(div.childNodes.item(1).childNodes);
            }
            else{
                document.getElementById('buttonMsg').innerHTML = '<p style="color: #888888; font-size: 12pt">没有更多了</p>';
            }
            return false;

        }
    });
}
