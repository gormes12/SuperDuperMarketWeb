var zonesVersion = 0;
var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var USER_TYPE_URL = buildUrlWithContextPath("usertype");
var UPLOAD_FILE_URL = buildUrlWithContextPath("uploadfile");
var ZONE_LIST_URL = buildUrlWithContextPath("zones");

$(function() {
    ajaxUsersList();
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);

    triggerAjaxZonesContent();
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
            console.log("Server Zones version: " + data.version + ", Current zones version: " + zonesVersion);
            if (data.version !== zonesVersion) {
                zonesVersion = data.version;
                appendToZonesInfo(data.entries);
            }
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

    // add the relevant entries
    $.each(entries || [], function (index, entry) {
        $("<div class=\"w3-quarter w3-Light-Blue\">" +
            "<div class=\"w3-card w3-container w3-round-xlarge\">" +
            "<h3 class=\"w3-center\">" + entry.zoneName + "</h3><br>" +
            "  <p> Owner: " + entry.ownerName + "</p>" +
            "  <p> Total Item Type: " + entry.totalItemType + "</p>" +
            "  <p> Total Stores: " + entry.totalStores + "</p>" +
            "  <p> Total Orders: " + entry.totalOrders + "</p>" +
            "  <p> Average Order Price: " + entry.avgOrderPrice + "</p>" +
            "  </div>" +
            "  </div>").appendTo(zonesInfo);
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
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=user-list) element
        $('<li>' + user.username + ": " + user.userType + '</li>').appendTo(userList);
    });
}

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
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
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
                //do not add the user string to the chat area
                //since it's going to be retrieved from the server
                //$("#result h1").text(r);
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