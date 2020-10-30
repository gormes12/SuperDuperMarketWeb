var refreshRate = 2000; //milli seconds
var chosenStoreInStaticOrder="";
var ITEMS_LIST_URL = buildUrlWithContextPath("itemslist");
var STORES_LIST_URL = buildUrlWithContextPath("storeslist");
var ORDER_DETAILS_URL = buildUrlWithContextPath("historyOrders");
var SCREEN_ONE_MAKE_ORDER = buildUrlWithContextPath("makeOrderScOne")
var USER_TYPE_URL = buildUrlWithContextPath("usertype");
var ADD_ITEM_TO_CART = buildUrlWithContextPath("addItemToCart");
var SALES_URL = buildUrlWithContextPath("sales");
var ADD_SALE_TO_CART = buildUrlWithContextPath("addSaleToCart");
var SHOW_SUMMARY_ORDER_URL = buildUrlWithContextPath("summaryOrder");
var EXECUTE_ORDER_URL = buildUrlWithContextPath("executeOrder");
var EXECUTE_ADD_NEW_STORE_URL = buildUrlWithContextPath("executeAddNewStore");
var ADD_FEEDBACK_TO_OWNER_STORE = buildUrlWithContextPath("addFeedbackToOwnerStore");
var CUSTOMER_HISTORY_ORDER_DETAILS_URL = buildUrlWithContextPath("customerHistoryOrders");
var GET_FEEDBACK_URL = buildUrlWithContextPath("getFeedbacks");
var SCREEN_ONE_OPEN_STORE = buildUrlWithContextPath("openStoreGetFirstDetails");
var ADD_ITEM_TO_STORE = buildUrlWithContextPath("addItemToStore");
var GET_STORES_TO_ADD_NEW_ITEM_TO_ZONE = buildUrlWithContextPath("getStoresToAddNewItemToZone");
var EXECUTE_ADD_NEW_ITEM_TO_ZONE_URL = buildUrlWithContextPath("executeAddNewItemToZone");


$(function() {
    addRelevantTab();
    ajaxItemsContent();
    ajaxStoresContent();
    initShowOrderDetailsForm();
    initMakeOrderForm();
    ajaxCustomerHistoryOrders();
    ajaxFeedbacksContent();
});

function triggerAjaxItemsContent() {
    setTimeout(ajaxItemsContent, refreshRate);
};

function triggerAjaxStoresContent() {
    setTimeout(ajaxStoresContent, refreshRate);
};

    var slideIndex = 1;
function initItemsSlide() {
    showDivs(slideIndex);
};
function plusDivs(n) {
    showDivs(slideIndex += n);
};

function currentDiv(n) {
    showDivs(slideIndex = n);
};

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
};

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
};

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
};

function ajaxStoresContent() {
    $.ajax({
        url: STORES_LIST_URL,
        success: function (stores) {
            appendToStoresInfo(stores);
            refreshStoresForStaticOrder(stores);
            triggerAjaxStoresContent();
        },
        error: function (error) {
            triggerAjaxStoresContent();
        }
    });
};

function appendToStoresInfo(stores) {
    var storesInfo = $("#zone-stores");
    var storesComboBox = $("#stores");

    var lastSelectedStoreValue = storesComboBox.val();
    storesComboBox.empty();
    storesInfo.empty();

    $.each(stores || [], function (index, store) {
        //next sentence only for comboBox
        $(
            "<option value=\"" + store.id +"\">" + store.storeName + "</option>"
        ).appendTo(storesComboBox);

        $(
            "<div class=\"w3-panel w3-pale-blue w3-round-xlarge\">" +
                "<div class=\"w3-col w3-left\" style='width: 29%'>" +
                    "<p> Store Name: " + store.storeName + "</p>" +
                    "<p> Store Owner: " + store.ownerName + "</p>" +
                    "<p> Store ID: " + store.id + "</p>" +
                    "<p> Location: (" + store.xCoordinate +","+ store.yCoordinate + ")" + "</p>" +
                    "<p> No. Orders: " + store.orders.length + "</p>" +
                    "<p> Total revenues of items sold: " + store.totalItemsSoldRevenues.toFixed(2) + " ₪</p>" +
                    "<p> PPK: " + store.pricePerKilometer.toFixed(2) + " ₪</p>" +
                    "<p> Total Deliveries Revenues: " + store.totalDeliveriesRevenues.toFixed(2) + " ₪</p>" +
                "</div>" +
                "<div class=\"w3-rest\">" +
                    "<table id=\""+store.id+"\" class=\"w3-card w3-margin w3-table w3-bordered w3-striped w3-centered w3-text-black\" style=\"max-width: 800px\">" +
                        "<tr >\n" +
                            "<th>Serial No.</th>" +
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
                "<td>" + item.price.toFixed(2) + " ₪</td>" +
                "<td>" + item.totalItemSold + "</td>" +
                "</tr>"
            ).appendTo(itemContainer);
        });
    });

    storesComboBox.val(""+lastSelectedStoreValue);
};

function initShowOrderDetailsForm() {

    $("#ShowOrderDetailsForm").submit(function () {
        var parameters = $(this).serialize();
        var orderDetailsTable = $("#order-details-table");

        orderDetailsTable.empty();
        $("#items-order-details").empty();

        $.ajax({
            data: parameters,
            url: ORDER_DETAILS_URL,
            timeout: 2000,
            error: function (error) {
                console.error("Failed to submit");
                if (error.responseText.includes("You are not the owner of this store")){
                    $(
                        "<h3>" + error.responseText + "</h3>"
                    ).appendTo(orderDetailsTable);
                }
            },
            success: function (shoppingCarts) {
                if (shoppingCarts.length !== 0) {
                    appendOrdersToDetailsTable(shoppingCarts);
                } else{
                    $(
                        "<h3 style='height: 100vh'>No Order Made!</h3>"
                    ).appendTo(orderDetailsTable);
                }
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
};

function appendOrdersToDetailsTable(shoppingCarts){
    $(
        "<table id=\"details-table\" class=\"w3-margin w3-table w3-bordered w3-striped w3-centered\" style=\"max-width: 70%\">" +
            "<tr class=\"w3-dark-grey\">\n" +
                "<th>No. Order</th>" +
                "<th style='min-width: 120px'>Date</th>" +
                "<th>Customer Name</th>" +
                "<th>Customer Location</th>" +
                "<th>Total Items Purchased</th>" +
                "<th>Items Purchased Cost</th>" +
                "<th>Delivery Cost</th>" +
            "</tr>" +
        "</table>"
    ).appendTo($("#order-details-table"));

    var detailsTable = $("#details-table");
    $.each(shoppingCarts || [], function (index, cart) {
        $(
            "<tr class=\"w3-hover-blue\">" +
                "<td>" + cart.orderID + "</td>" +
                "<td>" + cart.orderDate + "</td>" +
                "<td>" + cart.customerName + "</td>" +
                "<td> (" + cart.customerXCoordinate + "," + cart.customerYCoordinate + ")</td>" +
                "<td>" + cart.TotalItemAmount + "</td>" +
                "<td>" + cart.TotalItemsPrice.toFixed(2) + " ₪</td>" +
                "<td>" + cart.deliveryCost.toFixed(2) + " ₪</td>" +
            "</tr>"
        ).appendTo(detailsTable);
        $( "#details-table tr:last" ).click(function() {
            addOrdersItemsToContainer(cart.items, "items-order-details");
        });
    });
};

function addOrdersItemsToContainer(items, containerID){
    var itemsDetailsTable = $("#"+containerID);

    itemsDetailsTable.empty();

    $(
        "<h6 class='w3-center'><b>Items Details:</b></h6>" +
        "<div style='display: flex; justify-content: center'>" +
        "<table id=\"items-table-"+ containerID +"\" class=\"w3-table w3-padding w3-striped w3-centered\" style=\"max-width: 80%\">" +
        "<thead>" +
        "<tr class=\"w3-light-grey\">\n" +
        "<th>Serial No.</th>" +
        "<th>Name</th>" +
        "<th>Purchase Category</th>" +
        "<th>Quantity</th>" +
        "<th>Price (Per Unit)</th>" +
        "<th>Total Price</th>" +
        "<th>From Sale</th>" +
        "</tr>" +
        "</thead>" +
        "</table>" +
        "</div><br><br>"
    ).appendTo(itemsDetailsTable);

    var detailsTable = $("#items-table-"+containerID);
    $.each(items || [], function (index, item) {
        $(
            "<tr>" +
            "<td>" + item.serialNumber + "</td>" +
            "<td>" + item.itemName + "</td>" +
            "<td>" + item.purchaseCategory + "</td>" +
            "<td>" + item.amount.toFixed(2) + "</td>" +
            "<td>" + item.price.toFixed(2) + " ₪</td>" +
            "<td>" + item.TotalPrice.toFixed(2) + " ₪</td>" +
            "<td id='isFromSale-index" + index + "-" + containerID + "'></td>" +
            "</tr>"
        ).appendTo(detailsTable);
        item.IsFromSale? $("#isFromSale-index"+index+"-"+containerID).text(String.fromCharCode(10003)) : $("#isFromSale-index"+index+"-"+containerID).text(String.fromCharCode(10007));
    });
};

function ajaxFeedbacksContent() {
    $.ajax({
        url: GET_FEEDBACK_URL,
        success: function (feedbacks) {
            var feedbackInfo = $("#feedback-details");
            feedbackInfo.empty();

            if (feedbacks != null) {
                appendFeedback(feedbacks);
            } else{
                $(
                    "<h3>No Feedbacks</h3>"
                ).appendTo(feedbackInfo);
            }
            setTimeout(ajaxFeedbacksContent, 3000);
        },
        error: function (error) {
            setTimeout(ajaxFeedbacksContent, 3000);
        }
    });
};

function appendFeedback(feedbacks){
    var feedbackInfo = $("#feedback-details");
    $(
        "<div class='w3-panel'><div class='w3-center'>"
    ).appendTo(feedbackInfo);
    $.each(feedbacks || [], function (index, feedback) {
        $(
            "<div class=\"w3-panel w3-card-4 w3-light-grey w3-round-medium\" style='width:50%'>\n" +
            "  <div class=\"w3-right\"> " + feedback.date + "</div>" +
            "  <div class=\"rate\">" +
            "                <input type=\"radio\" onclick=\"return false;\" id=star5-order" + index + " name=rate" + index + " value=\"5\" />" +
            "                <label title=\"rate\" for=star5-order" + index +  ">5 stars</label>\n" +
            "                <input type=\"radio\" onclick=\"return false;\" id=star4-order" + index + " name=rate" + index + " value=\"4\" />" +
            "                <label title=\"rate\" for=star4-order" + index +  ">4 stars</label>\n" +
            "                <input type=\"radio\" onclick=\"return false;\" id=star3-order" + index + " name=rate" + index + " value=\"3\" />\n" +
            "                <label title=\"rate\" for=star3-order" + index +  ">3 stars</label>\n" +
            "                <input type=\"radio\" onclick=\"return false;\" id=star2-order" + index + " name=rate" + index + " value=\"2\" />\n" +
            "                <label title=\"rate\" for=star2-order" + index +  ">2 stars</label>\n" +
            "                <input type=\"radio\" onclick=\"return false;\" id=star1-order" + index + " name=rate" + index + " value=\"1\" />\n" +
            "                <label title=\"rate\" for=star1-order" + index +  ">1 star</label>\n" +
            "  </div><br><br>\n" +
            "    <p class=\" w3-large w3-serif w3-center\">\n" +
            "         <img src=\"../../imageAndIcon/quotes.png\" alt=\"quotes\" ><br>" +
            "         <i> " + feedback.textRating + " </i>" +
            "    </p>" +
            "    <p><b> " + feedback.userGiverFeedback + " </b></p>\n" +
            "  </div><br>\n"
        ).appendTo(feedbackInfo);
        $('input[name=rate'+ index +'][value=' + feedback.numericalRating + ']').prop('checked',true)
    });
    $(
        "</div></div><br><br><br>"
    ).appendTo(feedbackInfo);
};

function showStoresForStaticOrder(){
    $("#stores-for-static-order").show();
};

function hideStoresForStaticOrder(){
    $("#stores-for-static-order").hide();
};

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
            "<tr id='tr-store" + store.id + "' class='w3-hover-blue' onclick='storeChosen("+store.id+", this)'>" +
            "<td>" + store.id + "</td>" +
            "<td>" + store.storeName + "</td>" +
            "<td> (" + store.xCoordinate + "," + store.yCoordinate + ")</td>" +
            "<td>" + store.pricePerKilometer.toFixed(2) + " ₪</td>" +
            "</tr>"
        ).appendTo(detailsTable);
    });
    $("#tr-store"+ chosenStoreInStaticOrder).addClass("w3-blue");

};

function storeChosen(id, tr){
    $("#tr-store"+ chosenStoreInStaticOrder).removeClass("w3-blue");
    chosenStoreInStaticOrder = id;
    $(tr).addClass("w3-blue");
};

function initMakeOrderForm(){
    $("#orderform").submit(function () {

        var orderType = $("input[name=order-type]:checked").val();

        if((orderType === "static")&&(chosenStoreInStaticOrder === "")) {
            $("#error-screen-one-order-label").text("You must to choose a store you want to buy from...")
        } else {

            var parameters = $(this).serialize();

            $.ajax({
                data: parameters+"&chosenStore="+chosenStoreInStaticOrder,
                url: SCREEN_ONE_MAKE_ORDER,
                timeout: 7000,
                error: function (res) {
                    $("#error-screen-one-order-label").text(res.responseText);
                },
                success: function (items) {
                    changeToScTwoInMakeOrder();
                    showAvailableItemsForOrder(items, orderType);
                }
            });
        }

            return false;
    });
};

function changeToScTwoInMakeOrder(){
    $("#make-order").empty();
    $(
        "<div id=\"sub-cart\" class=\"w3-third w3-panel  w3-card\" style=\"visibility: hidden; display: none\">\n" +
        "            <h4 style=\"padding-left: 17px\">Shopping Cart</h4>\n" +
        "            <table id=\"sub-cart-table\" class=\"w3-table w3-striped w3-centered\">\n" +
        "            <thead>    <tr class=\"w3-light-grey\">\n" +
        "                    <th>Serial No.</th>\n" +
        "                    <th>Name</th>\n" +
        "                    <th>Amount</th>\n" +
        "                </tr></thead>" +
        "            </table><br>\n" +
        "            <p id='move-next-screen' class=\"w3-right\">\n" +
        "                <button onclick='getSales()' style=\"border:none ;background:transparent\">" +
        "                    <img src=\"../../imageAndIcon/forward-arrow.png\" alt=\"next\" style='cursor: pointer'>\n" +
        "                </button>\n" +
        "            </p>\n" +
        "</div>\n" +
        "<div id=\"order-items\" class=\"w3-rest w3-panel \">"+
        "</div>"
    ).appendTo($("#make-order"));
};

function getSales(){
    $.ajax({
        url: SALES_URL,
        success: function(sales) {
            if (sales.length === 0){
                getSummaryOrder();
            } else{
                showSales(sales);
                var nextScreen = $("#move-next-screen");
                nextScreen.empty();
                $(
                    "<button onclick='getSummaryOrder()' style=\"border:none ;background:transparent\">" +
                        "<img src=\"../../imageAndIcon/forward-arrow.png\" alt=\"next\" style='cursor: pointer'>" +
                    "</button>"
                ).appendTo(nextScreen);
            }
        },
        error: function (error){
            console.log(error.responseText);
        }
    });
};

function getSummaryOrder(){
    $.ajax({
        url: SHOW_SUMMARY_ORDER_URL,
        success: function(order) {
            showSummaryOrder(order);
        },
        error: function (error){
            console.log(error.responseText);
        }
    });
}

function showSummaryOrder(order) {
    var cartContainer = $("#order-items");
    cartContainer.empty();
    $("#sub-cart").empty();
    $(
        "<h4 style=\"padding-left: 17px\">Summary Order</h4>"+
        "<p>" +
        "<span>Order ID: " + order.orderID + "</span><br>" +
        "<span>Date: " + order.orderDate + "</span><br>" +
        "<span>Total Delivery cost: " + order.deliveryCost.toFixed(2) + " ₪</span><br>" +
        "<span>Total Order Price: " + order.totalOrderPrice.toFixed(2) + " ₪</span>" +
        "<span style=\"visibility: hidden\">enter</span>\n" +
        "</p>" +
        "<p class=\"w3-right\">\n" +
        "                <button class='executeOrderButton' type=\"submit\" style=\"border:none ;background:transparent\">\n" +
        "                    <img src=\"../../imageAndIcon/forward-arrow.png\" alt=\"next\" style='cursor: pointer'>" +
        "                </button>\n" +
        "            </p>"
    ).appendTo($("#sub-cart"));

    $( ".executeOrderButton:last" ).click(function() {
        executeOrder(order.shoppingCarts);
    });

    $.each(order.shoppingCarts || [], function (index, shoppingCart) {
        $(
            "<div class=\"w3-card-4 w3-rest\" >\n" +
            "            <header class=\"w3-container w3-pale-red w3-text-black\">\n" +
            "                <h5><b>Store Name: " + shoppingCart.storeName + "</b></h5>\n" +
            "                <span>Store ID: " + shoppingCart.storeID + " &nbsp;&nbsp; | &nbsp;&nbsp;</span>" +
            "                <span>PPK: " + shoppingCart.storePPK.toFixed(2) + " ₪ &nbsp;&nbsp; | &nbsp;&nbsp;</span>\n" +
            "                <span>Distance from customer: " + shoppingCart.distanceFromStore.toFixed(2) + " &nbsp;&nbsp; | &nbsp;&nbsp;</span>" +
            "                <span>Delivery cost to customer: " + shoppingCart.deliveryCost.toFixed(2) + " ₪</span><br>\n" +
            "                <span style=\"visibility: hidden\">enter</span>\n" +
            "            </header>" +
            "            <div id='summaryOrderFromStore-"+ index +"' class=\"w3-container\">" +
            "            </div>" +
            "            <span class=\"w3-button w3-block w3-dark-grey\" style='cursor: default'>Thank You</span>\n" +
            "</div><br>"
        ).appendTo(cartContainer);
        addOrdersItemsToContainer(shoppingCart.items, "summaryOrderFromStore-"+index);
    });

};

function executeOrder(shoppingCarts){
    $.ajax({
        url: EXECUTE_ORDER_URL,
        success: function() {
            console.log("Order Made!");
            suggestGiveFeedback(shoppingCarts);
        },
        error: function (error){
            console.log(error.responseText);
        }
    });
};

function suggestGiveFeedback(shoppingCarts){
    $("#sub-cart").empty();
    var feedbacksContainer = $("#order-items");
    feedbacksContainer.empty().append("<h2>Order FeedBack</h2>");
    $.each(shoppingCarts || [], function (index, cart) {
        $(
            "<form id=feedbackform" + cart.storeID + " class=\"w3-row add-feedback-form \" method=\"get\">" +
            "    <div class=\"w3-card-4 w3-round-xlarge w3-padding\" style=\"width:40%\">\n" +
            "        <header class=\"w3-container w3-light-grey\">\n" +
            "        <img src=\"../../imageAndIcon/feedback.png\"  alt=\"feedback\" class=\"w3-left  w3-margin-right\" style=\"width:60px\">\n" +
            "        <h3> " + cart.storeName +" </h3>\n" +
            "        </header>\n" +
            "        <div class=\"w3-container\">\n" +
            "            <h6>How Would You Rate This Store?</h6>\n" +
            "            <input type=\"text\" name=\"storeID\" value=" + cart.storeID + " style='display: none' />\n" +
            "            <p class=\"star-widget rate\">\n" +
            "                <input type=\"radio\" id=star5-form" + index + " name=\"rate\" value=\"5\" required/>\n" +
            "                <label title=\"rate\" for=star5-form" + index +  ">5 stars</label>\n" +
            "                <input type=\"radio\" id=star4-form" + index + " name=\"rate\" value=\"4\" required/>\n" +
            "                <label title=\"rate\" for=star4-form" + index +  ">4 stars</label>\n" +
            "                <input type=\"radio\" id=star3-form" + index + " name=\"rate\" value=\"3\" required/>\n" +
            "                <label title=\"rate\" for=star3-form" + index +  ">3 stars</label>\n" +
            "                <input type=\"radio\" id=star2-form" + index + " name=\"rate\" value=\"2\" required/>\n" +
            "                <label title=\"rate\" for=star2-form" + index +  ">2 stars</label>\n" +
            "                <input type=\"radio\" id=star1-form" + index + " name=\"rate\" value=\"1\" required/>\n" +
            "                <label title=\"rate\" for=star1-form" + index +  ">1 star</label>\n" +
            "            </p>\n" +
            "            <p class=\"textarea\">\n" +
            "                <textarea name='textRate' cols=\"30\" placeholder=\"Describe your experience..\" ></textarea>\n" +
            "            </p>\n" +
            "            <div class=\"btn\">\n" +
            "                <button class=\"w3-button w3-block w3-dark-grey\" type=\"submit\">Rate</button>\n" +
            "                <span id=error-add-feedback-s"+cart.storeID+ " class=\"w3-text-red \" style=\"font-size: 13px\"></span>" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</form>\n"+
            "<hr style=\"width:40%\">"
        ).appendTo(feedbacksContainer);
    });

    initAddFeedbackForms();
};

function initAddFeedbackForms(){
    $(".add-feedback-form").submit(function () {

        var parameters = $(this).serialize();
        var storeID = $(this).find("input[name=storeID]").val();
        var storeName = $(this).find("h3").text();
        var formID =  $(this).attr("id");

        $.ajax({
            data: parameters,
            url: ADD_FEEDBACK_TO_OWNER_STORE,
            timeout: 2000,
            error: function (res) {
                $("#error-add-feedback-s"+storeID).text(res.responseText);
            },
            success: function () {
                var responseContainer = $("#"+ formID);
                responseContainer.empty();
                $(
                    "<div class=\"w3-card-4 w3-round-xlarge w3-padding\" style=\"width:40%\">\n" +
                    "<header> " +
                    "<h6>Thank You!</h6>" +
                    "</header> " +
                    "<div><label> The feedback to store named " + storeName + ", id - " + storeID + " was sent successfully! </label></div>" +
                    "</div>"
                ).appendTo(responseContainer);
            }
        });

        return false;
    });
};

var currentOfferSales;

function showSales(sales) {
    var saleCard = $("#order-items");
    saleCard.empty();
    currentOfferSales = sales;

    $.each(sales || [], function (index, sale) {
        $(
        "    <div class=\"w3-card-4\" style=\"width:35%\">\n" +
        "        <header class=\"w3-container w3-light-grey\">\n" +
        "            <img src=\"../../imageAndIcon/sale.png\"  alt=\"sale\" class=\"w3-left  w3-margin-right\" style=\"width:60px\">\n" +
        "            <h3>" + sale.saleName + "</h3>\n" +
        "        </header>\n" +
        "        <div class=\"w3-container\">\n" +
        "            <p>" + sale.conditionSaleInString + "</p>\n" +
        "            <hr>" +
        "            <span>" + sale.youDeserveSentence + "</span><br>\n" +
        "            <ul id=listToExecute"+index+ ">" +

        "            </ul>" +
        "        </div>" +
        "        <button class=\"w3-button w3-block w3-dark-grey addSaleButton\">+ Select</button>\n" +
        "        <div class=\"w3-card-4\">\n" +
        "             <form class='w3-hide' action='javascript:;' id=saleOption"+index+ ">"+

        "             </form>" +
        "       </div>" +
        "  </div>\n" +
        "<hr style=\"width:35%\">"
        ).appendTo(saleCard);
        $( ".addSaleButton:last" ).click(function() {
            addSaleClicked(sale,"saleOption"+index);
        });
        appendSaleDetailsToList("listToExecute"+index, sale.saleDetailsInListString);
    });

};

function appendSaleDetailsToList(id, saleDetailsInListString){
    var idList = $("#"+id);
    $.each(saleDetailsInListString || [], function (index, details) {
        details = details.replace("?", "₪");
        $(
            "<li>" + details + "</li><br>"
        ).appendTo(idList);
    });
};

function addSaleClicked(sale, formSaleOptionId){
    var formSaleOptionId = $("#"+formSaleOptionId);
    formSaleOptionId.empty();
     if (sale.operator === "ONE-OF"){
         $(
             "<header class=\"w3-container w3-light-grey\">\n" +
             "    <h6> Select One Of:</h6>\n" +
             "</header>\n" +
             "<div class=\"w3-container\">"
         ).appendTo(formSaleOptionId);

         $.each(sale.saleDetailsInListString || [], function (index, details) {
             details = details.replace("?", "₪");
             $(
                "<input type=\"radio\" name=\"sale-option-serialnumber-item\" value=" + details.split(".")[1].split(" ")[1] + " required>" +
                "<label>" + details + "</label><br>"
             ).appendTo(formSaleOptionId);
         });

         $(
             "</div>" +
             "<button class='w3-round-large w3-tiny' type='submit'>add</button>"
         ).appendTo(formSaleOptionId);

         $( "button:last" ).click(function() {
             addSaleToCart(sale,formSaleOptionId);
             // return false;
         });

         formSaleOptionId.toggleClass("w3-hide").toggleClass("w3-show");
     }
     else {
         addSaleToCart(sale);
     }
};

function addSaleToCart (sale, formSaleOptionId){
    if(formSaleOptionId != null) {
        var serialNumberOfItemSelectedFromOneOfSaleType = (formSaleOptionId).find("input[name=sale-option-serialnumber-item]:checked").val();
    }
    if((formSaleOptionId == null) || (serialNumberOfItemSelectedFromOneOfSaleType != null)) {
        var SaleDTOandItemSR = {sale: sale, serialNumberItem: serialNumberOfItemSelectedFromOneOfSaleType};

        $.ajax({
            method: 'post',
            dataType: 'JSON',
            data: {data: JSON.stringify(SaleDTOandItemSR)},
            url: ADD_SALE_TO_CART,
            timeout: 2000,
            success: function (salesDetails) {
                $.each(salesDetails || [], function (index, details) {
                    addItemToSubCart("sub-cart", "sub-cart-table", details.itemName, details.itemSerialNumber, details.quantity);
                });
                refreshOfferSales(sale);
            },
            error: function (error) {
                console.log(error.responseText);
            }
        });
    }
};

function refreshOfferSales(selectedSale){
    var selectedSaleConditionSerialNumber = selectedSale.conditionSaleItemID;
    var leftToExploit = parseFloat(itemsInCartBeforeAddSales.get(""+selectedSaleConditionSerialNumber)) - parseFloat(selectedSale.conditionSaleAmount);
    var tempLeftSalesToExploit = [];

    $.each(currentOfferSales || [], function (index, sale) {
        if (selectedSaleConditionSerialNumber === sale.conditionSaleItemID){
            if (leftToExploit >= sale.conditionSaleAmount){
                itemsInCartBeforeAddSales.set(""+selectedSaleConditionSerialNumber, leftToExploit);
                tempLeftSalesToExploit.push(sale);
            }
        } else{
            tempLeftSalesToExploit.push(sale);
        }
    });

    if(tempLeftSalesToExploit.length === 0){
        doWhenAllSalesBeenUsed();
    } else {
        showSales(tempLeftSalesToExploit);
    }
};

function doWhenAllSalesBeenUsed(){
    var orderItems = $("#order-items");

    orderItems.empty();
    $(
        "<h4> You took advantage of all the sales you could carry out.</h4><br>" +
        "<h5> Please, click the Next button to proceed with the order.</h5>"
    ).appendTo(orderItems);

};

function showAvailableItemsForOrder(items, orderType){
    $("#sub-cart").show();
    var orderItems = $("#order-items");
    $.each(items || [], function (index, item) {
        $(
            "        <form class=\"w3-row add-item-to-cart-form\" method=\"get\">" +
            "<div class=\"w3-card-4\" style=\"width: 700px; height: 110px\">" +
            "    <div class=\"w3-col w3-left w3-margin-right w3-margin-left\" style=\"width:15%\">\n" +
            "        <img src=\"../../imageAndIcon/barcode.png\" style=\"max-height: 80px\" alt=\"barcode\">\n" +
            "    </div>" +
            "    <div class=\"w3-col w3-rest \" style=\"width:40%\">" +
            "        <span value=\""+item.itemName+"\" class=\"w3-opacity itemName\">" + item.itemName + "</span><br>" +
            "        <span value=\""+item.serialNumber+"\" class=\"w3-opacity serialNumber\">Serial No. " + item.serialNumber + "</span><br>" +
            "        <span class=\"w3-opacity serialNumber\">Purchase Category: " + item.purchaseCategory + "</span><br>" +
            "<span id=price"+index+" class=\"w3-text-blue\">-</span>"+
            "    </div>" +
            "    <div class=\"w3-col w3-right\" style=\"width:35%\">" +
            "            <label>Qty:</label>" +
            "            <input type=\"number\" name=\"amount\" step=\"0.100\" min=\"0.1\" max=\"50\" style=\"width:60px\" required>\n" +
            "            <button type=\"submit\" class=\"w3-button w3-circle w3-black w3-margin\">+</button><br>" +
            "            <span id=error-add-item-s"+item.serialNumber+ " style=\"font-size: 13px\"></span>" +
            "    </div>" +
            "</div>" +
            "        </form>" +
            "<hr style=\"max-width: 70%\">"
        ).appendTo(orderItems);
        if(orderType==="static"){
            $("#price"+index).text(item.price.toFixed(2)+" ₪");
        }
    });

    initAddItemToCartForm();
};

var itemsInCartBeforeAddSales = new Map();

function initAddItemToCartForm(){
    $(".add-item-to-cart-form").submit(function () {

        var parameters = $(this).serialize();
        var Amount = $(this).find("input[name=amount]").val();
        var chosenserialnumber= $(this).find(".serialNumber").attr('value');
        var chosenitemName= $(this).find(".itemName").attr('value');

            $.ajax({
                data: parameters +"&serialnumber="+chosenserialnumber,
                url: ADD_ITEM_TO_CART,
                timeout: 2000,
                error: function (res) {
                    $("#error-add-item-s"+chosenserialnumber).text(res.responseText).removeClass("w3-text-green").addClass("w3-text-red");
                },
                success: function () {
                    $("#error-add-item-s"+chosenserialnumber).text("item added successfully").removeClass("w3-text-red").addClass("w3-text-green");
                    addItemToSubCart("sub-cart", "sub-cart-table", chosenitemName, chosenserialnumber, Amount);
                    if (itemsInCartBeforeAddSales.get(chosenserialnumber) != null){
                        Amount = parseFloat(itemsInCartBeforeAddSales.get(chosenserialnumber)) + parseFloat(Amount);
                    }
                    itemsInCartBeforeAddSales.set(chosenserialnumber, Amount);
                }
            });

        return false;
    });
};

function addItemToSubCart(subCartID, subCartTableID, chosenitemName, chosenserialnumber, Amount){
    $("#"+subCartID).css('visibility','visible');
    $(
        "<tr>" +
        "<td>"+ chosenserialnumber +"</td>\n" +
        "<td>"+ chosenitemName +"</td>\n" +
        "<td>"+ Amount +"</td>\n" +
        "</tr>"
    ).appendTo($("#"+subCartTableID));

};

function addRelevantTab() {
    $.ajax({
        url: USER_TYPE_URL,
        success: function(userType) {
            if (userType === "Customer"){
                $(
                    "<a href=\"javascript:void(0)\" onclick=\"setMakeOrderTab(); openTab(event, 'make-order'); \">" +
                    "<div class='w3-quarter tablink w3-bottombar w3-hover-light-grey w3-padding' style='width:200px'>Make Order</div>" +
                    "</a>" +
                    "<a href=\"javascript:void(0)\" onclick=\"openTab(event, 'my-orders');\">" +
                    "<div class='w3-quarter tablink w3-bottombar w3-hover-light-grey w3-padding' style='width:200px'>My Orders</div>" +
                    "</a>"
                ).appendTo($("#tabs-row"));
            } else{
                $(
                    "<a href=\"javascript:void(0)\" onclick=\"openTab(event, 'order-details');\">" +
                    "<div class='w3-quarter tablink w3-bottombar w3-hover-light-grey w3-padding' style='width:200px'>Order Details</div>" +
                    "</a>" +
                    "<a href=\"javascript:void(0)\" onclick=\"openTab(event, 'feedback-details');\">" +
                    "<div class='w3-quarter tablink w3-bottombar w3-hover-light-grey w3-padding' style='width:200px'>Feedback Details</div>" +
                    "</a>" +
                    "<a href=\"javascript:void(0)\" onclick=\"setOpenNewStoreTab(); openTab(event, 'open-new-store');\">" +
                    "<div class='w3-quarter tablink w3-bottombar w3-hover-light-grey w3-padding' style='width:200px'>Open New Store</div>" +
                    "</a>" +
                    "<a href=\"javascript:void(0)\" onclick=\"initAddNewItemTab(); openTab(event, 'add-new-item');\">" +
                    "<div class='w3-quarter tablink w3-bottombar w3-hover-light-grey w3-padding' style='width:200px'>Add New Item</div>" +
                    "</a>"
                ).appendTo($("#tabs-row"));
            }
        }
    });
};

function initAddNewItemTab() {
        $.ajax({
            url: GET_STORES_TO_ADD_NEW_ITEM_TO_ZONE,
            timeout: 2000,
            error: function (res) {
                $("#add-new-item").empty();
                $(
                    "<div class=\"w3-card-4 w3-round-xlarge w3-padding\" style='height: 100vh'>\n" +
                    "        <div class=\"w3-center\" ><br>\n" +
                    "          <img src=\"../../imageAndIcon/no-access.png\" alt=\"no-access\" style=\"width:10%\" class=\"w3-circle w3-margin-top\"><br>\n" +
                    "          <label><b>No Access</b></label>\n" +
                    "        </div>\n" +
                    "        <div class=\"w3-section w3-center\">\n" +
                    "          <label>You are not the owner zone !</label><br>\n" +
                    "        </div>\n" +
                    "</div>"
                ).appendTo($("#add-new-item"));
            },
            success: function (stores) {
               setAddNewItemTab(stores);
            }
        });
};

function setAddNewItemTab(stores){
    $("#add-new-item").empty();
    $(
        "<form class=\"w3-container w3-panel w3-card w3-round-large w3-padding\" id='addNewItemForm'>\n" +
        "  <div class=\"w3-container w3-purple\">\n" +
        "    <h4>Fill the following details</h4>\n" +
        "  </div>\n" +
        "  <p>\n" +
        "  <label><b>Item Name</b></label>\n" +
        "  <input class=\"w3-input w3-animate-input\" type=\"text\" name='itemName' style=\"width:135px\" required></p>\n" +
        "  <p>\n" +
        "       <label><b>Purchase Category</b></label><br>\n" +
        "       <select class=\"w3-round w3-select\" name=\"purchaseCategory\" id=\"purchaseCategory\" style=\"width:135px\" required>\n" +
        "           <option value=\"Quantity\"> Quantity </option>"+
        "           <option value=\"Weight\"> Weight </option>"+
        "       </select>" +
        "  </p>\n" +
        "  <p id='myStores'>\n" +
        "  </p>\n" +
        "  <p class='w3-center'>" +
        "       <button id='addNewItem-btn' type=\"submit\" class=\"w3-button w3-white w3-border w3-round-large w3-center\" style=\"width:90px\" disabled>Add Item</button>\n" +
        "  </p>\n" +
        "</form>"
    ).appendTo($("#add-new-item"));
    appendMyStores(stores);
    initAddNewItemForm();
};

function appendMyStores(stores) {
    var storesContainer = $("#myStores");

    $.each(stores || [], function (index, store) {
        $(
            "<form id=store-form-"+ index + " class=\"w3-row selected-store-form\" method=\"get\">" +
            "<div class=\"w3-card-4\" style=\"width: 700px; height: 110px\">" +
            "    <div class=\"w3-col w3-left w3-margin-right w3-margin-left\" style=\"width:15%\">\n" +
            "        <img src=\"../../imageAndIcon/store.png\" style=\"max-height: 80px\" alt=\"store\">\n" +
            "    </div>" +
            "    <div class=\"w3-col w3-rest \" style=\"width:40%\">" +
            "        <span value=\""+store.storeName+"\" class=\"w3-opacity storeName\">" + store.storeName + "</span><br>" +
            "        <span value=\""+store.id+"\" class=\"w3-opacity storeID\">ID No. " + store.id + "</span><br>" +
            "    </div>" +
            "    <div class=\"w3-col w3-right\" style=\"width:35%\">" +
            "            <label>Price:</label>" +
            "            <input type=\"number\" name=\"price\" step=\"any\" min='0.1' style=\"width:95px\" required>\n" +
            "            <button type=\"submit\" class=\"w3-button w3-circle w3-black w3-margin\">+</button><br>" +
            "            <span id=error-add-item-to-store-s"+store.id+ " style=\"font-size: 13px\"></span>" +
            "    </div>" +
            "</div>" +
            "</form>" +
            "<hr style=\"max-width: 70%\">"
        ).appendTo(storesContainer);
    });

    initSelectedStoreForm();
};

var selcetedStoresToAddTheNewItem =[];

function initSelectedStoreForm() {

    $(".selected-store-form").submit(function () {

        var Price = $(this).find("input[name=price]").val();
        var storeID = $(this).find(".storeID").attr('value');
        var storeName = $(this).find(".storeName").attr('value');
        var formID = $(this).attr("id");

        // selcetedStoresToAddTheNewItem.set(storeID, Price);
        selcetedStoresToAddTheNewItem.push({id:storeID,price: Price});
        $("#addNewItem-btn").prop("disabled", false);
        var responseContainer = $("#" + formID);
        responseContainer.empty();
        $(
            "<div class=\"w3-card-4 w3-round-xlarge w3-padding\" style=\"width:60%\">\n" +
            "<header> " +
            "<h6 class='w3-wide'>Ok!</h6>" +
            "</header> " +
            "<div><label> This new item will been added to store named " + storeName + ", id - " + storeID + " !</label></div>" +
            "</div>"
        ).appendTo(responseContainer);

        return false;
    });
};


function initAddNewItemForm() {
    $("#addNewItemForm").submit(function () {

        var itemName = $(this).find("input[name=itemName]").val();
        var purchaseCategory = $("#purchaseCategory").val();

        var itemData = {itemName: itemName, purchaseCategory: purchaseCategory, selectedStoresIdAndPriceList: selcetedStoresToAddTheNewItem};

        $.ajax({
            method: 'post',
            data: {data: JSON.stringify(itemData)},
            url: EXECUTE_ADD_NEW_ITEM_TO_ZONE_URL,
            timeout: 7000,
            error: function (res) {

            },
            success: function () {
                $("#add-new-item").empty();
                $(
                    "<div class=\"w3-card-4 w3-round-xlarge w3-padding w3-center\" style=\"height: 70vh\">\n" +
                    "<header> " +
                    "<h6>Item Added Successfully</h6>" +
                    "</header> " +
                    "</div>"
                ).appendTo($("#add-new-item"));
            }
        });

        return false;
    });
};

function setOpenNewStoreTab(){
    $("#open-new-store").empty();
    $(
        "<form class=\"w3-container w3-panel w3-card w3-round-large w3-padding\" id='openNewStoreForm'>\n" +
        "<div class=\"w3-container w3-purple\">\n" +
        "  <h4>Fill the following details</h4>\n" +
        "</div>\n" +
        "\n" +
        "  <p>\n" +
        "  <label><b>Store Name</b></label>\n" +
        "  <input class=\"w3-input w3-animate-input\" type=\"text\" name='storeName' style=\"width:135px\" required></p>\n" +
        "  <p>\n" +
        "       <label><b>Location</b></label><br>\n" +
        "       <input class=\"w3-input\" id=\"xCoordinate\" name=\"xCoordinate\" type=\"number\" placeholder=\"X:\" step=\"1\" min=\"1\" max=\"50\" required style=\"width: 60px\">\n" +
        "       &nbsp;\n" +
        "       <input class=\"w3-input\" id=\"yCoordinate\" placeholder=\"Y:\" name=\"yCoordinate\" type=\"number\" step=\"1\" min=\"1\" max=\"50\" required style=\"width: 60px\">\n" +
        "  </p>\n" +
        "  <p>\n" +
        "       <label><b>PPK</b></label><br>\n" +
        "       <input class=\"w3-input\" id=\"ppk\" name=\"ppk\" type=\"number\" step=\"any\" min='0' required style=\"width:90px\" ></p><br>\n" +
        "  <p>" +
        "       <label id=\"error-screen-one-open-store-label\" class=\"w3-text-red\"></label>\n" +
        "  </p>" +
        "  <p class='w3-center'>" +
        "       <button type=\"submit\" class=\"w3-button w3-white w3-border w3-round-large\" style=\"width:90px; cursor: pointer\">Next</button>\n" +
        "  </p>\n" +
        "</form>"
    ).appendTo($("#open-new-store"));
    initOpenNewStoreForm();
};

function initOpenNewStoreForm() {
    $("#openNewStoreForm").submit(function () {


        var parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: SCREEN_ONE_OPEN_STORE,
            timeout: 7000,
            error: function (res) {
                $("#error-screen-one-open-store-label").text(res.responseText);
            },
            success: function (items) {
                chooseItemsToNewStore(items);
            }
        });

        return false;
    });
};

function chooseItemsToNewStore(items){
    var openStoreContainer = $("#open-new-store");
    openStoreContainer.empty();
    $(
        "<div id=\"sub-summary\" class=\"w3-third w3-container w3-panel w3-card\" style=\"visibility: hidden; display: none\">\n" +
        "            <h4 style=\"padding-left: 17px\">Items In Store</h4>\n" +
        "            <table id=\"sub-summary-table\" class=\"w3-table w3-striped w3-center\">\n" +
        "                <tr class=\"\">\n" +
        "                    <th>Serial No.</th>\n" +
        "                    <th>Name</th>\n" +
        "                    <th>Price</th>\n" +
        "                </tr>\n" +
        "            </table><br>\n" +
        "            <p class=\"w3-right\">\n" +
        "                <button onclick='addNewStore()' style=\"border:none ;background:transparent\">" +
        "                    <img src=\"../../imageAndIcon/forward-arrow.png\" alt=\"next\" style='cursor: pointer'>" +
        "                </button>" +
        "            </p>\n" +
        " </div>\n" +
        " <div id=\"newStore-items\" class=\"w3-rest w3-container w3-margin\">"+
        " </div>"
    ).appendTo(openStoreContainer);

    $("#sub-summary").show();
    var newStoreItems = $("#newStore-items");

    $.each(items || [], function (index, item) {
        $(
            "        <form class=\"w3-row add-item-to-store-form\" method=\"get\">" +
            "<div class=\"w3-card-4\" style=\"width: 700px; height: 110px\">" +
            "    <div class=\"w3-col w3-left w3-margin-right w3-margin-left\" style=\"width:15%\">\n" +
            "        <img src=\"../../imageAndIcon/barcode.png\" style=\"max-height: 80px\" alt=\"barcode\">\n" +
            "    </div>" +
            "    <div class=\"w3-col w3-rest \" style=\"width:40%\">" +
            "        <span value=\""+item.itemName+"\" class=\"w3-opacity itemName\">" + item.itemName + "</span><br>" +
            "        <span value=\""+item.serialNumber+"\" class=\"w3-opacity serialNumber\">Serial No. " + item.serialNumber + "</span><br>" +
            "        <span class=\"w3-opacity serialNumber\">Purchase Category: " + item.purchaseCategory + "</span><br>" +
            "        <span class=\"w3-text-blue\">Average Price: " + item.averagePrice.toFixed(2) + "</span>"+
            "    </div>" +
            "    <div class=\"w3-col w3-right\" style=\"width:35%\">" +
            "            <label>Price:</label>" +
            "            <input type=\"number\" name=\"price\" step=\"any\" min='0.1' style=\"width:95px\" required>\n" +
            "            <button type=\"submit\" class=\"w3-button w3-circle w3-black w3-margin\">+</button><br>" +
            "            <span id=error-add-item-to-store-s"+item.serialNumber+ " style=\"font-size: 13px\"></span>" +
            "    </div>" +
            "</div>" +
            "        </form>" +
            "<hr style=\"max-width: 70%\">"
        ).appendTo(newStoreItems);
    });

    initAddItemToStoreForm();
};

function initAddItemToStoreForm() {
    $(".add-item-to-store-form").submit(function () {

        var parameters = $(this).serialize();
        var Price = $(this).find("input[name=price]").val();
        var chosenserialnumber = $(this).find(".serialNumber").attr('value');
        var chosenitemName = $(this).find(".itemName").attr('value');

        $.ajax({
            data: parameters + "&serialnumber=" + chosenserialnumber,
            url: ADD_ITEM_TO_STORE,
            timeout: 2000,
            error: function (res) {
                $("#error-add-item-to-store-s" + chosenserialnumber).text(res.responseText).removeClass("w3-text-green").addClass("w3-text-red");
            },
            success: function () {
                $("#error-add-item-to-store-s" + chosenserialnumber).text("item added successfully").removeClass("w3-text-red").addClass("w3-text-green");
                addItemToSubCart("sub-summary", "sub-summary-table", chosenitemName, chosenserialnumber, Price);
            }
        });

        return false;
    });
};

function addNewStore(){
    $.ajax({
        url: EXECUTE_ADD_NEW_STORE_URL,
        success: function() {
            console.log("New Store Added!");
            // $("#sub-summary").empty();
            // $("#newStore-items").empty();
            $("#open-new-store").empty();
            $(
                "<div class=\"w3-card-4 w3-round-xlarge w3-padding w3-animate-zoom\" style='height: 100vh'>\n" +
                "<header class='w3-center'> " +
                "<h6>Store Added Successfully</h6>" +
                "</header> " +
                "</div>"
            ).appendTo($("#open-new-store"));
        },
        error: function (error){
            console.log(error.responseText);
        }
    });
};

function setMakeOrderTab(){
    chosenStoreInStaticOrder = "";
    $("#make-order").empty();
         $(
             "<form class=\"w3-panel w3-card-4 w3-light-grey\"  method=\"get\" action=\"makeOrderScOne\" id=\"orderform\">\n" +
             "\n" +
             "            <p>\n" +
             "                <label>Pick a date</label>\n" +
             "                <input style=\"max-width: 200px\" class=\"w3-input w3-border w3-round\" type=\"date\" id=\"date-to-order\" name=\"date-to-order\" min=\"2020-01-01\" required>\n" +
             "            </p>\n" +
             "            <p>\n" +
             "                <label>Location</label><br>\n" +
             "                <label>X:</label>\n" +
             "                <input  id=\"xCoordinate\" name=\"xCoordinate\" type=\"number\" step=\"1\" min=\"1\" max=\"50\" required>\n" +
             "                <label>Y:</label>\n" +
             "                <input id=\"yCoordinate\" name=\"yCoordinate\" type=\"number\" step=\"1\" min=\"1\" max=\"50\" required>\n" +
             "            </p>\n" +
             "            <p>\n" +
             "                <label>Order Type</label><br>\n" +
             "                    <input type=\"radio\" name=\"order-type\" value=\"static\" checked onclick=\"showStoresForStaticOrder()\">\n" +
             "                <label>Static Order</label>\n" +
             "                <input type=\"radio\" name=\"order-type\" value=\"dynamic\" onclick=\"hideStoresForStaticOrder()\">\n" +
             "                <label>Dynamic Order</label>\n" +
             "            </p>\n" +
             "            <p>\n" +
             "                <div id=\"stores-for-static-order\">\n" +
             "\n" +
             "                </div>\n" +
             "            </p>\n" +
             "            <p>\n" +
             "                <label id=\"error-screen-one-order-label\" class=\"w3-red\"></label>\n" +
             "            </p>\n" +
             "            <p class=\"w3-right\">\n" +
             "                <button  type=\"submit\" style=\"border:none ;background:transparent\">" +
             "                    <img src=\"../../imageAndIcon/forward-arrow.png\" alt=\"next\" style='cursor: pointer' >" +
             "                </button>\n" +
             "            </p>\n" +
             "        </form>"
         ).appendTo($("#make-order"));
         initMakeOrderForm();
};

function openTab(evt, tab) {
    var i, x, tablinks;
    x = $(".tab-option");
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }

    $(".tablink").removeClass("w3-border-light-blue").addClass("");
    document.getElementById(tab).style.display = "block";
    evt.currentTarget.firstElementChild.className += " w3-border-light-blue";
};

function ajaxCustomerHistoryOrders() {

        $.ajax({
            url: CUSTOMER_HISTORY_ORDER_DETAILS_URL,
            timeout: 2000,
            error: function () {
                console.error("Failed to receive Orders");
                setTimeout(ajaxCustomerHistoryOrders, 9000);

            },
            success: function (orders) {
                var orderDetailsTable = $("#customer-order-details-table");

                orderDetailsTable.empty();

                if (orders !== null) {
                    appendOrdersToCustomerDetailsTable(orders);
                } else{
                    $(
                        "<h3 style='height: 100vh'>No Order Made!</h3>"
                    ).appendTo(orderDetailsTable);

                }
                setTimeout(ajaxCustomerHistoryOrders, 9000);
            }
        });
};

function appendOrdersToCustomerDetailsTable(orders){
    $(
        "<table id=\"customer-details-table\" class=\"w3-margin w3-table w3-bordered w3-striped w3-centered\" style=\"max-width: 72%\"> <!---->" +
        "<tr class=\"w3-dark-grey\">\n" +
        "<th>No. Order</th>" +
        "<th style='min-width: 120px'>Date</th>" +
        "<th>Order Destination</th>" +
        "<th>No. Stores Purchased From</th>" +
        "<th>No. Items Purchased</th>" +
        "<th>Items Purchased Cost</th>" +
        "<th>Delivery Cost</th>" +
        "<th style='min-width: 80px'>Order Cost</th>" +
        "</tr>" +
        "</table>"
    ).appendTo($("#customer-order-details-table"));

    var detailsTable = $("#customer-details-table");
    $.each(orders || [], function (index, order) {
        $(
            "<tr class=\"w3-hover-blue\">" +
            "<td>" + order.orderID + "</td>" +
            "<td>" + order.orderDate + "</td>" +
            "<td> (" + order.destinationXCoordinate + "," + order.destinationYCoordinate + ")</td>" +
            "<td>" + order.totalStoresPurchasedFrom + "</td>" +
            "<td>" + order.totalItemsAmount + "</td>" +
            "<td>" + order.totalItemsPrice.toFixed(2) + " ₪</td>" +
            "<td>" + order.deliveryCost.toFixed(2) + " ₪</td>" +
            "<td>" + order.totalOrderPrice.toFixed(2) + " ₪</td>" +
            "</tr>"
        ).appendTo(detailsTable);
        $( "#customer-details-table tr:last" ).click(function() {
            showCustomerOrderItems(order.shoppingCarts, order.orderID);
        });
    });
};

function showCustomerOrderItems(shoppingCarts, orderID){
    var itemsDetailsTable = $("#items-customer-order-details");

    itemsDetailsTable.empty();

    $(
        "<br><h5>Items Details For Order No. " + orderID + ":</h5><br>" +
        "<div style='display: flex; justify-content: center'>" +
            "<table id=\"items-table\" class=\"w3-table w3-card w3-striped w3-centered\" style=\"max-width: 800px; \">" +
            "<tr class=\"w3-pale-blue\">\n" +
            "<th>Serial No.</th>" +
            "<th>Name</th>" +
            "<th>Purchase Category</th>" +
            "<th>Store</th>" +
            "<th>Quantity</th>" +
            "<th>Price (Per Unit)</th>" +
            "<th>Total Price</th>" +
            "<th>From Sale</th>" +
            "</tr>" +
            "</table>" +
        "</div>"
    ).appendTo(itemsDetailsTable);

    var detailsTable = $("#items-table");
    $.each(shoppingCarts || [], function (index, cart) {
        $.each(cart.items || [], function (index1, item) {
            $(
                "<tr>" +
                "<td>" + item.serialNumber + "</td>" +
                "<td>" + item.itemName + "</td>" +
                "<td>" + item.purchaseCategory + "</td>" +
                "<td>" + cart.storeName + " - ID:" + cart.storeID + "</td>" +
                "<td>" + item.amount.toFixed(2) + "</td>" +
                "<td>" + item.price.toFixed(2) + " ₪</td>" +
                "<td>" + item.TotalPrice.toFixed(2) + " ₪</td>" +
                "<td id=" + index + "-index-isFromSale-" + index1 + " ></td>" +
                "</tr>"
            ).appendTo(detailsTable);
            item.IsFromSale? $("#" + index + "-index-isFromSale-" + index1).text(String.fromCharCode(10003)) : $("#" + index + "-index-isFromSale-" + index1).text(String.fromCharCode(10007));
        });
    });
};
