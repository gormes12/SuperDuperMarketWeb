package dto;

import java.awt.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

public class ShoppingCartDTO {
    private final int orderID;
    private List<ShoppingCartItemDTO> items;
    private double deliveryCost;
    private double distanceFromStore;
    private String orderDate;
//    private StoreDTO storeDetails;
    private int TotalItemAmount;
    private double TotalItemsPrice;
    private String storeName;
    private int storeID;
    private double storePPK;
    private String customerName;
    private int customerXCoordinate;
    private int customerYCoordinate;


    public ShoppingCartDTO(int orderID, LocalDate orderDate, List<ShoppingCartItemDTO> items, double deliveryCost,
                           double distanceFromStore, StoreDTO storeDetails, String customerName, Point customerLocation) {
        this.orderID = orderID;
        this.items = items;
        this.deliveryCost = deliveryCost;
        this.distanceFromStore = distanceFromStore;
        this.orderDate = orderDate.toString();
//        this.storeDetails = storeDetails;
        TotalItemAmount = getTotalItemsAmount();
        TotalItemsPrice = getTotalItemsPrice();
        storeName = storeDetails.getStoreName();
        storeID = storeDetails.getId();
        this.customerName = customerName;
        customerXCoordinate = customerLocation.x;
        customerYCoordinate = customerLocation.y;
        storePPK = storeDetails.getPricePerKilometer();
    }

    public String getStoreName(){
        return storeName;
    }

    public int getStoreID(){
        return storeID;
    }

    public List<ShoppingCartItemDTO> getItems() {
        return items;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public double getPricePerKilometer() {
        return storePPK;
    }

    public double getDistanceFromStore() {
        return distanceFromStore;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public int getTotalItemTypes(){
        return items.size();
    }

//    public Point getLocation(){
//        return storeDetails.getLocation();
//    }

    public int getTotalItemsAmount() {
        int sum = 0;

        for (ShoppingCartItemDTO item : items) {
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

        for (ShoppingCartItemDTO item : items) {
            sum += item.getTotalPrice();
        }

        return sum;
    }

    public int getOrderID() {
        return orderID;
    }

//    public StoreDTO getStoreDetails() {
//        return storeDetails;
//    }

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
                storeID,
                storeName,
                getTotalItemTypes(),
                getTotalItemsAmount(),
                String.format("%.2f",getTotalItemsPrice()),
                String.format("%.2f", deliveryCost),
                String.format("%.2f",getOrderCost()));
    }

    public int getCustomerXCoordinate() {
        return customerXCoordinate;
    }

    public int getCustomerYCoordinate() {
        return customerYCoordinate;
    }
}
