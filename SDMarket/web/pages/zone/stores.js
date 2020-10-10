// var itemsVersion = 0;
var refreshRate = 2000; //milli seconds
var chosenStoreInStaticOrder="";
// var currentItem = 1;
var ITEMS_LIST_URL = buildUrlWithContextPath("itemslist");
var STORES_LIST_URL = buildUrlWithContextPath("storeslist");
var ORDER_DETAILS_URL = buildUrlWithContextPath("historyOrders");
var SCREEN_ONE_MAKE_ORDER = buildUrlWithContextPath("makeOrderScOne")

$(function() {
    ajaxItemsContent();
    triggerAjaxItemsContent();
    setInterval(ajaxStoresContent, refreshRate);
    initShowOrderDetailsForm();
});

function triggerAjaxItemsContent() {
    setTimeout(ajaxItemsContent, refreshRate);
}

/*function ajaxItemsContent() {
    $.ajax({
        url: ITEMS_LIST_URL,
        data: "itemsversion=" + itemsVersion,
        dataType: 'json',
        success: function(data) {
            /!*
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
             *!/
            console.log("Server Items version: " + data.version + ", Current items version: " + itemsVersion);
            if (data.version !== itemsVersion) {
                itemsVersion = data.version;
                appendToItemsInfo(data.entries);
                initItemsSlide();

            }
            triggerAjaxItemsContent();
        },
        error: function(error) {
            triggerAjaxItemsContent();
        }
    });
}

//entries = the added chat strings represented as a single string
function appendToItemsInfo(entries) {
    var itemsInfo = $("#items-info");
    var scroller = $(".scroll");
    if (itemsVersion === entries.length){
        itemsInfo.empty();
        currentItem = 1;
    }
    // add the relevant entries
    $.each(entries || [], function (index, entry) {
        $(
            "<div class=\"w3-card-4 w3-round-xlarge w3-center mySlides\" style=\"width:300px\">" +
            "<header class=\"w3-container w3-light-grey\">" +
            "<h3>" + entry.itemName + "</h3><br>" +
            "</header>" +
            "<div class=\"w3-container\">" +
            "<p> SerialNumber: " + entry.serialNumber + "</p>" +
            "<p> Purchase Category: " + entry.purchaseCategory + "</p>" +
            "<p> No. Stores Sell: " + entry.howManyStoreSold + "</p>" +
            "<p> Average Price: " + entry.averagePrice.toFixed(2) + "</p>" +
            "<p> No. Amount Sold: " + entry.howManyTimeSold + "</p>" +
            "</div>" +
            "</div>").appendTo(itemsInfo);
        $("<span class=\"w3-badge demo w3-border w3-transparent w3-hover-dark-grey\" onclick=\"currentDiv(" + currentItem + ")\"></span>").appendTo(scroller);
        currentItem = currentItem + 1;
    });
}*/

    var slideIndex = 1;
function initItemsSlide() {
    showDivs(slideIndex);
}
function plusDivs(n) {
    showDivs(slideIndex += n);
}

function currentDiv(n) {
    showDivs(slideIndex = n);
}

function showDivs(n) {
    var i;
    var x = $(".mySlides");
    var dots = $(".demo");
    if (n > x.length) {slideIndex = 1}
    if (n < 1) {slideIndex = x.length}
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" w3-dark-grey", "");
    }
    x[slideIndex-1].style.display = "block";
    dots[slideIndex-1].className += " w3-dark-grey";
}

function ajaxItemsContent() {
    $.ajax({
        url: ITEMS_LIST_URL,
        success: function (items) {
            appendToItemsInfo(items);
            initItemsSlide();
            triggerAjaxItemsContent();
        },
        error: function (error) {
            triggerAjaxItemsContent();
        }
    });
}

function appendToItemsInfo(items) {
    var itemsInfo = $("#items-info");
    var scroller = $(".scroll");

        itemsInfo.empty();
        scroller.empty();

    $.each(items || [], function (index, item) {
        $(
            "<div class=\"w3-card-4 w3-round-xlarge w3-center mySlides\" style=\"width:300px\">" +
            "<header class=\"w3-container w3-light-grey\">" +
            "<h3>" + item.itemName + "</h3><br>" +
            "</header>" +
            "<div class=\"w3-container\">" +
            "<p> SerialNumber: " + item.serialNumber + "</p>" +
            "<p> Purchase Category: " + item.purchaseCategory + "</p>" +
            "<p> No. Stores Sell: " + item.howManyStoreSold + "</p>" +
            "<p> Average Price: " + item.averagePrice.toFixed(2) + "</p>" +
            "<p> No. Amount Sold: " + item.howManyTimeSold + "</p>" +
            "</div>" +
            "</div>").appendTo(itemsInfo);
        $("<span class=\"w3-badge demo w3-border w3-transparent w3-hover-dark-grey\" onclick=\"currentDiv(" + (index+1) + ")\"></span>").appendTo(scroller);
    });
}

function ajaxStoresContent() {
    $.ajax({
        url: STORES_LIST_URL,
        success: function (stores) {
            appendToStoresInfo(stores);
            refreshStoresForStaticOrder(stores);
        },
        error: function (error) {
        }
    });
}

function appendToStoresInfo(stores) {
    var storesInfo = $("#zone-stores");
    var storesComboBox = $("#stores");

    storesInfo.empty();

    $.each(stores || [], function (index, store) {
        /*$(
            "<button onclick=\"openStoreAccordion('"+store.id+"')\" class=\"w3-btn w3-block w3-light-grey w3-center-align\">"+store.storeName+"</button>" +
            "<div id=\""+store.id+"\" class=\"w3-container w3-center w3-content w3-hide\">" +
            "<h4> Store Owner: " + store.ownerName + "</h4><br>" +
            "<p> Store ID: " + store.id + "</p>" +
            "<p> Location: (" + store.xCoordinate +","+ store.yCoordinate + ")" + "</p>" +
            "<p> No. Orders: " + store.orders.length + "</p>" +
            "<p> Total revenues of items sold: " + store.totalItemsSoldRevenues.toFixed(2) + "</p>" +
            "<p> PPK: " + store.pricePerKilometer.toFixed(2) + "</p>" +
            "<p> Total Deliveries Revenues: " + store.totalDeliveriesRevenues.toFixed(2) + "</p>" +
            "</div>").appendTo(storesInfo);*/
        $(
            "<option value=\"" + store.storeName +"\">" + store.storeName + "</option>"
        ).appendTo(storesComboBox);

        $(
            "<div class=\"w3-panel w3-cyan w3-round-xlarge \">" +
                "<div class=\"w3-col w3-left\" style='width: 29%'>" +
                    "<p> Store Name: " + store.storeName + "</p>" +
                    "<p> Store Owner: " + store.ownerName + "</p>" +
                    "<p> Store ID: " + store.id + "</p>" +
                    "<p> Location: (" + store.xCoordinate +","+ store.yCoordinate + ")" + "</p>" +
                    "<p> No. Orders: " + store.orders.length + "</p>" +
                    "<p> Total revenues of items sold: " + store.totalItemsSoldRevenues.toFixed(2) + "</p>" +
                    "<p> PPK: " + store.pricePerKilometer.toFixed(2) + "</p>" +
                    "<p> Total Deliveries Revenues: " + store.totalDeliveriesRevenues.toFixed(2) + "</p>" +
                "</div>" +
                "<div class=\"w3-rest\">" +
                    "<table id=\""+store.id+"\" class=\"w3-margin w3-table w3-bordered w3-striped w3-centered\" style=\"max-width: 800px\">" +
                        "<tr class=\"w3-light-grey\">\n" +
                            "<th>Serial Number</th>" +
                            "<th>Item Name</th>" +
                            "<th>Purchase Category</th>" +
                            "<th>Price</th>" +
                            "<th>Total Sold</th>" +
                        "</tr>" +
                    "</table>" +
                "</div>" +
            "</div>"
        ).appendTo(storesInfo);

        var itemContainer = $("#"+store.id);
        $.each(store.superMarketItems || [], function (index, item) {
            $(
                "<tr>" +
                "<td>" + item.serialNumber + "</td>" +
                "<td>" + item.itemName + "</td>" +
                "<td>" + item.purchaseCategory + "</td>" +
                "<td>" + item.price + "</td>" +
                "<td>" + item.totalItemSold + "</td>" +
                "</tr>"
            ).appendTo(itemContainer);
        });
    });
}

function initShowOrderDetailsForm() {

    $("#ShowOrderDetailsForm").submit(function () {
        var parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: ORDER_DETAILS_URL,
            timeout: 2000,
            error: function () {
                console.error("Failed to submit");
            },
            success: function (orders) {
                appendOrdersToDetailsTable(orders);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
};

function appendOrdersToDetailsTable(orders){
    var orderDetailsTable = $("#order-details-table");

    orderDetailsTable.empty();


    $(
        "<table id=\"details-table\" class=\"w3-margin w3-table w3-bordered w3-striped w3-centered\" style=\"max-width: 800px\">" +
            "<tr class=\"w3-light-grey\">\n" +
                "<th>No. Order</th>" +
                "<th>Date</th>" +
                "<th>Customer Name</th>" +
                "<th>Customer Location</th>" +
                "<th>Total Items Purchased</th>" +
                "<th>Items Purchased Cost</th>" +
                "<th>Delivery Cost</th>" +
            "</tr>" +
        "</table>"
    ).appendTo(orderDetailsTable);

    var detailsTable = $("#details-table");
    $.each(orders || [], function (index, order) {
        $(
            "<tr onclick='showItems(order.shoppingCart)'>" +
                "<td>" + order.orderID + "</td>" +
                "<td>" + order.orderDate + "</td>" +
                "<td>" + order.customerName + "</td>" +
                "<td> (" + order.customerXCoordinate + "," + order.customerYCoordinate + ")</td>" +
                "<td>" + order.TotalItemAmount + "</td>" +
                "<td>" + order.TotalItemsPrice.toFixed(2) + "</td>" +
                "<td>" + order.deliveryCost.toFixed(2) + "</td>" +
            "</tr>"
        ).appendTo(detailsTable);
    });
}

function showItems(items){
    var itemsDetailsTable = $("#items-order-details");

    itemsDetailsTable.empty();

    $(
        "<table id=\"items-table\" class=\"w3-margin w3-table w3-bordered w3-striped w3-centered\" style=\"max-width: 800px\">" +
        "<tr class=\"w3-light-grey\">\n" +
        "<th>Serial Number</th>" +
        "<th>Name</th>" +
        "<th>Purchase Category</th>" +
        "<th>Amount</th>" +
        "<th>Price Per Unit</th>" +
        "<th>Total Price</th>" +
        "<th>From Sale</th>" +
        "</tr>" +
        "</table>"
    ).appendTo(itemsDetailsTable);

    var detailsTable = $("#items-table");
    $.each(items || [], function (index, item) {
        $(
            "<tr>" +
            "<td>" + item.SerialNumber + "</td>" +
            "<td>" + item.ItemName + "</td>" +
            "<td>" + item.PurchaseCategory + "</td>" +
            "<td>" + item.Amount + "</td>" +
            "<td>" + item.Price + "</td>" +
            "<td>" + item.TotalPrice + "</td>" +
            "<td>" + item.IsFromSale + "</td>" +
            "</tr>"
        ).appendTo(detailsTable);
    });
}

function appendFeedback(){
    var feedbackInfo = $("#feedback-details");

    feedbackInfo.empty();

    $.each(stores || [], function (index, store) {
        $(
            "<div class=\"w3-panel w3-light-grey w3-round-xlarge \">" +
            "<p> Store Name: " + store.storeName + "</p>" +
            "<p> Store Owner: " + store.ownerName + "</p>" +
            "<p> Store ID: " + store.id + "</p>" +
            "<p> Location: (" + store.xCoordinate +","+ store.yCoordinate + ")" + "</p>" +
            "<p> No. Orders: " + store.orders.length + "</p>" +
            "<p> Total revenues of items sold: " + store.totalItemsSoldRevenues.toFixed(2) + "</p>" +
            "<p> PPK: " + store.pricePerKilometer.toFixed(2) + "</p>" +
            "<p> Total Deliveries Revenues: " + store.totalDeliveriesRevenues.toFixed(2) + "</p>" +
            "</div>"
        ).appendTo(feedbackInfo);

    });
}

function showStoresForStaticOrder(){
    $("#stores-for-static-order").show();
}

function hideStoresForStaticOrder(){
    $("#stores-for-static-order").hide();
}

function refreshStoresForStaticOrder(stores){
    var storesContainer = $("#stores-for-static-order");
    storesContainer.empty();

    $(
        "<table id=\"stores-order-table\" class=\"w3-margin w3-table w3-bordered w3-striped w3-centered\" style=\"max-width: 800px\">" +
        "<tr class=\"w3-light-grey\">\n" +
        "<th>Store ID</th>" +
        "<th>Name</th>" +
        "<th>Store Location</th>" +
        "<th>PPK</th>" +
        "</tr>" +
        "</table>"
    ).appendTo(storesContainer);

    var detailsTable = $("#stores-order-table");
    $.each(stores || [], function (index, store) {
        $(
            "<tr onclick='storeChosen(store.id)'>" +
            "<td>" + store.id + "</td>" +
            "<td>" + store.storeName + "</td>" +
            "<td> (" + store.xCoordinate + "," + store.yCoordinate + ")</td>" +
            "<td>" + store.pricePerKilometer.toFixed(2) + "</td>" +
            "</tr>"
        ).appendTo(detailsTable);
    });

}

function storeChosen(id){
    chosenStoreInStaticOrder = id;
}

function initMakeOrderForm(){
    $("#orderform").submit(function () {
        var parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: SCREEN_ONE_MAKE_ORDER,
            timeout: 2000,
            error: function () {
                console.error("Failed to submit");
            },
            success: function (orders) {
                appendOrdersToDetailsTable(orders);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });

}
/*function openStoreAccordion(id) {
    var x = $("#" + id);
    x.toggleClass("w3-show");
}*/
