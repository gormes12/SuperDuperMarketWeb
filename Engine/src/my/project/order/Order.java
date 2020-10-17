package my.project.order;

import dto.OrderDTO;
import dto.SaleDTO;
import dto.ShoppingCartDTO;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import my.project.item.ShoppingCartItem;
import my.project.store.Store;
import my.project.user.Customer;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Order {
    private static int counterOrderID = 100;
    private final int orderID;
    private HashMap<Integer, ShoppingCart> shoppingCarts; // Integer: storeID
    private double deliveryCost;
    private LocalDate orderDate;

    public Order(LocalDate orderDateRequested) {
        orderID = counterOrderID++;
        shoppingCarts = new HashMap<>();
        orderDate = orderDateRequested;
    }

    public void addNewShoppingCart(Store store, Customer customer){
        if(!shoppingCarts.containsKey(store.getID())){
            ShoppingCart shoppingCart = new ShoppingCart(orderID, store, orderDate, customer);
            shoppingCarts.put(store.getID(), shoppingCart);
            addTotalDeliveryCost(shoppingCart.getDeliveryCost());
        }else{
            throw new ValueException("You already have an active order from this store.");
        }
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    private void addTotalDeliveryCost(double cost) {
        deliveryCost+=cost;
    }

    public void addItemToShoppingCartFromStore(int storeID, ShoppingCartItem shoppingCartItem){
        ShoppingCart cart = shoppingCarts.getOrDefault(storeID, null);

        if (cart!=null){
            cart.addItemToCart(shoppingCartItem);
        }
        else{
            throw new NullPointerException("You have no an active order from this store.");
        }
    }

    public HashMap<Integer, ShoppingCart> getShoppingCarts(){
        return shoppingCarts;
    }

    public OrderDTO createOrderDTO() {
        List<ShoppingCartDTO> shoppingCartsDTO = new LinkedList<>();

        for (ShoppingCart cart: shoppingCarts.values()) {
            shoppingCartsDTO.add(cart.createShoppingCartDTO());
        }

        return new OrderDTO(orderID, orderDate, shoppingCartsDTO, deliveryCost);/*, pricePerKilometer, distanceFromStore, storeDetails);*/
    }

    public int getOrderID(){
        return orderID;
    }

    public double getDeliveryCost(){
        return deliveryCost;
    }

    public int getTotalItemTypes(){
        int sum = 0;

        for (ShoppingCart cart : shoppingCarts.values()) {
            sum += cart.getTotalItemTypes();
        }

        return sum;
    }

    public int getTotalItemsAmount() {
        int sum = 0;

        for (ShoppingCart cart : shoppingCarts.values()) {
            sum += cart.getTotalItemsAmount();
        }

        return sum;
    }

    public double getTotalItemsPrice() {
        double sum = 0;

        for (ShoppingCart cart : shoppingCarts.values()) {
            sum += cart.getTotalItemsPrice();
        }

        return sum;
    }
}
