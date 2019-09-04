$(function() {
        $("#groupTable").tablesorter();
    });

function onSearch(obj){
    var table = document.getElementById('groupTable');//獲取table的id標識
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

laydate.render({
    elem: '#endtime'
    ,theme: '#cc9897'
    ,type: 'datetime'
});

laydate.render({
    elem: '#starttime'
    ,theme: '#cc9897'
    ,type: 'datetime'
});

var mStartTime;
var mEndTime;

function rollBack(){
    document.getElementById("starttime").value = mStartTime;
    document.getElementById("endtime").value = mEndTime;
}

function insertG(){
    document.getElementById("insertGroup").style.display='block';

    document.getElementById("formTitle").innerHTML = "开团";
    document.getElementById("rollback").hidden = true;
    document.getElementById("insertConfirm").hidden = false;
    document.getElementById("modifyConfirm").hidden = true;
    document.getElementById("id").disabled=false;
    document.getElementById("id").value = null;
    document.getElementById("starttime").value = null;
    document.getElementById("endtime").value = null;

}

function modifyG(id,startTime,endTime){
    document.getElementById("insertGroup").style.display='block';

    document.getElementById("formTitle").innerHTML = "修改团";
    document.getElementById("rollback").hidden = false;
    document.getElementById("insertConfirm").hidden = true;
    document.getElementById("modifyConfirm").hidden = false;
    document.getElementById("id").value = id;
    document.getElementById("id").disabled = true;
    document.getElementById("starttime").value = startTime;
    document.getElementById("endtime").value = endTime;

    mStartTime=startTime;
    mEndTime=endTime;

}

function modifyGroup() {

    document.getElementById("id").disabled=false;

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/modifyGroup",
        data:$('#groupInfo').serialize(),
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
                document.getElementById("id").disabled=true;
                alert("修改成功");
                window.location.reload();
                return false;
            }
        }
    });
}

function addGroup() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/addGroup",
        data:$('#groupInfo').serialize(),
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

function deleteSingleGroup(name) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deleteGroup",
        data:{
            groupid : name
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
    $("input[name = 'gItem']:checked").each(function () {

        $.ajax({
            cache: true,
            type: "POST",
            url: "/admin/deleteGroup",
            data:{
                groupid : $(this).attr('id')
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

function checkedAll(obj) {
    $("input[name='pItem']").each(function(index) {
        if(document.getElementById('productTable').rows[index+1].style.display === '')
            this.checked = obj.checked;
    });
}