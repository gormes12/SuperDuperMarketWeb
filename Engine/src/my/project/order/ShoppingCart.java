package my.project.order;

import dto.ShoppingCartDTO;
import dto.ShoppingCartItemDTO;
import dto.StoreDTO;
import my.project.item.ShoppingCartItem;
import my.project.location.Location;
import my.project.store.Store;
import my.project.user.Customer;

import java.awt.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ShoppingCart {
    private final int orderID;
    private List<ShoppingCartItem> cartItem;
    private double deliveryCost;
    private double distanceFromStore;
    private LocalDate orderDate;
    private Store storeDetails;
    private Customer customerDetails;

    public ShoppingCart(int orderID, Store store, LocalDate orderDateRequested, Customer customer) {
        this.orderID = orderID;
        cartItem = new LinkedList<>();
        orderDate = orderDateRequested;
        distanceFromStore = calculateDistance(store.getLocation(), customer.getLocation());
        deliveryCost = distanceFromStore*store.getPricePerKilometer();
        storeDetails = store;
        customerDetails = customer;
    }

    private double calculateDistance(Location storeLocation, Location userLocation) {
        double a = storeLocation.getX() - userLocation.getX();
        double b = storeLocation.getY() - userLocation.getY();
        double powA = Math.pow(a, 2);
        double powB = Math.pow(b, 2);

        return Math.sqrt(powA + powB);
    }

    public Store getStoreDetails(){
        return storeDetails;
    }

//    public HashMap<Integer, ShoppingCartItem> getCartItem(){
//        return cartItem;
//    }

    public List<ShoppingCartItem> getCartItem(){
        return cartItem;
    }

    public void addItemToCart(ShoppingCartItem shoppingCartItem) {
       /* ShoppingCartItem item = cartItem.getOrDefault(shoppingCartItem.getSerialNumber(), null);

        if (item != null) {
            item.setAmount(item.getAmount() + shoppingCartItem.getAmount());
        } else {
            cartItem.put(shoppingCartItem.getSerialNumber(), shoppingCartItem);
        }*/
        cartItem.add(shoppingCartItem);
    }

    public ShoppingCartDTO createShoppingCartDTO() {
        List<ShoppingCartItemDTO> cartDTO = new LinkedList<>();

        for (ShoppingCartItem item: cartItem/*.values()*/) {
            cartDTO.add(new ShoppingCartItemDTO(
                    item.getSerialNumber(),
                    item.getItemName(),
                    item.getPurchaseCategory().toString(),
                    item.getPrice(),
                    item.getAmount(), item.isFromSale()));
        }

        return new ShoppingCartDTO(orderID, orderDate, cartDTO, deliveryCost, distanceFromStore,
                new StoreDTO(storeDetails.getOwnerName(), storeDetails.getStoreName(), storeDetails.getID(),
                        new Point(storeDetails.getLocation().getX(), storeDetails.getLocation().getY()),
                        null, storeDetails.getPricePerKilometer(), null, null, storeDetails.getTotalDeliveriesRevenues()), customerDetails.createCustomerDTO());
    }

    public int getOrderID(){
        return orderID;
    }

    public double getDeliveryCost(){
        return deliveryCost;
    }

    public int getTotalItemTypes(){
        return cartItem.size();
    }

    public int getTotalItemsAmount() {
        int sum = 0;

        for (ShoppingCartItem item : cartItem/*.values()*/) {
            if (item.getPurchaseCategory().toString().equals("Quantity")) {
                sum += item.getAmount();
            } else {
                sum += 1;
            }
        }

        return sum;
    }

    public double getTotalItemsPrice() {
        double sum = 0;

        for (ShoppingCartItem item : cartItem/*.values()*/) {
            sum += item.getTotalPriceOfItem();
        }

        return sum;
    }

    public double getOrderCost() {
        return getTotalItemsPrice()+deliveryCost;
    }

    public String toString() {
        return MessageFormat.format("Date: {1}{0}Total item amount: {2}{0}" +
                        "Total items price: {3}{0}Delivery cost: {4}{0}Order cost: {5}{0}",
                System.lineSeparator(),
                orderDate,
                getTotalItemsAmount(),
                String.format("%.2f",getTotalItemsPrice()),
                String.format("%.2f", deliveryCost),
                String.format("%.2f",getOrderCost()));
    }
}
