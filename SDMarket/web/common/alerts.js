var GET_ORDER_ALERT = buildUrlWithContextPath("getOrderAlert");
var GET_FEEDBACK_ALERT = buildUrlWithContextPath("getFeedbackAlert");
var GET_NEW_STORE_ALERT = buildUrlWithContextPath("getNewStoreAlert");


$(function() {
    ajaxOrderAlertContent();
    ajaxFeedbackAlertContent();
    ajaxNewStoreAlertContent();
});

function ajaxOrderAlertContent() {
    $.ajax({
        url: GET_ORDER_ALERT,
        // data: "orderAlertVersion=" + orderAlertVersion,
        dataType: 'json',
        success: function(data) {

            // if (data.version !== orderAlertVersion) {
                if (data !== null) {
                // orderAlertVersion = data.version;
                popUpOrderAlert(data);
            }
            setTimeout(ajaxOrderAlertContent, 1000);

        },
        error: function(error) {
            setTimeout(ajaxOrderAlertContent, 1000);
        }
    });
};

function popUpOrderAlert(orders) {
    $.each(orders || [], function (index, order) {
        $(
            "<div id=orderAlert-" + order.orderID + index + " class=\"w3-modal\" style=\"display: block\">\n" +
            "    <div class=\"w3-modal-content w3-card-4 w3-animate-zoom w3-padding\" style=\"width: 400px; height: 250px\">\n" +
            "        <div class=\"w3-center\"><br>\n" +
            "          <span class=\"w3-button w3-xlarge w3-hover-red w3-display-topright\" title=\"Close\">&times;</span>\n" +
            "          <img src=\"../../imageAndIcon/order-alert.png\" alt=\"order-alert\" style=\"width:10%\" class=\"w3-circle w3-margin-top\"><br>\n" +
            "          <label><b>Order Alert</b></label>\n" +
            "        </div>\n" +
            "        <div class=\"w3-section\">\n" +
            "          <label><b>Order No. " + order.orderID + "</b></label><br>\n" +
            "          <label><b>Customer Name: </b>" + order.customerName + "</label><br>\n" +
            "          <label><b>Total Items Types: </b>" + order.totalItemsType + "</label><br>\n" +
            "          <label><b>Items Cost: </b>" + order.TotalItemsPrice.toFixed(2) + "</label><br>\n" +
            "          <label><b>Delivery Cost: </b>" + order.deliveryCost.toFixed(2) + "</label><br>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>"
        ).appendTo($(document.body));
        $( "span:last" ).click(function() {
            removeDivById("orderAlert-" + order.orderID + index);
        });
    });
};

function ajaxFeedbackAlertContent() {
    $.ajax({
        url: GET_FEEDBACK_ALERT,
        dataType: 'json',
        success: function (data) {
            if (data !== null) {
                popUpFeedbackAlert(data);
            }

            setTimeout(ajaxFeedbackAlertContent, 1000);
        },
        error: function (error) {
            setTimeout(ajaxFeedbackAlertContent, 1000);
        }
    });
};

function popUpFeedbackAlert(feedbacks) {
    $.each(feedbacks || [], function (index, feedback) {
        $(
            "<div id=feedbackAlert-" + index + " class=\"w3-modal\" style=\"display: block\">\n" +
            "    <div class=\"w3-modal-content w3-card-4 w3-animate-zoom w3-padding\" style=\"width: 400px; height: 250px\">\n" +
            "        <div class=\"w3-center\"><br>\n" +
            "          <span class=\"w3-button w3-xlarge w3-hover-red w3-display-topright\" title=\"Close\">&times;</span>\n" +
            "          <img src=\"../../imageAndIcon/feedback-alert.png\" alt=\"feedback-alert\" style=\"width:10%\" class=\"w3-circle w3-margin-top\"><br>\n" +
            "          <label><b>Feedback Alert</b></label>\n" +
            "        </div>\n" +
            "        <div class=\"w3-section w3-center\">\n" +
            "          <label>You got new feedback from <b>" + feedback.userGiverFeedback + "</b></label><br>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>"
        ).appendTo($(document.body));
        $( "span:last" ).click(function() {
            removeDivById("feedbackAlert-" + index);
        });
    });
};

function ajaxNewStoreAlertContent() {
    $.ajax({
        url: GET_NEW_STORE_ALERT,
        dataType: 'json',
        success: function(data) {

            if (data !== null) {
                popUpNewStoreAlert(data);
            }
            setTimeout(ajaxNewStoreAlertContent, 1000);

        },
        error: function(error) {
            setTimeout(ajaxNewStoreAlertContent, 1000);
        }
    });
};

function popUpNewStoreAlert(stores) {
    $.each(stores || [], function (index, store) {
        $(
            "<div id=newStoreAlert-" + store.id + index + " class=\"w3-modal\" style=\"display: block\">\n" +
            "    <div class=\"w3-modal-content w3-card-4 w3-animate-zoom w3-padding\" style=\"width: 400px; height: 250px\">\n" +
            "        <div class=\"w3-center\"><br>\n" +
            "          <span class=\"w3-button w3-xlarge w3-hover-red w3-display-topright\" title=\"Close\">&times;</span>\n" +
            "          <img src=\"../../imageAndIcon/newStore-alert.png\" alt=\"new-store-alert\" style=\"width:10%\" class=\"w3-circle w3-margin-top\"><br>\n" +
            "          <label><b>New Store Alert</b></label>\n" +
            "        </div>\n" +
            "        <div class=\"w3-section\">\n" +
            "          <label><b>Store No. " + store.id + "</b></label><br>\n" +
            "          <label><b>Store Name: </b>" + store.storeName + "</label><br>\n" +
            "          <label><b>Location: </b>(" + store.xCoordinate + "," + store.yCoordinate + ")</label><br>\n" +
            "          <label><b>Items Amount </b>" + store.totalSuperMarketItemsAmount + "</label><br>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>"
        ).appendTo($(document.body));
        $( "span:last" ).click(function() {
            removeDivById("newStoreAlert-" + store.id + index);
        });
    });
};

function removeDivById(divId){
    $("#" + divId).remove();
};