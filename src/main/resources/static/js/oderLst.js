function onSearch(obj) {
    var table = document.getElementById('orderTable'); //獲取table的id標識
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
    window.location.href="/admin/revealOrderByParam?param=" + param.value;
}

var price;
var page=0;
var deleteLst = [];

var oGroupId;
var oproductId;
var oName;
var oPrice;
var oCount;
var oState;
var oUsername;

function rollBack() {

    document.getElementById("groupid").value = oGroupId;
    document.getElementById("productid").value = oproductId;
    document.getElementById("productValue").value = oName;
    document.getElementById("price").value = oPrice;
    document.getElementById("ocount").value = oCount;
    document.getElementById("state").value = oState;
    document.getElementById("user").value = oUsername;

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/getProductListByGroup",
        data: {
            id: oGroupId
        },
        async: false,
        error: function (request) {
            alert("新增失败，请重试。");
            // alert("Connection error:"+request.error);
            return false;
        },
        success: function (data) {
            var innerhtmlValue = "";

            data.forEach(function (pp) {
                innerhtmlValue += "<a class=\"dropdown-item\" onclick=\"addPriceValue('" + pp.toString().split(' ')[0] + "','" +
                    pp.toString().split(' ')[1] + "','" + pp.toString().split(' ')[2] + "')\">" +
                    pp.toString().split(' ')[1] + "</a>"
            });

            document.getElementById("oProductLst").innerHTML = innerhtmlValue;
            return false;
        }
    });
}

function insertO() {
    document.getElementById("insertOrder").style.display = 'block';
    document.getElementById("oProductLst").innerHTML = "";

    document.getElementById("formTitle").innerHTML = "新增订单";
    document.getElementById("ocount").readOnly = true;
    document.getElementById("rollback").hidden = true;
    document.getElementById("insertConfirm").hidden = false;
    document.getElementById("modifyConfirm").hidden = true;
    document.getElementById("groupid").value = null;
    document.getElementById("productid").value = null;
    document.getElementById("productValue").value = null;
    document.getElementById("price").value = null;
    document.getElementById("ocount").value = null;
    document.getElementById("state").value = null;
    document.getElementById("user").value = null;

}

function modifyO(id, groupId, productId, name, pprice, orderPrice, count, state, username) {
    document.getElementById("insertOrder").style.display = 'block';

    document.getElementById("formTitle").innerHTML = "修改订单";
    document.getElementById("ocount").readOnly = false;
    document.getElementById("rollback").hidden = false;
    document.getElementById("insertConfirm").hidden = true;
    document.getElementById("modifyConfirm").hidden = false;
    document.getElementById("id").value = id;
    document.getElementById("groupid").value = groupId;
    document.getElementById("productid").value = productId;
    document.getElementById("productValue").value = name;
    document.getElementById("price").value = orderPrice;
    document.getElementById("ocount").value = count;
    document.getElementById("state").value = state;
    document.getElementById("user").value = username;

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/getProductListByGroup",
        data: {
            id: groupId
        },
        async: false,
        error: function (request) {
            alert("新增失败，请重试。");
            // alert("Connection error:"+request.error);
            return false;
        },
        success: function (data) {
            var innerhtmlValue = "";

            data.forEach(function (pp) {
                innerhtmlValue += "<a class=\"dropdown-item\" onclick=\"addPriceValue('" + pp.toString().split(' ')[0] + "','" +
                    pp.toString().split(' ')[1] + "','" + pp.toString().split(' ')[2] + "')\">" +
                    pp.toString().split(' ')[1] + "</a>"
            });
            document.getElementById("oProductLst").innerHTML = innerhtmlValue;
            return false;
        }
    });

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/getGroupList",
        data: {},
        async: false,
        error: function (request) {
            alert("新增失败，请重试。");
            // alert("Connection error:"+request.error);
            return false;
        },
        success: function (data) {
            var innerhtmlValue = "";

            data.forEach(function (pp) {
                innerhtmlValue += "<a class=\"dropdown-item\" onclick=\"addNameValue('" + pp + "')\">" + pp + "</a>"
            });
            document.getElementById("oGroupLst").innerHTML = innerhtmlValue;
            return false;
        }
    });

    oGroupId = groupId;
    oproductId = productId;
    oName = name;
    oPrice = orderPrice;
    oCount = count;
    oState = state;
    oUsername = username;

    price = parseInt(pprice);

}

function modifyOrder() {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/modifyOrder",
        data: $('#orderInfo').serialize(),
        async: false,
        error: function (request) {
            alert("修改失败，请重试。");
            // alert("Connection error:"+request.error);
            return false;
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

function addOrder() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/addOrder",
        data: $('#orderInfo').serialize(),
        async: false,
        error: function (request) {
            alert("新增失败，请重试。");
            // alert("Connection error:"+request.error);
            return false;
        },
        success: function (data) {
            if (data === "1") {
                alert("新增成功");
                window.location.reload();
                return false;
            } else {
                alert("新增失败，请检查QQ号填写是否正确。");
                return false;
            }
        }
    });
}

function showConfirm(id){
    deleteLst = [];

    if(id != null)
    {
        deleteLst.push(id);
    }
    else
    {
        $("input[name = 'oItem']:checked").each(function () {
            deleteLst.push($(this).attr('id'));
        });

        if(deleteLst.length === 0){
            alert("您选一个啊，点checkbox！");
            return false;
        }
    }

    document.getElementById("confirmBox").style.display='block';
}

function deleteOrder() {
    document.getElementById("confirmBox").style.display='none';

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deleteOrder",
        data: {
            orderid: deleteLst.toString(),
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

function addPriceValue(id, name, priceValue) {
    document.getElementById("productid").value = id;
    document.getElementById("productValue").value = name;
    document.getElementById('ocount').readOnly = false;
    price = parseInt(priceValue);
    if (document.getElementById('ocount').value !== null) {
        document.getElementById("price").value = document.getElementById('ocount').value * price;
    }
}

function changePriceValue(count) {
    document.getElementById("price").value = count * price;
}

function addStateValue(stateValue) {
    document.getElementById("state").value = stateValue;
}

function addNameValue(groupid) {
    document.getElementById('groupid').value = groupid;
    document.getElementById("productid").value = null;
    document.getElementById("productValue").value = null;

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/getProductListByGroup",
        data: {
            id: groupid
        },
        async: false,
        error: function (request) {
            alert("新增失败，请重试。");
            // alert("Connection error:"+request.error);
            return false;
        },
        success: function (data) {
            var innerhtmlValue = "";

            data.forEach(function (pp) {
                innerhtmlValue += "<a class=\"dropdown-item\" onclick=\"addPriceValue('" + pp.toString().split(' ')[0] + "','" +
                    pp.toString().split(' ')[1] + "','" + pp.toString().split(' ')[2] + "')\">" +
                    pp.toString().split(' ')[1] + "</a>"
            });
            document.getElementById("oProductLst").innerHTML = innerhtmlValue;
            return false;
        }
    });
}

function checkedAll(obj) {
    $("input[name='oItem']").each(function (index) {
        if (document.getElementById('orderTable').rows[index + 1].style.display === '')
            this.checked = obj.checked;
    });
}

function showExport() {
    var ids = [];
    $("input[name='oItem']:checked").each(function (index) {
        ids.push($(this).attr('id'));
    });

    if (ids.length > 0) {
        location.href = "/admin/exportSelectedData?ids=" + ids;
    } else {
        location.href = "/admin/exportData";
    }

}

function importOrders() {
    window.location.reload();
}

function setState(stateId) {
    var ids = [];
    $("input[name='oItem']:checked").each(function () {
        ids.push($(this).attr('id'));
    });


    if (ids.length === 0) {
        alert("你勾选的列表为空！");
        return false;
    }

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/modifyOrderState",
        data: {
            id: ids.toString(),
            state: stateId
        },
        async: false,
        error: function (request) {
            alert("修改失败，请重试。");
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

function loadMore() {
    var param = document.getElementById('searchBar');
    page = page + 1;

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/loadMoreOrder",
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