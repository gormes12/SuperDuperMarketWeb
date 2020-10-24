var zonesVersion = 0;
var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var USER_TYPE_URL = buildUrlWithContextPath("usertype");
var UPLOAD_FILE_URL = buildUrlWithContextPath("uploadfile");
var ZONE_LIST_URL = buildUrlWithContextPath("zones");
var PASS_SCREEN = buildUrlWithContextPath("passScreen");

$(function() {
    ajaxUsersList();
    triggerAjaxZonesContent();

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
                $("#message-upload-file-label").text("File Error:\n" + res.responseText).addClass("w3-red").toggleClass("w3-green");
            },
            success: function(res) {
                $("#message-upload-file-label").text("File Successfully Uploaded").toggleClass("w3-red").addClass("w3-green").toggleClass("w3-red");
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
                $("<input type='file' name='file1'>" +
                    "<input type='submit' value='Add Zone'/><br><br>").appendTo($("#upload-file"));
            }
        }
    });
}