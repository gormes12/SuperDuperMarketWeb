package dto;

import javafx.util.Pair;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class OrderDTO {
    private int orderID;
    private List<ShoppingCartDTO> shoppingCarts;
    private double deliveryCost;
    private LocalDate orderDate;
    private double totalOrderPrice;

    public OrderDTO(int orderID, LocalDate orderDate, List<ShoppingCartDTO> shoppingCart, double deliveryCost) {
        this.orderID = orderID;
        this.shoppingCarts = shoppingCart;
        this.deliveryCost = deliveryCost;
        this.orderDate = orderDate;
        totalOrderPrice = getOrderCost();
    }

    public List<ShoppingCartDTO> getShoppingCarts() {
        return shoppingCarts;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getTotalItemTypes() {
        int sum = 0;

        for (ShoppingCartDTO cart : shoppingCarts) {
            sum += cart.getTotalItemTypes();
        }

        return sum;
    }

    public int getTotalItemsAmount() {
        int sum = 0;

        for (ShoppingCartDTO cart : shoppingCarts) {
            sum += cart.getTotalItemsAmount();
        }

        return sum;
    }

    public double getTotalItemsPrice() {
        double sum = 0;

        for (ShoppingCartDTO cart : shoppingCarts) {
            sum += cart.getTotalItemsPrice();
        }

        return sum;
    }

    public int getOrderID() {
        return orderID;
    }

    public double getOrderCost() {
        return getTotalItemsPrice()+deliveryCost;
    }

    public String toString() {
        String storesDetails = "";
        if (shoppingCarts.size() >1) {
            int counter = 1;
            for (ShoppingCartDTO cart : shoppingCarts) {
                storesDetails = storesDetails.concat(
                        MessageFormat.format("{1}#. Store ID: {2} ; Store Name: {3}{0}", System.lineSeparator(), counter++, cart.getStoreID(), cart.getStoreName()));
            }
        }else{
            storesDetails = storesDetails.concat(
                    MessageFormat.format("Store ID: {1} ; Store Name: {2}{0}", System.lineSeparator(), shoppingCarts.get(0).getStoreID(), shoppingCarts.get(0).getStoreName()));
        }
        return MessageFormat.format("Order Serial Number: {1}{0}Date: {2}{0}Order made from:{0}{3}" +
                        "Total item types: {4}{0}Total item amount: {5}{0}" +
                        "Total items price: {6}{0}Delivery cost: {7}{0}Order cost: {8}{0}",
                System.lineSeparator(),
                orderID,
                orderDate,
                storesDetails,
                getTotalItemTypes(),
                getTotalItemsAmount(),
                String.format("%.2f", getTotalItemsPrice()),
                String.format("%.2f", deliveryCost),
                String.format("%.2f", getOrderCost()));
    }
}
