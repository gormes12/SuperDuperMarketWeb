package my.project.user;

import dto.*;
import my.project.location.Location;
import my.project.order.Order;
import my.project.user.User;

import java.awt.*;
import java.util.HashMap;

public class Customer extends User {

    private Location location;
    private HashMap<Integer, Order> orders;

    public Customer(int id, String username) {
        super(id,username, eUserType.Customer);
        orders = new HashMap<>();
    }

    public Customer(int id, String username, Location location) {
        super(id,username, eUserType.Customer);
        this.location=location;
        orders = new HashMap<>();
    }

    public eUserType getUserType() {
        return eUserType.Customer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addOrder(Order order) {
        orders.putIfAbsent(order.getOrderID(), order);
    }

    public int getTotalAmountOrdersPlaced(){
        return orders.size();
    }

    public double getAvgOrdersPrice() {
        double sumOrdersPrice = 0;

        for(Order order : orders.values()){
            sumOrdersPrice += order.getTotalItemsPrice();
        }

        return sumOrdersPrice / orders.size();
    }

    public double getAvgDeliveryPrice() {
        double sumDeliveryPrice = 0;

        for(Order order : orders.values()){
            sumDeliveryPrice += order.getDeliveryCost();
        }

        return sumDeliveryPrice / orders.size();
    }

    public CustomerDTO createCustomerDTO() {
        HashMap<Integer, OrderDTO> allOrders = new HashMap<>();
        for(Order order : orders.values()){
            allOrders.put(order.getOrderID(), order.createOrderDTO());
        }

        return new CustomerDTO(ID, username, new Point(location.getX(), location.getY()), allOrders);
    }
}
