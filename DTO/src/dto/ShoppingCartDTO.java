package dto;

import java.awt.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

public class ShoppingCartDTO {
    private final int orderID;
    private List<ShoppingCartItemDTO> shoppingCart;
    private double deliveryCost;
    private double distanceFromStore;
    private LocalDate orderDate;
    private StoreDTO storeDetails;

    public ShoppingCartDTO(int orderID, LocalDate orderDate, List<ShoppingCartItemDTO> shoppingCart, double deliveryCost,
                           double distanceFromStore, StoreDTO storeDetails) {
        this.orderID = orderID;
        this.shoppingCart = shoppingCart;
        this.deliveryCost = deliveryCost;
        this.distanceFromStore = distanceFromStore;
        this.orderDate = orderDate;
        this.storeDetails = storeDetails;
    }

    public String getStoreName(){
        return storeDetails.getStoreName();
    }

    public int getStoreID(){
        return storeDetails.getId();
    }

    public List<ShoppingCartItemDTO> getShoppingCart() {
        return shoppingCart;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public double getPricePerKilometer() {
        return storeDetails.getPricePerKilometer();
    }

    public double getDistanceFromStore() {
        return distanceFromStore;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getTotalItemTypes(){
        return shoppingCart.size();
    }

    public Point getLocation(){
        return storeDetails.getLocation();
    }

    public int getTotalItemsAmount() {
        int sum = 0;

        for (ShoppingCartItemDTO item : shoppingCart) {
            if (item.getPurchaseCategory().equals("Quantity")) {
                sum += item.getAmount();
            } else {
                sum += 1;
            }
        }

        return sum;
    }

    public double getTotalItemsPrice() {
        double sum = 0;

        for (ShoppingCartItemDTO item : shoppingCart) {
            sum += item.getTotalPrice();
        }

        return sum;
    }

    public int getOrderID() {
        return orderID;
    }

    public StoreDTO getStoreDetails() {
        return storeDetails;
    }

    public double getOrderCost() {
        return getTotalItemsPrice()+deliveryCost;
    }

    public String toString() {
        return MessageFormat.format("Serial Number: {1}{0}Date: {2}{0}Store ID: {3}{0}" +
                        "Store Name: {4}{0}Total item types: {5}{0}Total item amount: {6}{0}" +
                        "Total items price: {7}{0}Delivery cost: {8}{0}Order cost: {9}{0}",
                System.lineSeparator(),
                orderID,
                orderDate,
                storeDetails.getId(),
                storeDetails.getStoreName(),
                getTotalItemTypes(),
                getTotalItemsAmount(),
                String.format("%.2f",getTotalItemsPrice()),
                String.format("%.2f", deliveryCost),
                String.format("%.2f",getOrderCost()));
    }
}
