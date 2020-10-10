var transactionVersion = 0;
var refreshRate = 2000; //milli seconds
var TRANSACTION_LIST_URL = buildUrlWithContextPath("transactions");
var USER_TYPE_URL = buildUrlWithContextPath("usertype");
var DEPOSIT_MONEY_URL = buildUrlWithContextPath("deposit");


$(function() {
    addDepositedTab();
    triggerAjaxTransactionContent();
    initDepositMoneyForm();
});

function triggerAjaxTransactionContent() {
    setTimeout(ajaxTransactionContent, refreshRate);
}

function ajaxTransactionContent() {
    $.ajax({
        url: TRANSACTION_LIST_URL,
        data: "transactionversion=" + transactionVersion,
        dataType: 'json',
        success: function(data) {
            /*
             data will arrive in the next form:
             {
                "entries": [
                    {
                        "type": "Deposited",
                        "date": "2020-10-16",
                        "amount": 100.0,
                        "creditBalanceBefore": 0.0,
                        "creditBalanceAfter": 100.0
                    },
                    {
                        "type": "Deposited",
                        "date": "2020-10-24",
                        "amount": 566.0,
                        "creditBalanceBefore": 0.0,
                        "creditBalanceAfter": 566.0
                    }
                ],
                "version":2
             }
             */
            console.log("Server Transaction version: " + data.version + ", Current Transaction version: " + transactionVersion);
            if (data.version !== transactionVersion) {
                if (data.version !== 0){
                    // document.getElementById("no-transactions").style.display = "block";
                    $("#no-transactions").hide();
                    // $("#table").attr('style', 'display:block;');
                    $("#table").show();
                } else{
                }
                transactionVersion = data.version;
                appendToTransactionInfo(data.entries);
            }
            triggerAjaxTransactionContent();
        },
        error: function(error) {
            triggerAjaxTransactionContent();
        }
    });
}

function appendToTransactionInfo(entries) {
    var tableTransactionsInfo = $("#table-transactions");

    // add the relevant entries
    $.each(entries || [], function (index, entry) {
        $(
          "<tr>" +
             "<td>" + entry.type + "</td>" +
             "<td>" + entry.date + "</td>" +
             "<td>" + entry.amount + "</td>" +
             "<td>" + entry.creditBalanceBefore + "</td>" +
             "<td>" + entry.creditBalanceAfter + "</td>" +
          "</tr>"
         ).appendTo(tableTransactionsInfo);
    });
}

function addDepositedTab() {
    $.ajax({
        url: USER_TYPE_URL,
        success: function(userType) {
            if (userType === "Customer"){
                $("<a href=\"javascript:void(0)\" onclick=\"openTab(event, 'deposited');\">" +
                    "<div class='w3-quarter tablink w3-bottombar w3-hover-light-grey w3-padding' style='width:200px'>Deposit Money</div>" +
                "</a>").appendTo($("#tabs-row"));
            }
        }
    });
}

function initDepositMoneyForm() {
    $("#depositform").submit(function () {
        var parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: DEPOSIT_MONEY_URL,
            timeout: 2000,
            error: function () {
                console.error("Failed to submit");
            },
            success: function (r) {
                //do not add the user string to the chat area
                //since it's going to be retrieved from the server
                //$("#result h1").text(r);
            }
        });

        //$("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
};
