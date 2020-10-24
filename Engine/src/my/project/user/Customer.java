package my.project.user;

import dto.*;
import my.project.location.Location;
import my.project.order.Order;
import my.project.user.User;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Customer extends User {

    private Location location;
    private HashMap<String, List<OrderDTO>> orders; // String: ZoneName

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

    public void addOrder(String zoneName, OrderDTO order) {
        List<OrderDTO> orderList = orders.getOrDefault(zoneName, null);
        if (orderList == null){
            orderList = new LinkedList<>();
        }

        orderList.add(order);
        orders.put(zoneName, orderList);
    }

    public Collection<OrderDTO> getOrdersFromZone(String zoneName) {
        return orders.get(zoneName);
    }

    public int getTotalAmountOrdersPlaced(){
        return orders.size();
    }

    /*public double getAvgOrdersPrice() {
        double sumOrdersPrice = 0;

        for(Order order : orders.values()){
            sumOrdersPrice += order.getTotalItemsPrice();
        }

        return sumOrdersPrice / orders.size();
    }*/

    /*public double getAvgDeliveryPrice() {
        double sumDeliveryPrice = 0;

        for(Order order : orders.values()){
            sumDeliveryPrice += order.getDeliveryCost();
        }

        return sumDeliveryPrice / orders.size();
    }*/

   /* public CustomerDTO createCustomerDTO() {
        HashMap<Integer, OrderDTO> allOrders = new HashMap<>();
        for(Order order : orders.values()){
            allOrders.put(order.getOrderID(), order.createOrderDTO());
        }

        return new CustomerDTO(ID, username, new Point(location.getX(), location.getY()), allOrders);
    }*/
}
