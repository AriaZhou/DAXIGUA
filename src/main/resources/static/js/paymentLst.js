function onSearch(obj){
    var table = document.getElementById('paymentTable');//獲取table的id標識
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

function deleteSinglePayment(name) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deletePayment",
        data:{
            paymentid : name,
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
    console.log(ids);

    if(ids.length === 0){
        alert("您选一个啊，点checkbox！");
        return false;
    }

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deletePaymentGroup",
        data:{
            paymentid : ids.toString()
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
                return  false;
            }
        }
    });
}

function checkedAll(obj) {
    $("input[name='pItem']").each(function(index) {
        if(document.getElementById('paymentTable').rows[index+1].style.display === '')
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

function setPaymentTrue(name) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/setPaymentTrue",
        data:{
            paymentid : name,
        },
        async: false,
        error: function(request){
            alert("删除失败，请重试。");
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

function uploadExcel(){
    var excelFile=$("#excelFile").val();
    if(excelFile==""||excelFile.length==0){
        alert("请选择文件路径!(.xlsx)");
        return;
    }
    if(excelFile.indexOf(".xlsx")==-1){
        alert("请选择正确格式文件!(.xlsx)");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/admin/uploadpayment",
        data: {'excelFile':new FormData($("#uploadE")[0])},
        dataType : "json",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        clearForm:true,
        async: false,
        error: function(request){
            alert("删除失败，请重试。");
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
