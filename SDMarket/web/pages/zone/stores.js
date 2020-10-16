// var itemsVersion = 0;
var refreshRate = 2000; //milli seconds
var chosenStoreInStaticOrder="";
// var currentItem = 1;
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


$(function() {
    addRelevantTab();
    ajaxItemsContent();
    ajaxStoresContent();
    // triggerAjaxItemsContent();
    // setInterval(ajaxStoresContent, refreshRate);
    initShowOrderDetailsForm();
    initMakeOrderForm();
});

function triggerAjaxItemsContent() {
    setTimeout(ajaxItemsContent, refreshRate);
};

function triggerAjaxStoresContent() {
    setTimeout(ajaxStoresContent, refreshRate);
};

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

        //next sentence only for comboBox
        $(
            "<option value=\"" + store.storeName +"\">" + store.storeName + "</option>"
        ).appendTo(storesComboBox);


        $(
            "<div class=\"w3-panel w3-deep-orange w3-round-xlarge \">" +
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
                    "<table id=\""+store.id+"\" class=\"w3-margin w3-table w3-bordered w3-striped w3-centered\" style=\"max-width: 800px\">" +
                        "<tr class=\"w3-light-grey\">\n" +
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
};

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
            "<tr onclick='addOrdersItemsToContainer(order.shoppingCart, items-order-details)'>" +
                "<td>" + order.orderID + "</td>" +
                "<td>" + order.orderDate + "</td>" +
                "<td>" + order.customerName + "</td>" +
                "<td> (" + order.customerXCoordinate + "," + order.customerYCoordinate + ")</td>" +
                "<td>" + order.TotalItemAmount + "</td>" +
                "<td>" + order.TotalItemsPrice.toFixed(2) + " ₪</td>" +
                "<td>" + order.deliveryCost.toFixed(2) + " ₪</td>" +
            "</tr>"
        ).appendTo(detailsTable);
    });
};

function addOrdersItemsToContainer(items, containerID){
    var itemsDetailsTable = $("#"+containerID);

    itemsDetailsTable.empty();

    $(
        "<table id=\"items-table-"+ containerID +"\" class=\"w3-table w3-striped w3-centered\" style=\"max-width: 800px\">" +
        "<tr class=\"w3-light-grey\">\n" +
        "<th>Serial No.</th>" +
        "<th>Name</th>" +
        "<th>Purchase Category</th>" +
        "<th>Quantity</th>" +
        "<th>Price (Per Unit)</th>" +
        "<th>Total Price</th>" +
        "<th>From Sale</th>" +
        "</tr>" +
        "</table>"
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
            "<p> Total revenues of items sold: " + store.totalItemsSoldRevenues.toFixed(2) + " ₪</p>" +
            "<p> PPK: " + store.pricePerKilometer.toFixed(2) + " ₪</p>" +
            "<p> Total Deliveries Revenues: " + store.totalDeliveriesRevenues.toFixed(2) + " ₪</p>" +
            "</div>"
        ).appendTo(feedbackInfo);

    });
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
            "<tr onclick='storeChosen("+store.id+")'>" +
            "<td>" + store.id + "</td>" +
            "<td>" + store.storeName + "</td>" +
            "<td> (" + store.xCoordinate + "," + store.yCoordinate + ")</td>" +
            "<td>" + store.pricePerKilometer.toFixed(2) + " ₪</td>" +
            "</tr>"
        ).appendTo(detailsTable);
    });

};

function storeChosen(id){
    chosenStoreInStaticOrder = id;
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

            // by default - we'll always return false so it doesn't redirect the user.
            return false;
    });
};

function changeToScTwoInMakeOrder(){
    $("#make-order").empty();
    $(
        "<div id=\"sub-cart\" class=\"w3-third w3-container w3-card\" style=\"visibility: hidden; display: none\">\n" +
        "            <h4 style=\"padding-left: 17px\">Shopping Cart</h4>\n" +
        "            <table id=\"sub-cart-table\" class=\"w3-table w3-striped w3-center\">\n" +
        "                <tr class=\"\">\n" +
        "                    <th>Serial No.</th>\n" +
        "                    <th>Name</th>\n" +
        "                    <th>Amount</th>\n" +
        "                </tr>\n" +
        "            </table><br>\n" +
        "            <p id='move-next-screen' class=\"w3-cell-bottomright\">\n" +
        "                <button onclick='getSales()' style=\"border:none ;background:transparent\"> <i class=\"material-icons w3-xxlarge \">arrow_forward</i></button>\n" +
        "            </p>\n" +
        "        </div>\n" +
        "        <div id=\"order-items\" class=\"w3-rest w3-container w3-margin\">"+
        " </div>"
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
                    "<button onclick='getSummaryOrder()' style=\"border:none ;background:transparent\"> <i class=\"material-icons w3-xxlarge \">arrow_forward</i></button>"
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
    $("#order-items").empty();
    $("#sub-cart").empty();/*.width('28%')*/
    $(
        "<h4 style=\"padding-left: 17px\">Summary Order</h4>"+
        "<p>" +
        "<span>Order ID: " + order.orderID + "</span><br>" +
        "<span>Date: " + order.orderDate + "</span><br>" +
        "<span>Total Delivery cost: " + order.deliveryCost.toFixed(2) + " ₪</span><br>" +
        "<span>Total Order Price: " + order.totalOrderPrice.toFixed(2) + " ₪</span>" +
        "<span style=\"visibility: hidden\">enter</span>\n" +
        "</p>" +
        "<p class=\"w3-cell-bottommiddle\">\n" +
        "                <button type=\"submit\" onclick='executeOrder()' style=\"border:none ;background:transparent\">\n" +
        "                    <i class=\"material-icons w3-xxlarge \">arrow_forward</i>\n" +
        "                </button>\n" +
        "            </p>"
    ).appendTo($("#sub-cart"));

    $.each(order.shoppingCarts || [], function (index, shoppingCart) {
        $(
            "<div class=\"w3-card-4\" style=\"width: 71%\">\n" +
            "            <header class=\"w3-container w3-deep-orange\">\n" +
            "                <h5><b>Store Name: " + shoppingCart.storeName + "</b></h5>\n" +
            "                <span>Store ID: " + shoppingCart.storeID + " &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;</span>" +
            "                <span>PPK: " + shoppingCart.storePPK.toFixed(2) + " ₪ &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;</span>\n" +
            "                <span>Distance from customer: " + shoppingCart.distanceFromStore.toFixed(2) + " &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;</span>" +
            "                <span>Delivery cost to customer: " + shoppingCart.deliveryCost.toFixed(2) + "</span><br>\n" +
            "                <span style=\"visibility: hidden\">enter</span>\n" +
            "            </header>" +
            "            <div id='summaryOrderFromStore-"+ index +"' class=\"w3-container\">" +
            "            </div>" +
            "            <span class=\"w3-button w3-block w3-dark-grey\">Thank You</span>\n" +
            "</div><br>"
        ).appendTo($("#order-items"));
        addOrdersItemsToContainer(shoppingCart.items, "summaryOrderFromStore-"+index);
    });

};

function executeOrder(){
    $.ajax({
        url: EXECUTE_ORDER_URL,
        success: function() {
            console.log("Success!!!!!!!!!!!!!");
        },
        error: function (error){
            console.log(error.responseText);
        }
    });
};

var currentOfferSales;

function showSales(sales) {
    var saleCard = $("#order-items");
    saleCard.empty();
    currentOfferSales = sales;

    $.each(sales || [], function (index, sale) {
        $(
        // "<form class=\"w3-row add-item-to-cart-form\" method=\"get\">\n" +
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
        // "</form>\n" +
        "<hr style=\"width:35%\">"
        ).appendTo(saleCard);
        $( ".addSaleButton:last" ).click(function() {
            addSaleClicked(sale,"saleOption"+index);
        });
        appendSaleDetailsToList("listToExecute"+index, sale.saleDetailsInListString);
    });

    // initAddItemToCartForm();
};

/*function initAddItemToCartForm(){
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
                $("#error-add-item-s"+chosenserialnumber).text(res.responseText);
            },
            success: function () {
                $("#error-add-item-s"+chosenserialnumber).text("");
                addItemToSubCart(chosenitemName, chosenserialnumber, Amount);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
};*/

function appendSaleDetailsToList(id, saleDetailsInListString){
    var idList = $("#"+id);
    $.each(saleDetailsInListString || [], function (index, details) {
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
     }else{
         addSaleToCart(sale);
     }
};

function addSaleToCart (sale, formSaleOptionId){
    var serialNumberOfItemSelectedFromOneOfSaleType = (formSaleOptionId). find("input[name=sale-option-serialnumber-item]:checked").val();
    if(serialNumberOfItemSelectedFromOneOfSaleType != null) {
        var SaleDTOandItemSR = {sale: sale, serialNumberItem: serialNumberOfItemSelectedFromOneOfSaleType};

        $.ajax({
            method: 'post',
            dataType: 'JSON',
            data: {data: JSON.stringify(SaleDTOandItemSR)},
            url: ADD_SALE_TO_CART,
            timeout: 2000,
            success: function (salesDetails) {
                $.each(salesDetails || [], function (index, details) {
                    addItemToSubCart(details.itemName, details.itemSerialNumber, details.quantity);
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
            "<div class=\"w3-card-4\" style=\"width: 70%; height: 90px\">" +
            "    <div class=\"w3-col w3-left w3-margin-right w3-margin-left\" style=\"width:15%\">\n" +
            "        <img src=\"../../imageAndIcon/barcode.png\" style=\"max-height: 80px\" alt=\"barcode\">\n" +
            "    </div>" +
            "    <div class=\"w3-col w3-rest \" style=\"width:30%\">" +
            "        <span value=\""+item.itemName+"\" class=\"w3-opacity itemName\">" + item.itemName + "</span><br>" +
            "        <span value=\""+item.serialNumber+"\" class=\"w3-opacity serialNumber\">Serial No. " + item.serialNumber + "</span><br>" +
            "<span id=price"+index+" class=\"w3-text-blue\">-</span>"+
            "    </div>" +
            "    <div class=\"w3-col w3-right\" style=\"width:35%\">" +
            "            <label>Qty:</label>" +
            "            <input type=\"number\" name=\"amount\" step=\"0.100\" min=\"0.1\" max=\"50\" style=\"width:60px\" required>\n" +
            "            <button type=\"submit\" class=\"w3-button w3-circle w3-black w3-margin\">+</button><br>" +
            "            <span id=error-add-item-s"+item.serialNumber+ " class=\"w3-text-red \" style=\"font-size: 13px\"></span>" +
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
                    $("#error-add-item-s"+chosenserialnumber).text(res.responseText);
                },
                success: function () {
                    $("#error-add-item-s"+chosenserialnumber).text("");
                    addItemToSubCart(chosenitemName, chosenserialnumber, Amount);
                    if (itemsInCartBeforeAddSales.get(chosenserialnumber) != null){
                        Amount = parseFloat(itemsInCartBeforeAddSales.get(chosenserialnumber)) + parseFloat(Amount);
                    }
                    itemsInCartBeforeAddSales.set(chosenserialnumber, Amount);
                }
            });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });


};

function addItemToSubCart(chosenitemName, chosenserialnumber, Amount){
    $("#sub-cart").css('visibility','visible');
    $(
        "<tr>" +
        "<td>"+ chosenserialnumber +"</td>\n" +
        "<td>"+ chosenitemName +"</td>\n" +
        "<td>"+ Amount +"</td>\n" +
        "</tr>"
    ).appendTo($("#sub-cart-table"));

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
                    "<a href=\"javascript:void(0)\" onclick=\"openTab(event, 'open-new-store');\">" +
                    "<div class='w3-quarter tablink w3-bottombar w3-hover-light-grey w3-padding' style='width:200px'>Open New Store</div>" +
                    "</a>"
                ).appendTo($("#tabs-row"));
            }
        }
    });
};

function setMakeOrderTab(){
    chosenStoreInStaticOrder = "";
    $("#make-order").empty();
         $(
             "<form class=\"w3-container w3-card-4 w3-light-grey\"  method=\"get\" action=\"makeOrderScOne\" id=\"orderform\">\n" +
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
             "            <p class=\"w3-cell-bottommiddle\">\n" +
             "                <button  type=\"submit\" style=\"border:none ;background:transparent\"> <i class=\"material-icons w3-xxlarge \">arrow_forward</i></button>\n" +
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

        $(".tablink").removeClass("w3-border-red").addClass("");
        document.getElementById(tab).style.display = "block";
        evt.currentTarget.firstElementChild.className += " w3-border-red";
    };
/*function openStoreAccordion(id) {
    var x = $("#" + id);
    x.toggleClass("w3-show");
}*/
