package dto;

import java.awt.*;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

public class StoreDTO {
    private final String ownerName;
    private final String storeName;
    private final int id;
    private final Point location;
    private final List<SuperMarketItemDTO> superMarketItems;
    private final double pricePerKilometer;
    private final List<SaleDTO> sales;
    private final List<ShoppingCartDTO> orders;
    private final double totalDeliveriesRevenues;
    private double totalItemsSoldRevenues;
    private final int xCoordinate;
    private final int yCoordinate;



    public StoreDTO(String ownerName, String storeName, int ID, Point location, List<SuperMarketItemDTO> superMarketItems,
                    double pricePerKilometer, List<SaleDTO> sales, List<ShoppingCartDTO> orders, double deliveriesRevenues) {
        this.ownerName = ownerName;
        this.storeName = storeName;
        this.id = ID;
        this.location = new Point(location.x, location.y){
            @Override
            public String toString() {
                return "(" + getX() + "," + getY() + ")";
            }
        };
        xCoordinate = location.x;
        yCoordinate = location.y;
        this.superMarketItems = superMarketItems;
        this.pricePerKilometer = pricePerKilometer;
        this.sales = sales;
        this.orders = orders;
        totalDeliveriesRevenues = deliveriesRevenues;
        if (orders!=null) {
            totalItemsSoldRevenues = calculateItemsSoldRevenues(orders);
        }
    }

    private double calculateItemsSoldRevenues(List<ShoppingCartDTO> orders) {
        double sum = 0;
        for (ShoppingCartDTO shoppingCart : orders){
            sum +=shoppingCart.getTotalItemsPrice();
        }

        return sum;
    }

//    public StoreDTO(Store store)
//    {
//        name = store.getName();
//        ID = store.getID();
//        location = store.getLocation();
//        superMarketItems = convertSuperMarketItemsToDTO(store.getSuperMarketItems().values());
//        pricePerKilometer = store.getPricePerKilometer();
//        sales = store.getSales();
//        orders = convertOrdersToDTO(store.getOrders());
//        totalDeliveriesRevenues = store.getTotalDeliveriesRevenues();
//    }



    public String getStoreName() {
        return storeName;
    }

    public int getId() {
        return id;
    }

    public Point getLocation() {
        return location;
    }

    public List<SuperMarketItemDTO> getSuperMarketItems() {
        return superMarketItems;
    }

    public double getPricePerKilometer() {
        return pricePerKilometer;
    }

//    public List<SaleDTO> getSales() {
//        return sales;
//    }

    public double getTotalDeliveriesRevenues() {
        return totalDeliveriesRevenues;
    }

    public List<ShoppingCartDTO> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
            String separationLine = "------------------------------------";

            return MessageFormat.format("Store ID: {1}{0}Name: {2}{0}" +
                            "{7}{0}Items: {3}{7}{0}Orders: {4}{7}{0}Price Per Kilometer(PPK): {5}{0}" +
                            "Total payment received from deliveries: {6}{0}",
                    System.lineSeparator(),
                    id,
                    storeName,
                    collectionToString(superMarketItems),//itemsToString(),
                    ordersToString(),
                    String.format("%.2f",pricePerKilometer),
                    String.format("%.2f",totalDeliveriesRevenues),
                    separationLine);

    }

    private < T > String collectionToString(Collection<T> tCollection){
        String result;

        if(tCollection.isEmpty()){
            result = "None" + System.lineSeparator();
        }
        else {
            result = System.lineSeparator();
            for (T obj : tCollection) {
                result = result.concat(obj + System.lineSeparator());
            }
        }

        return result;
    }

    private String ordersToString() {
        String allOrdersToString;

        if(orders.isEmpty()){
            allOrdersToString = "None- there have been no orders" + System.lineSeparator();
        }
        else {
            allOrdersToString = "";
            for (ShoppingCartDTO order : orders) {
                allOrdersToString = allOrdersToString.concat(
                        MessageFormat.format("Date: {1}{0}Total item amount: {2}{0}" +
                                "Total items price: {3}{0}Delivery cost: {4}{0}Order cost: {5}{0}",
                        System.lineSeparator(),
                        order.getOrderDate(),
                        order.getTotalItemsAmount(),
                        order.getTotalItemsPrice(),
                        String.format("%.2f", order.getDeliveryCost()),
                        order.getOrderCost()));
            }
            //allOrdersToString.concat(order + System.lineSeparator());
        }

        return allOrdersToString;
    }

    public List<SaleDTO> getSales() {
        return sales;
    }
//
//    private String itemsToString() {
//        String allItemsToString;
//
//        if(superMarketItems.isEmpty()){
//            allItemsToString = "None- this store has no items yet ";
//        }
//        else {
//            allItemsToString = "";
//            for (dto.SuperMarketItemDTO item : superMarketItems) {
//                allItemsToString = allItemsToString.concat(item + System.lineSeparator());
//            }
//        }
//
//        return allItemsToString;
//    }

}
