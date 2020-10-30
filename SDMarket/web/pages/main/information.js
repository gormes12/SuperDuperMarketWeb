var zonesVersion = 0;
var chatVersion = 0;
var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var CHAT_SEND_URL = buildUrlWithContextPath("sendChat");
var CHAT_LIST_URL = buildUrlWithContextPath("chat");
var USER_TYPE_URL = buildUrlWithContextPath("usertype");
var UPLOAD_FILE_URL = buildUrlWithContextPath("uploadfile");
var ZONE_LIST_URL = buildUrlWithContextPath("zones");
var PASS_SCREEN = buildUrlWithContextPath("passScreen");

$(function() {
    ajaxUsersList();
    ajaxZonesContent();
    ajaxChatContent();
    initChatForm();
    setInterval(ajaxUsersList, refreshRate);
    initChooseZone();

});

function triggerAjaxZonesContent() {
    setTimeout(ajaxZonesContent, refreshRate);
}

function ajaxZonesContent() {
    $.ajax({
        url: ZONE_LIST_URL,
        data: "zonesversion=" + zonesVersion,
        dataType: 'json',
        success: function(data) {
            /*
             data will arrive in the next form:
             {
                "entries": [
                    {
                        "avgOrderPrice": 0,
                        "ownerName": "maorgo",
                        "totalItemType": 10,
                        "totalOrders": 0,
                        "totalStores": 4,
                        "zoneName": "Hasharon"
                    },
                    {
                        "avgOrderPrice": 0,
                        "ownerName": "maorgo",
                        "totalItemType": 5,
                        "totalOrders": 0,
                        "totalStores": 2,
                        "zoneName": "Galil Maarvi"
                    }
                ],
                "version":1
             }
             */
            console.log("Server Zones version: " + data.version + ", Current zones version: " + zonesVersion);
            // if (data.version !== zonesVersion) {
                zonesVersion = data.version;
                appendToZonesInfo(data.entries);
            // }
            triggerAjaxZonesContent();
        },
        error: function(error) {
            triggerAjaxZonesContent();
        }
    });
}

//entries = the added chat strings represented as a single string
function appendToZonesInfo(entries) {
    var zonesInfo = $("#zone-info");
    if (zonesVersion === entries.length){
        zonesInfo.empty();
    }
    // add the relevant entries
    $.each(entries || [], function (index, entry) {
        $("<div class=\"w3-quarter w3-margin\">" +
                "<div class=\"w3-card w3-container w3-round-xlarge w3-light-blue\">" +
                    "<h3 class=\"w3-center\">" + entry.zoneName + "</h3>" +
                    "<p> Owner: " + entry.ownerName + "</p>" +
                    "<p> Total Item Type: " + entry.totalItemType + "</p>" +
                    "<p> Total Stores: " + entry.totalStores + "</p>" +
                    "<p> Total Orders: " + entry.totalOrders + "</p>" +
                    "<p> Average Order Price: " + entry.avgOrderPrice.toFixed(2) + "</p>" +
                    "<form action='passScreen' method='post' class='pass-screen w3-center' style='padding-bottom: 8px'>" +
                        "<input name='chooseZone' value=\"" + entry.zoneName + "\" style='display: none'/>" +
                        "<input class='w3-round-large w3-btn' style='border: 1px solid black ' type='submit' value='Enter'/>" +
                    "</form>" +
                "</div>" +
        "</div>").appendTo(zonesInfo);
    });
}

function initChooseZone(){
    $(".pass-screen").submit(function() {
        var parameters = $(this).serialize();

        $.ajax({
            method: 'POST',
            data: parameters,
            url: PASS_SCREEN,
            timeout: 2000,
            error: function(res) {
            },
            success: function(res) {
            }
        });

        return false;
    });
}

function refreshUsersList(users) {
    var userList = $("#user-list");

    //clear all current users
    userList.empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        console.log("Adding user #" + index + ": " + user);
        $('<li>' + user.username + ": " + user.userType + '</li>').appendTo(userList);
    });
}

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        },
        error: function (){
            console.log("error");
        }
    });
}

$(function () {
    addUploadFileButton();
    $("#uploadform").submit(function() {
        var file = this[0].files[0];

        var formData = new FormData();
        formData.append("file", file);

        $.ajax({
            method: 'POST',
            data: formData,
            url: UPLOAD_FILE_URL,
            processData: false,
            contentType: false,
            timeout: 4000,
            error: function(res) {
                $("#message-upload-file-label").text("File Error:\n" + res.responseText).removeClass("w3-text-green").addClass("w3-text-red");
            },
            success: function(res) {
                $("#message-upload-file-label").text("File Successfully Uploaded").removeClass("w3-text-red").addClass("w3-text-green");
            }
        });

        return false;
    });
});

function addUploadFileButton() {
    $.ajax({
        url: USER_TYPE_URL,
        success: function(userType) {
            if (userType === "StoreOwner"){
                $("<input type='file' id='file1'>" +
                    "<label for=\"file1\" class=\"btn-file\">upload zone</label> " +
                    "<button class='w3-round-xxlarge w3-btn' style='box-shadow:0 8px 16px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19)' type='submit' value='Add Zone'>Add</button><br>" +
                    "<span class=\"file-name\">\n" +
                    "" +
                    "</span>").appendTo($("#upload-file"));

                $('#file1').change(function() {
                    var i = $('.file-name').clone();
                    var file;
                    if ($('#file1')[0].files[0] == null){
                       file = "No File Selected";
                    } else{
                       file = $('#file1')[0].files[0].name;
                    }
                    $('.file-name').text(file);
                    $("#message-upload-file-label").text("");
                });

            }
        }
    });
}

function initChatForm(){
    $("#chatform").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: CHAT_SEND_URL,
            timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {

            }
        });

        $("#userstring").val("");
        return false;
    });
};

function appendToChatArea(messages) {

    // add the relevant entries
    $.each(messages || [], appendChatEntry);

    // handle the scroller to auto scroll to the end of the chat area
    var scroller = $("#chatarea");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({ scrollTop: height }, "slow");
}

function appendChatEntry(index, message){
    var entryElement = createChatEntry(message);
    $("#chatarea").append(entryElement).append("<br>");
}

function createChatEntry (message){
    message.chatString = message.chatString.replace(":)", "<img style='width: 20px; height: 20px' src='../../imageAndIcon/happy-smile.png'/>")
        .replace(":(", "<img style='width: 20px; height: 20px' src='../../imageAndIcon/sad-smile.png'/>");
    return "<div class=\"w3-container w3-padding-8\" style='border: 2px solid #dedede; background-color: #f1f1f1; border-radius: 5px;'>" +
        "  <label style='grid-template-columns: fit-content(100px) fit-content(100px) 1fr;'>" + message.username + ": " +  message.chatString + "</label><br><br>" +
        "  <label class=\"w3-right\">" + message.time + "</label><br>\n" +
        "</div>";
}

function ajaxChatContent() {
    $.ajax({
        url: CHAT_LIST_URL,
        data: "chatversion=" + chatVersion,
        dataType: 'json',
        success: function(data) {
            /*
             data will arrive in the next form:
             {
                "entries": [
                    {
                        "chatString":"Hi",
                        "username":"bbb",
                        "time":1485548397514
                    },
                    {
                        "chatString":"Hello",
                        "username":"bbb",
                        "time":1485548397514
                    }
                ],
                "version":1
             }
             */
            if (data.version !== chatVersion) {
                chatVersion = data.version;
                appendToChatArea(data.entries);
            }
            triggerAjaxChatContent();
        },
        error: function(error) {
            triggerAjaxChatContent();
        }
    });
};

function triggerAjaxChatContent() {
    setTimeout(ajaxChatContent, refreshRate);
};