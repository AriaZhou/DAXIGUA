

function onSearch(obj){
    var table = document.getElementById('orderTable');//獲取table的id標識
    var rowsLength = table.rows.length;//表格總共有多少行
    var columnsLength = table.rows.item(0).cells.length;//表格總共有多少列
    var key = obj.value;//獲取輸入框的值
    for(var i=1;i<rowsLength-1;i++){//按表的行數進行迴圈，本例第一行是標題，所以i=1，從第二行開始篩選（從0數起）
        for(var j=1; j<columnsLength; j++){
            var searchText = table.rows[i].cells[j].getElementsByTagName('p')[0].innerHTML;//取得table行，列的值
            if(searchText.match(key)){//用match函式進行篩選，如果input的值，即變數 key的值為空，返回的是ture，
                table.rows[i].style.display='';//顯示行操作，
                break;
            }else{
                table.rows[i].style.display='none';//隱藏行操作
            }
        }
    }
}

(function($) {
        $.expr[":"].Contains = function(a, i, m) {
            return (a.textContent || a.innerText || "").toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
        };
        function filterList(header, list) {
            //@header 头部元素
            //@list 无需列表
            //创建一个搜素表单
            var form = $("<form>").attr({
                "class":"filterform",
                action:"#"
            }), input = $("<input>").attr({
                "class":"filterinput",
                type:"text"
            });
            $(form).append(input).appendTo(header);
            $(input).change(function() {
                var filter = $(this).val();
                if (filter) {
                    $matches = $(list).find("a:Contains(" + filter + ")").parent();
                    $("li", list).not($matches).slideUp();
                    $matches.slideDown();
                } else {
                    $(list).find("li").slideDown();
                }
                return false;
            }).keyup(function() {
                $(this).change();
            });
        }
        $(function() {
            filterList($("#form"), $("#demo-list"));
        });
    })(jQuery);

var price;

var oGroupId;
var oproductId;
var oPrice;
var oCount;
var oState;
var oUsername;

function rollBack(){

    document.getElementById("groupid").value = oGroupId;
    document.getElementById("name").value = oproductId;
    document.getElementById("price").value = oPrice;
    document.getElementById("ocount").value = oCount;
    document.getElementById("state").value = oState;
    document.getElementById("user").value = oUsername;

    var plist = document.getElementById('oProductLst').getElementsByTagName('a');
    for (let i = 0; i < plist.length; i++) {
        var p = plist.item(i);
        if(p.title===oGroupId)
            p.hidden=false;
        else
            p.hidden=true;
    }
}

function insertO(){
    document.getElementById("insertOrder").style.display='block';

    document.getElementById("formTitle").innerHTML = "新增订单";
    document.getElementById("rollback").hidden = true;
    document.getElementById("insertConfirm").hidden = false;
    document.getElementById("modifyConfirm").hidden = true;
    document.getElementById("groupid").value = null;
    document.getElementById("name").value = null;
    document.getElementById("price").value = null;
    document.getElementById("ocount").value = null;
    document.getElementById("state").value = null;
    document.getElementById("user").value = null;

}

function modifyO(groupId,productId,name,price,count,state,username){
    document.getElementById("insertOrder").style.display='block';

    document.getElementById("formTitle").innerHTML = "修改订单";
    document.getElementById("rollback").hidden = false;
    document.getElementById("insertConfirm").hidden = true;
    document.getElementById("modifyConfirm").hidden = false;
    document.getElementById("groupid").value = groupId;
    document.getElementById("name").value = productId;
    document.getElementById("price").value = price;
    document.getElementById("ocount").value = count;
    document.getElementById("state").value = state;
    document.getElementById("user").value = username;

    var plist = document.getElementById('oProductLst').getElementsByTagName('a');
    for (let i = 0; i < plist.length; i++) {
        var p = plist.item(i);
        if(p.title===groupId)
            p.hidden=false;
        else
            p.hidden=true;
    }

    oGroupId = groupId;
    oproductId = productId;
    oPrice = price;
    oCount = count;
    oState = state;
    oUsername = username;

}

function modifyOrder() {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/modifyOrder",
        data:$('#orderInfo').serialize(),
        async: false,
        error: function(request){
            alert("修改失败，请重试。");
            // alert("Connection error:"+request.error);
            return false;
        },
        success: function(data){
            if(data==="0"){
                alert("修改失败，请重试。");
                return false;
            }else{
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
        data:$('#orderInfo').serialize(),
        async: false,
        error: function(request){
            alert("新增失败，请重试。");
            // alert("Connection error:"+request.error);
            return false;
        },
        success: function(data){
            if(data==="1"){
                alert("新增成功");
                window.location.reload();
                return false;
            }else{
                alert("新增失败，请检查QQ号填写是否正确。");
                return false;
            }
        }
    });
}

function deleteOrder(name) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deleteOrder",
        data:{
            orderid : name,
        },
        async: false,
        error: function(request){
            alert("删除失败，请重试。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function(data){
            if(data==="0"){
                alert("删除失败，请重试。");
                return false;
            }else{
                alert("删除成功");
                window.location.reload();
                return false;

            }
        }
    });
}

function deleteGroup(){
    var flag = 0;
    $("input[name = 'oItem']:checked").each(function () {

        $.ajax({
            cache: true,
            type: "POST",
            url: "/admin/deleteOrder",
            data:{
                orderid : $(this).attr('id')
            },
            async: false,
            error: function(request){
                alert("删除失败，请重试。");
                flag = 1;
                // alert("Connection error:"+request.error);
            },
            success: function(data){
                if(data==="0"){
                    alert("删除失败，请重试。");
                    flag = 1;
                }
            }
        });
        if(flag === 1)
            return false;

    });

    if(flag === 0){
        alert("删除成功");
        window.location.reload();
    }
}

function addPriceValue(id, name, priceValue) {
    document.getElementById('name').value=id;
    document.getElementById('ocount').readOnly = false;
    price = parseInt(priceValue);
    if(document.getElementById('ocount').value !== null){
        document.getElementById("price").value = document.getElementById('ocount').value*price;
    }
}

function changePriceValue(count) {
    document.getElementById("price").value = count*price;
}

function addStateValue(stateValue) {
    document.getElementById("state").value = stateValue;
}

function addNameValue(groupid) {
    document.getElementById('groupid').value = groupid;
    document.getElementById('name').value = null;
    var plist = document.getElementById('oProductLst').getElementsByTagName('a');
    for (let i = 0; i < plist.length; i++) {
        var p = plist.item(i);
        if(p.title===groupid)
            p.hidden=false;
        else
            p.hidden=true;
    }
}

function checkedAll(obj) {
    $("input[name='oItem']").each(function(index) {
        if(document.getElementById('oderTable').rows[index+1].style.display === '')
            this.checked = obj.checked;
    });
}
