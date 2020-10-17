package my.project.store;

import dto.SaleDTO;
import dto.ShoppingCartDTO;
import dto.StoreDTO;
import dto.SuperMarketItemDTO;
import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import my.project.item.ShoppingCartItem;
import my.project.item.SuperMarketItem;
import my.project.item.ePurchaseCategory;
import my.project.location.Location;
import my.project.order.ShoppingCart;
import my.project.sale.Sale;
import my.project.sale.SaleDetails;
import my.project.user.StoreOwner;

import java.awt.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

  public class Store {
      private String ownerName;
      private String storeName;
      private final int ID;
      private Location location;
      private HashMap<Integer, SuperMarketItem> superMarketItems;
      private double pricePerKilometer;
      private HashMap<String, Sale> sales;
      private HashMap<Integer, ShoppingCart> orders;
      private double totalDeliveriesRevenues;

      public Store(String ownerName,String storeName, int ID, Location location, double pricePerKilometer) {
          this.ownerName = ownerName;
          this.storeName = storeName;
          this.ID = ID;
          this.location = location;
          this.pricePerKilometer = pricePerKilometer;
          superMarketItems = new HashMap<>();
          orders = new HashMap<>();
          sales = new HashMap<>();
      }

      /*public Store(String name, int ID, Location location, HashMap<Integer, SuperMarketItem> superMarketItems,
                   double pricePerKilometer, HashMap<String, Sale> sales, HashMap<Integer, ShoppingCart> orders, double deliveriesRevenues) {
          this.storeName = name;
          this.ID = ID;
          this.location = location;
          this.superMarketItems = superMarketItems;
          this.pricePerKilometer = pricePerKilometer;
          this.sales = sales;
          this.orders = orders;
          totalDeliveriesRevenues = deliveriesRevenues;
      }*/

      public Location getLocation() {
          return location;
      }

      public void setLocation(Location location) {
          this.location = location;
      }

      public void setLocation(int x, int y) {
          location.setX(x);
          location.setY(y);
      }

      public HashMap<Integer, SuperMarketItem> getSuperMarketItems() {
          return superMarketItems;
      }

      public void setSuperMarketItems(HashMap<Integer, SuperMarketItem> superMarketItems) {
          this.superMarketItems = superMarketItems;
      }

      public void addProduct(SuperMarketItem item) {
          if (superMarketItems.putIfAbsent(item.getSerialNumber(), item) != null) {
              throw new ValueException("You already have item with serial number '" + item.getSerialNumber() + "' in this store");
          }
      }

      public void addProduct(int serialNumber, String itemName, ePurchaseCategory purchaseCategory, double price, double totalItemSold) {
          addProduct(new SuperMarketItem(serialNumber, itemName, purchaseCategory, price, totalItemSold));
      }

      public void removeItem(int serialNumber) {
          if (superMarketItems.remove(serialNumber) == null) {
              throw new ValueException("There is no item with serial number '" + serialNumber + "' in this store");
          }
      }

      public void updateProductName(int serialNumber, String newName) {
          SuperMarketItem item = superMarketItems.get(serialNumber);

          if (item != null) {
              item.setItemName(newName);
          } else {
              throw new ValueException("There is no item with serial number '" + serialNumber + "' in this store");
          }
      }

      public void updateProductPrice(int serialNumber, double newPrice) {
          SuperMarketItem item = superMarketItems.get(serialNumber);

          if (item != null) {
              item.setPrice(newPrice);
          } else {
              throw new ValueException("There is no item with serial number '" + serialNumber + "' in this store");
          }
      }

      public void updateProductNameAndPrice(int serialNumber, String newName, double newPrice) {
          SuperMarketItem item = superMarketItems.get(serialNumber);

          if (item != null) {
              item.setItemName(newName);
              item.setPrice(newPrice);
          } else {
              throw new ValueException("There is no item with serial number '" + serialNumber + "' in this store");
          }
      }

      public double getPricePerKilometer() {
          return pricePerKilometer;
      }

      public void setPricePerKilometer(double pricePerKilometer) {
          this.pricePerKilometer = pricePerKilometer;
      }

      public SuperMarketItem getItem(int serialNumber) {
          return superMarketItems.get(serialNumber);
      }

      public String getStoreName() {
          return storeName;
      }

      public void setStoreName(String storeName) {
          this.storeName = storeName;
      }

      public int getID() {
          return ID;
      }

      @Override
      public String toString() {
          return MessageFormat.format("Id: {1}{0}Name: {2}{0}" +
                          "Items:{0}{3} Orders:{0}{4} Price Per Kilometer(PPK): {5}{0}" +
                          "Total payment received from deliveries: {6}",
                  System.lineSeparator(),
                  ID,
                  storeName,
                  itemsToString(),
                  ordersToString(),
                  pricePerKilometer,
                  totalDeliveriesRevenues);
      }

      private String ordersToString() {
          String allOrdersToString;

          if (orders.isEmpty()) {
              allOrdersToString = "None- there have been no orders";
          } else {
              allOrdersToString = "";
              for (ShoppingCart order : orders.values()) {
                  allOrdersToString = allOrdersToString.concat(order + System.lineSeparator());
              }
          }

          return allOrdersToString;
      }

      private String itemsToString() {
          String allItemsToString;

          if (superMarketItems.isEmpty()) {
              allItemsToString = "None- this store has no items yet ";
          } else {
              allItemsToString = "";
              for (SuperMarketItem item : superMarketItems.values()) {
                  allItemsToString = allItemsToString.concat(item + System.lineSeparator());
              }
          }

          return allItemsToString;
      }

      public double getTotalDeliveriesRevenues() {
          return totalDeliveriesRevenues;
      }

      public void setTotalDeliveriesRevenues(double totalDeliveriesRevenues) {
          this.totalDeliveriesRevenues = totalDeliveriesRevenues;
      }

      public void addDeliveryRevenue(double amount) {
          totalDeliveriesRevenues += amount;
      }

      public HashMap<String, Sale> getSales() {
          return sales;
      }

      public HashMap<Integer, ShoppingCart> getOrders() {
          return orders;
      }

      public double getPriceItem(int serialNumber) {
          SuperMarketItem item = superMarketItems.getOrDefault(serialNumber, null);

          if (item == null) {
              throw new ValueException("In this store there is no item with serial number: " + serialNumber);
          } else {
              return item.getPrice();
          }
      }

      public StoreDTO createStoreDTO() {
          List<ShoppingCartDTO> orderDTOList = convertOrdersToDTO();
          List<SuperMarketItemDTO> superMarketItemDTOList = convertSuperMarketItemsToDTO();

          List<SaleDTO> salesDTOList = convertSalesToDTO();

          return new StoreDTO(ownerName, storeName, ID, new Point(location.getX(), location.getY()), superMarketItemDTOList, pricePerKilometer, salesDTOList, orderDTOList, totalDeliveriesRevenues);
      }

      private List<SaleDTO> convertSalesToDTO() {
          List<SaleDTO> salesDTO = new LinkedList<>();

          for(Sale sale : sales.values()){
              salesDTO.add(sale.createSaleDTO());
          }

          return salesDTO;
      }

      private List<ShoppingCartDTO> convertOrdersToDTO() {
          List<ShoppingCartDTO> orderDTOList = new LinkedList<>();

          for (ShoppingCart order : orders.values()) {
              orderDTOList.add(order.createShoppingCartDTO());
          }

          return orderDTOList;
      }

      private List<SuperMarketItemDTO> convertSuperMarketItemsToDTO() {
          List<SuperMarketItemDTO> superMarketItemDTOList = new LinkedList<>();

          for (SuperMarketItem item : superMarketItems.values()) {
              superMarketItemDTOList.add(item.createSuperMarketItemDTO());
          }

          return superMarketItemDTOList;
      }

      public void addOrder(ShoppingCart order) {
          orders.putIfAbsent(order.getOrderID(), order);
          for (ShoppingCartItem item : order.getCartItem()/*.values()*/) {
              superMarketItems.get(item.getSerialNumber()).addAmountToTotalItemSold(item.getAmount());
          }
      }

      public void addSale(Sale sale, List<SaleDetails> detailsList) {
          for (SaleDetails saleDetails : detailsList) {
              sale.addSaleDetails(saleDetails);
          }

          if (sales.containsKey(sale.getSaleName())) {

          } else {
              sales.put(sale.getSaleName(), sale);
          }
      }

      public List<Sale> getSalesOfItem(int itemSerialNumber, double amount) {
          List<Sale> salesOfItem = new LinkedList<>();
          for(Sale sale : sales.values()){
              Pair<Integer, Double> saleCondition = sale.getConditionSale();
              if(saleCondition.getKey() == itemSerialNumber && saleCondition.getValue() <= amount){
                  salesOfItem.add(sale);
              }
          }

          return salesOfItem.isEmpty()? null : salesOfItem;
      }

      public String getOwnerName() {
          return ownerName;
      }
  }