
function modifyUser() {
    console.log($('#basicInfo').serialize());

    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/modifyUser",
        data: $('#basicInfo').serialize(),
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

function modifyPassword() {

    var oldPassword = document.getElementById("oldPassword").value;
    var newPassword = document.getElementById("newPassword").value;
    var newPasswordAgain = document.getElementById("newPasswordAgain").value;

    if (newPassword !== newPasswordAgain) {
        document.getElementById("errorMsg").hidden = false;
        return false;
    }

    $.ajax({
        cache: true,
        type: "POST",
        url: "/user/modifyPassword",
        data: {
            oldPsw: oldPassword,
            newPsw: newPassword
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
                alert(data);
                window.location.reload();
                return false;
            }
        }
    });
}
