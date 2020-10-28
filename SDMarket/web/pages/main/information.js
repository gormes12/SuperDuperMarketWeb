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
    //The users list is refreshed automatically every second
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
                    "<form action='passScreen' method='post' class='pass-screen' style='padding-bottom: 8px'>" +
                        "<input name='chooseZone' value=\"" + entry.zoneName + "\" style='display: none'/>" +
                        "<input type='submit' value='Enter'/>" +
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
                // console.error("Failed to submit");
                // $("#message-upload-file-label").text("File Error:\n" + res.responseText).addClass("w3-red").toggleClass("w3-green");
            },
            success: function(res) {
                //$("#message-upload-file-label").text("File Successfully Uploaded").toggleClass("w3-red").addClass("w3-green").toggleClass("w3-red");
            }
        });

        //$("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
}


//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
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
        // formData.append("name", this[1].value);

        $.ajax({
            method: 'POST',
            data: formData,
            url: UPLOAD_FILE_URL,
            processData: false,
            contentType: false,
            timeout: 4000,
            error: function(res) {
                // console.error("Failed to submit");
                $("#message-upload-file-label").text("File Error:\n" + res.responseText).removeClass("w3-text-green").addClass("w3-text-red");
            },
            success: function(res) {
                $("#message-upload-file-label").text("File Successfully Uploaded").removeClass("w3-text-red").addClass("w3-text-green");
            }
        });

        //$("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});

function addUploadFileButton() {
    $.ajax({
        url: USER_TYPE_URL,
        success: function(userType) {
            if (userType === "StoreOwner"){
                $("<input type='file' id='file1'>" +
                    "<label for=\"file1\" class=\"btn-file\">upload</label> " +
                    "<input class='w3-round-xlarge' type='submit' value='Add Zone'/><br>" +
                    "<span class=\"file-name\">\n" +
                    "      No file Selected\n" +
                    "</span>").appendTo($("#upload-file"));

                $('#file1').change(function() {
                    var i = $(this).nextAll('span').clone();
                    var file;
                    if ($('#file1')[0].files[0] == null){
                       file = "No File Selected";
                    } else{
                       file = $('#file1')[0].files[0].name;
                    }
                    $(this).nextAll('span').text(file);
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
                //do not add the user string to the chat area
                //since it's going to be retrieved from the server
                //$("#result h1").text(r);
            }
        });

        $("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
};

function appendToChatArea(messages) {
//    $("#chatarea").children(".success").removeClass("success");

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
    return $("<span class=\"success\">").append(message.username + ": " + message.chatString);
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