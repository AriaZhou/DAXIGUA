function onSearch(obj){
    var table = document.getElementById('userTable');//獲取table的id標識
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

var uusername;
var uname;
var uemail;
var uaddress;
var upassword;
var uphone;
var ureceiver;
var uprovince;
var ucity;
var udistrict;

function rollBack(){

    document.getElementById("username").value = uusername;
    document.getElementById("name").value = uname;
    document.getElementById("email").value = uemail;
    document.getElementById("address").value = uaddress;
    document.getElementById("password").value = upassword;
    document.getElementById("phone").value = uphone;
    document.getElementById("receiver").value = ureceiver;
    document.getElementById("province").value = uprovince;
    document.getElementById("city").value = ucity;
    document.getElementById("district").value = udistrict;

    $("#insertUser").find("input").each(function(){
        var width=textWidth($(this).val());
        $(this).width(width)
    });

}

function insertU(){
    document.getElementById("insertUser").style.display='block';

    document.getElementById("formTitle").innerHTML = "新增用户";
    document.getElementById("rollback").hidden = true;
    document.getElementById("insertConfirm").hidden = false;
    document.getElementById("modifyConfirm").hidden = true;
    document.getElementById("username").value = null;
    document.getElementById("name").value = null;
    document.getElementById("email").value = null;
    document.getElementById("address").value = null;
    document.getElementById("password").value = null;
    document.getElementById("phone").value = null;
    document.getElementById("receiver").value = null;
    document.getElementById("province").value = null;
    document.getElementById("city").value = null;
    document.getElementById("district").value = null;

    $("#insertUser").find("input").each(function(){
        var width=textWidth($(this).val());
        $(this).width("fit-content")
    });

}

function modifyU(username,name,email,receiver,province,city,district,address,phone){
    document.getElementById("insertUser").style.display='block';

    document.getElementById("formTitle").innerHTML = "修改用户";
    document.getElementById("rollback").hidden = false;
    document.getElementById("insertConfirm").hidden = true;
    document.getElementById("modifyConfirm").hidden = false;
    document.getElementById("username").value = username;
    document.getElementById("name").value = name;
    document.getElementById("email").value = email;
    document.getElementById("address").value = address;
    document.getElementById("password").value = '00000000';
    document.getElementById("phone").value = phone;
    document.getElementById("receiver").value = receiver;
    document.getElementById("province").value = province;
    document.getElementById("city").value = city;
    document.getElementById("district").value = district;

    uusername = username;
    uname = name;
    uemail = email;
    uaddress = address;
    upassword = '00000000';
    uphone = phone;
    ureceiver = receiver;
    uprovince = province;
    ucity = city;
    udistrict = district;

    $("#insertUser").find("input").each(function(){
        var width=textWidth($(this).val());
        $(this).width(width)
    });

}

//获取文本宽度
function textWidth(text){
    var sensor = $('<pre>'+ text +'</pre>').css({display: 'none'});
    $('body').append(sensor);
    var width = sensor.width();
    sensor.remove();
    return width+6;
};

function modifyUser() {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/modifyUser",
        data:$('#userInfo').serialize(),
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

function addUser() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/addUser",
        data:$('#userInfo').serialize(),
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
                alert("新增失败，该QQ号已经注册过了。");
                return false;
            }
        }
    });
}

function deleteSingleUser(name) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/admin/deleteUser",
        data:{
            userid : name,
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
    $("input[name = 'uItem']:checked").each(function () {
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
        url: "/admin/deleteUserGroup",
        data:{
            userid : ids.toString()
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
    $("input[name='uItem']").each(function(index) {
        if(document.getElementById('userTable').rows[index+1].style.display === '')
            this.checked = obj.checked;
    });
}

function showExport(){
    var ids = [];
    $("input[name='uItem']:checked").each(function(index) {
        ids.push($(this).attr('id'));
    });

    if(ids.length>0){
        console.log(ids);
        location.href="/admin/exportSelectedData?ids="+ids;
    }else{
        location.href="/admin/exportData";
    }

}
