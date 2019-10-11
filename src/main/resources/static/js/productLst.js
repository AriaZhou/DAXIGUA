function onSearch(obj){
    var table = document.getElementById('productTable');//獲取table的id標識
    var rowsLength = table.rows.length;//表格總共有多少行
    var columnsLength = table.rows.item(0).cells.length;//表格總共有多少列
    var key = obj.value;//獲取輸入框的值
    for(var i=1;i<rowsLength;i++){//按表的行數進行迴圈，本例第一行是標題，所以i=1，從第二行開始篩選（從0數起）
        for(var j=1; j<columnsLength-1; j++){
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

// (function($) {
//         $.expr[":"].Contains = function(a, i, m) {
//             return (a.textContent || a.innerText || "").toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
//         };
//         function filterList(header, list) {
//             //@header 头部元素
//             //@list 无需列表
//             //创建一个搜素表单
//             var form = $("<form>").attr({
//                 "class":"filterform",
//                 action:"#"
//             }), input = $("<input>").attr({
//                 "class":"filterinput",
//                 type:"text"
//             });
//             $(form).append(input).appendTo(header);
//             $(input).change(function() {
//                 var filter = $(this).val();
//                 if (filter) {
//                     $matches = $(list).find("a:Contains(" + filter + ")").parent();
//                     $("li", list).not($matches).slideUp();
//                     $matches.slideDown();
//                 } else {
//                     $(list).find("li").slideDown();
//                 }
//                 return false;
//             }).keyup(function() {
//                 $(this).change();
//             });
//         }
//         $(function() {
//             filterList($("#form"), $("#demo-list"));
//         });
//     })(jQuery);

var mGroupid;
var mName;
var mPrice;
var mCount;
var mStarttime;
var mEndtime;
var mDescription;

function rollBack(){
    document.getElementById("groupid").value = mGroupid;
    document.getElementById("name").value = mName;
    document.getElementById("price").value = mPrice;
    document.getElementById("count").value = mCount;
    document.getElementById("starttime").value = mStarttime;
    document.getElementById("endtime").value = mEndtime;
    document.getElementById("description").value = mDescription;
}

function insertP(){
    document.getElementById("insertProduct").style.display='block';

    document.getElementById("formTitle").innerHTML = "新增产品";
    document.getElementById("rollback").hidden = true;
    document.getElementById("insertConfirm").hidden = false;
    document.getElementById("modifyConfirm").hidden = true;
    document.getElementById("groupid").value = null;
    document.getElementById("name").value = null;
    document.getElementById("price").value = null;
    document.getElementById("count").value = null;
    document.getElementById("starttime").value = null;
    document.getElementById("endtime").value = null;
    document.getElementById("description").value = null;

}

function modifyP(id, gid,name,price,count,state,startTime,endTime,description){
    document.getElementById("insertProduct").style.display='block';

    document.getElementById("formTitle").innerHTML = "修改产品";
    document.getElementById("rollback").hidden = false;
    document.getElementById("insertConfirm").hidden = true;
    document.getElementById("modifyConfirm").hidden = false;
    document.getElementById("id").value = id;
    document.getElementById("groupid").value = gid;
    document.getElementById("name").value = name;
    document.getElementById("price").value = price;
    document.getElementById("count").value = count;
    document.getElementById("state").value = state;
    document.getElementById("starttime").value = startTime;
    document.getElementById("endtime").value = endTime;
    document.getElementById("description").value = description;


    mGroupid = gid;
    mName = name;
    mPrice= price;
    mCount = count;
    mStarttime = startTime;
    mEndtime = endTime;
    mDescription = description;

}

function modifyProduct() {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/modifyProduct",
        data:$('#productInfo').serialize(),
        async: false,
        error: function(request){
            alert("修改失败，请重试。");
            return false;
            // alert("Connection error:"+request.error);
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

function addProduct() {
    document.getElementById("id").value = document.getElementById("groupid").value+new Date().getTime();

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/addProduct",
        data:$('#productInfo').serialize(),
        async: false,
        error: function(request){
            alert("新增失败，请重试。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function(data){
            if(data==="0"){
                alert("新增失败，请重试。");
                return false;
            }else{
                alert("新增成功");
                window.location.reload();
                return false;
            }
        }
    });
}

function deleteProduct(name) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deleteProduct",
        data:{
            productid : name
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
    var ids = [];
    $("input[name = 'pItem']:checked").each(function () {
        ids.push($(this).attr('id'));
    });

    if(ids.length === 0){
        alert("您选一个啊，点checkbox！");
        return false;
    }

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deleteProductGroup",
        data:{
            productid : ids.toString()
        },
        async: false,
        error: function(request){
            alert("删除失败，请重试。");
            return false;
            // alert("Connection error:"+request.error);
        },
        success: function(data){
            if(data==="0"){
                alert("有人买了这个商品了，不能删了哦。");
                return false;
            }else{
                alert("删除成功");
                window.location.reload();
                return  false;
            }
        }
    });
}

function addTimeValue(gid, stime, endtime) {
    document.getElementById("groupid").value = gid;
    document.getElementById("starttime").value = stime;
    document.getElementById("endtime").value = endtime;
}

function addStateValue(stateValue) {
    document.getElementById("state").value = stateValue;
}

function checkedAll(obj) {
    $("input[name='pItem']").each(function(index) {
        if(document.getElementById('productTable').rows[index+1].style.display === '')
            this.checked = obj.checked;
    });
}

function showExport(){
    var ids = [];
    $("input[name='pItem']:checked").each(function(index) {
        ids.push($(this).attr('id'));
    });

    if(ids.length>0){
        console.log(ids);
        location.href="/admin/exportSelectedProductData?ids="+ids;
    }else{
        location.href="/admin/exportProductData";
    }

}
