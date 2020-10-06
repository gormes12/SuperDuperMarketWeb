package dto;

import java.awt.*;
import java.util.HashMap;

public class CustomerDTO {
    private final int ID;
    private final String name;
    private final Point location;
    private final HashMap<Integer, OrderDTO> orders;

    public CustomerDTO(int ID, String name, Point location, HashMap<Integer, OrderDTO> orders){
        this.ID = ID;
        this.name = name;
        this.location = location;
        this.orders = orders;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public int getTotalAmountOrdersPlaced(){
        return orders.size();
    }

    public double getAvgOrdersPrice() {
        double sumOrdersPrice = 0;

        for(OrderDTO order : orders.values()){
            sumOrdersPrice += order.getTotalItemsPrice();
        }

        return orders.size()==0? orders.size() : sumOrdersPrice / orders.size();
    }

    public double getAvgDeliveryPrice() {
        double sumDeliveryPrice = 0;

        for(OrderDTO order : orders.values()){
            sumDeliveryPrice += order.getDeliveryCost();
        }

        return orders.size()==0? orders.size() : sumDeliveryPrice / orders.size();
    }

    @Override
    public String toString() {
        return name + ", ID: " + ID;
    }
}
