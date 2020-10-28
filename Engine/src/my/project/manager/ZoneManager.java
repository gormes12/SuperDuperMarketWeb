package my.project.manager;

import dto.*;
import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import my.project.user.Customer;
import my.project.item.*;
import my.project.location.Location;
import my.project.order.Order;
import my.project.order.ShoppingCart;
import my.project.sale.Sale;
import my.project.sale.SaleDetails;
import my.project.store.Store;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class ZoneManager {
    public static final int LocationXMaxCoordinate = 50;
    public static final int LocationYMaxCoordinate = 50;
    public static final int LocationXMinCoordinate = 1;
    public static final int LocationYMinCoordinate = 1;
    public static int STORE_ID = 100;
    public static int ITEM_SERIALNUMBER = 100;

    private String ownerZoneName;
    private final String zoneName;
    private HashMap<Integer, Item> items;   // K: serialNumber, V:item
    private HashMap<Integer, Pair<List<Store>, Double>> pairListStoreAndAvgPricePerItem;  // K: serialNumber, V:Pair(K: Store list that sell it, V: Average price per item)
    private HashMap<Integer, Double> amountSoldItem;   //K: serialNumber, V: Number of times that item sold

    private HashMap<Location, Store> storesByLocation;
    private HashMap<Integer, Store> storesById;
    private HashMap<String, List<Store>> storesByName;

    private HashMap<Location, Customer> customersByLocation;
    private HashMap<Integer, Customer> customersById;
    private HashMap<String, List<Customer>> customersByName;

    private HashMap<Integer, Order> orders;

    private Order tempOrder;
    private List<Sale> tempDeservedSales;

    public ZoneManager(String zoneName, String ownerZoneUsername) {
        this.zoneName = zoneName;
        ownerZoneName = ownerZoneUsername;
        items = new HashMap<>();
        pairListStoreAndAvgPricePerItem = new HashMap<>();
        storesById = new HashMap<>();
        storesByLocation = new HashMap<>();
        storesByName = new HashMap<>();
        amountSoldItem = new HashMap<>();
        orders = new HashMap<>();
        customersByLocation = new HashMap<>();
        customersById = new HashMap<>();
        customersByName = new HashMap<>();
    }

    public void createOrder(LocalDate orderDateRequested) {
        tempOrder = new Order(orderDateRequested);
        tempDeservedSales = new LinkedList<>();
    }

    public void createShoppingCart(int storeID, Customer customer) {
        Store store = storesById.get(storeID);
//        double distance = calculateDistance(store.getLocation(), customer.getLocation());
        tempOrder.addNewShoppingCart(store, customer);
        //tempOrder = new Order(storeID, store.getName(), orderDateRequested, distance, store.getPricePerKilometer());
    }



    public void addStore(Store store) {

        if (isLocationCaughtUp(store.getLocation())) {
            throw new ValueException("The location " + store.getLocation() + " already taken!");
        } else {
            storesByLocation.put(store.getLocation(), store);
            if (storesById.putIfAbsent(store.getID(), store) != null) {
                throw new ValueException("There is already store with id '" + store.getID() + "' in the system!");
            }
            List<Store> storeList = storesByName.get(store.getStoreName());
            if (storeList == null) {
                storeList = new LinkedList<>();
            }

            storeList.add(store);
            storesByName.put(store.getStoreName(), storeList);
        }
    }

    public void addStore(String ownerName, String storeName, int storeID, Location storeLocation, double pricePerKilometer) {
        Store newStore = new Store(ownerName, storeName, storeID, storeLocation, pricePerKilometer);
        addStore(newStore);
    }

    public StoreDTO addStore(String ownerName, String storeName, Location storeLocation, double pricePerKilometer, HashMap<Integer, Double> items){
        Store newStore = new Store(ownerName, storeName, STORE_ID, storeLocation, pricePerKilometer);
        addStore(newStore);

        for (Map.Entry<Integer, Double> entry : items.entrySet()) {
            addItemToStore(STORE_ID, entry.getKey(), entry.getValue());
        }

        STORE_ID++;

        return newStore.createStoreDTO();
    }

    public void addItemToStore(int storeID, SuperMarketItem superMarketItem) {
        Store store = storesById.getOrDefault(storeID, null);
        Item item = items.getOrDefault(superMarketItem.getSerialNumber(), null);

        if (store != null) {
            if (item != null) {
                store.addProduct(superMarketItem);
                addItemToListStoreAndUpdateAvgPrice(store, superMarketItem.getSerialNumber(), superMarketItem.getPrice());
            } else {
                throw new ValueException("There is no item with serial number: " + superMarketItem.getSerialNumber() + " in the system");
            }
        } else {
            throw new ValueException("There is no store with id: " + storeID + " in the system");
        }
    }

    public void addItemToStore(int storeID, int serialNumber, String itemName,
                               ePurchaseCategory purchaseCategory, double price, double totalItemSold) {
        addItemToStore(storeID, new SuperMarketItem(serialNumber, itemName, purchaseCategory, price, totalItemSold));
    }

    public void addItemToStore(int storeId, int itemId, double price) {
        Item item = items.getOrDefault(itemId, null);
        if (item != null) {
            addItemToStore(storeId, new SuperMarketItem(item, price, 0));
        } else {
            throw new ValueException("There is no item with serial number: " + itemId + " in the system");
        }
    }

    public void addSaleToStore(int storeID, String saleName, String itemName, int itemId, double quantity, String operator, List<SaleDetails> detailsList) {
        if (isValidIDStore(storeID)) {
            checkValidItem(storeID, itemId, quantity);
            checkDetailsList(storeID, detailsList);
            storesById.get(storeID).addSale(new Sale(saleName, itemName, itemId, quantity, storeID, operator), detailsList);
        } else {
            throw new ValueException("There is no store with id:" + storeID + " in the system");
        }
    }

    private void checkValidItem(int storeID, int itemId, double quantity) {
        if (!isAvailableItemInSystem(itemId)) {
            throw new ValueException("There is no item with serial number: " + itemId + " in the system");
        } else if (!isAvailableItemInStore(storeID, itemId)) {
            throw new ValueException("There is no item with serial number: " + itemId + " in the store id:" + storeID);
        } else if (!isQuantitySuitableToItem(itemId, quantity)) {
            throw new ValueException("Item with serial number: " + itemId + " can not be sold to a decimal quantity");
        }
    }

    private boolean isQuantitySuitableToItem(int itemId, double quantity) {
        Item item = items.get(itemId);
        if (item.getPurchaseCategory() == ePurchaseCategory.Quantity) {
            if (quantity % 1 != 0) {
                return false;
            }
        }

        return true;
    }

    private void checkDetailsList(int storeID, List<SaleDetails> detailsList) {
        for (SaleDetails details : detailsList) {
            checkValidItem(storeID, details.getItemSerialNumber(), details.getQuantity());
        }
    }

    private void addItemToListStoreAndUpdateAvgPrice(Store store, int serialNumber, double itemPrice) {
        Pair<List<Store>, Double> listStoreAndAvgPrice = pairListStoreAndAvgPricePerItem.get(serialNumber);
        List<Store> storeList;
        if (listStoreAndAvgPrice == null) {
            storeList = new LinkedList<>();
            storeList.add(store);
            pairListStoreAndAvgPricePerItem.put(serialNumber, new Pair<>(storeList, itemPrice));
        } else {
            storeList = listStoreAndAvgPrice.getKey();
            storeList.add(store);
            double avgPrice = (((storeList.size() - 1) * listStoreAndAvgPrice.getValue()) + itemPrice) / storeList.size();
            pairListStoreAndAvgPricePerItem.put(serialNumber, new Pair<>(storeList, avgPrice));
        }
    }

    public void addItem(Item item) {
        if (items.putIfAbsent(item.getSerialNumber(), item) != null) {
            throw new ValueException("Error file: You have two items with the same serial number in the system.");
        }
    }

    public int addItemAndGetSerialNumber(String itemName, String purchaseCategory) {
        addItem(new Item(ITEM_SERIALNUMBER, itemName, purchaseCategory.equals("Quantity")? ePurchaseCategory.Quantity : ePurchaseCategory.Weight));
        ITEM_SERIALNUMBER++;
        return ITEM_SERIALNUMBER - 1;
    }

    public List<StoreDTO> getStores() {
        List<StoreDTO> result = new LinkedList<>();

        for (Store store : storesById.values()) {
            result.add(store.createStoreDTO());
        }

        return result;
    }

    public List<SaleDTO> getSalesOfActiveOrder() {
        List<SaleDTO> result = new LinkedList<>();

        for (Sale sale : tempDeservedSales) {
            result.add(sale.createSaleDTO());
        }

        return result;
    }

    public List<ItemDTO> getItems() {
        int howManyStoreSold;
        double averagePrice, howManyTimeSold;
        Pair<List<Store>, Double> listStoreAndAvgPrice;
        List<ItemDTO> result = new LinkedList<>();

        for (Item item : items.values()) {
            listStoreAndAvgPrice = pairListStoreAndAvgPricePerItem.get(item.getSerialNumber());
            howManyStoreSold = listStoreAndAvgPrice.getKey().size();
            averagePrice = listStoreAndAvgPrice.getValue();
            howManyTimeSold = amountSoldItem.getOrDefault(item.getSerialNumber(), (double) 0);
            result.add(new ItemDTO
                    (item.getSerialNumber(), item.getItemName(), item.getPurchaseCategory().toString(), howManyStoreSold, averagePrice, howManyTimeSold));
        }

        return result;
    }

    public void isThereItemThatNotOfferedForSell() {
        for (Integer serialNumber : items.keySet()) {
            if (pairListStoreAndAvgPricePerItem.getOrDefault(serialNumber, null) == null) {
                throw new ValueException("All items in the system must be offered for sale."
                        + System.lineSeparator() + "Item with serial Number '" + serialNumber + "' not offered for sale in any store in the system.");
            }
        }
    }

    public boolean isValidIDStore(int storeID) {
        return storesById.containsKey(storeID);
    }

    public boolean isLocationCaughtUp(int xCoordinate, int yCoordinate) {
        return storesByLocation.containsKey(new Location(xCoordinate, yCoordinate));
    }

    public boolean isLocationCaughtUp(Location location) {
        return storesByLocation.containsKey(location);
    }

    public double getPriceItemFromStore(int storeID, int serialNumber) {
        double itemPrice;
        Store store = storesById.getOrDefault(storeID, null);

        if (store != null) {
            try {
                itemPrice = store.getPriceItem(serialNumber);
            } catch (ValueException e) {
                itemPrice = -1;
            }
        } else {
            throw new ValueException("There is no store in the system with ID: " + storeID);
        }

        return itemPrice;
    }

    public boolean isAvailableItemInSystem(int userInputSerialNumber) {
        return items.containsKey(userInputSerialNumber);
    }

    private SuperMarketItem getItemFromStore(int storeID, int serialNumber) {
        return storesById.get(storeID).getSuperMarketItems().getOrDefault(serialNumber, null);
    }

    public boolean isAvailableItemInStore(int storeID, int itemSerialNumber) {
        return storesById.get(storeID).getSuperMarketItems().containsKey(itemSerialNumber);
    }

    public String getPurchaseCategoryOfItem(int itemSerialNumber) {
        return items.get(itemSerialNumber).getPurchaseCategory().toString();
    }

    public void addItemToCartFromStore(int storeID, int itemSerialNumber, double amount) {
        SuperMarketItem item = getItemFromStore(storeID, itemSerialNumber);
        tempOrder.addItemToShoppingCartFromStore(storeID, new ItemInShoppingCart(item, amount));
        List<Sale> sales = getSaleOfThisItemFromStore(storeID, itemSerialNumber, amount);
        if (sales != null) {
            if (tempDeservedSales == null) {
                tempDeservedSales = new LinkedList<>();
            }
            tempDeservedSales.addAll(sales);
        }
    }

    private List<Sale> getSaleOfThisItemFromStore(int storeID, int itemSerialNumber, double amount) {
        Store store = storesById.getOrDefault(storeID, null);
        if (store != null) {
            return store.getSalesOfItem(itemSerialNumber, amount);
        }

        return null;
    }

    public OrderDTO getActiveOrder() {
        return tempOrder.createOrderDTO();
    }

    /*public void executeOrderAndAddToSystem(int customerID) {
        Customer customer = customersById.getOrDefault(customerID, null);
        if (customer != null) {
            executeOrderAndAddToSystem(tempOrder);
            customer.addOrder(tempOrder);
        } else {
            throw new ValueException("Customer's ID not found! Please Try Again!");
        }
    }*/

    public void executeOrderAndAddToSystem(Order orderToExecute/*, Customer customer*/) {
        orders.putIfAbsent(orderToExecute.getOrderID(), orderToExecute);
//        customer.addOrder(orderToExecute);
        for (ShoppingCart cart : orderToExecute.getShoppingCarts().values()) {
            Store store = cart.getStoreDetails();//storesById.get(inputStoreID);
            store.addOrder(cart);
            store.addDeliveryRevenue(cart.getDeliveryCost());
        }

        updateAmountSoldPerItem(orderToExecute);
    }

    private void updateAmountSoldPerItem(Order fromOrder) {
        for (ShoppingCart cart : fromOrder.getShoppingCarts().values()) {
            for (ShoppingCartItem item : cart.getCartItem()/*.values()*/) {
                amountSoldItem.put(item.getSerialNumber(), amountSoldItem.getOrDefault(item.getSerialNumber(), (double) 0) + item.getAmount());
            }
        }
    }

    public Pair<Integer, String> getStoreDetails(int orderID) {
        return null;
    }

    public Object getOrders() {
        List<OrderDTO> result = new LinkedList<>();

        for (Order order : orders.values()) {
            result.add(order.createOrderDTO());
        }

        return result;
    }

    public List<SuperMarketItemDTO> getAvailableItemFrom(int storeID) {
        List<SuperMarketItemDTO> result = null;
        Store store = storesById.getOrDefault(storeID, null);
        if (store != null) {
            result = new LinkedList<>();
            for (SuperMarketItem item : store.getSuperMarketItems().values()) {
                result.add(item.createSuperMarketItemDTO());
            }
        } else {
            throw new ValueException("There is no store with id '" + storeID + "' in system.");
        }

        return result;
    }

    public boolean deleteItemFrom(int storeID, int serialNumber) {
        boolean isDeleted = false;
        if (isAvailableItemInStore(storeID, serialNumber)) {
            List<Store> storeList = pairListStoreAndAvgPricePerItem.get(serialNumber).getKey();
            if (storeList.size() != 1) {
                Store store = storesById.get(storeID);
                storeList.remove(store);
                removeItemFromStoreListAndUpdateAvgPrice(storeList, store.getPriceItem(serialNumber), serialNumber);
                store.removeItem(serialNumber);
                isDeleted = true;
            }
        } else {
            throw new ValueException("Item with serial number '" + serialNumber + "' not available in store with ID '" + storeID + "'.");
        }

        return isDeleted;
    }

    private void removeItemFromStoreListAndUpdateAvgPrice(List<Store> storeList, double priceItem, int serialNumber) {
        double prevAvgPrice = pairListStoreAndAvgPricePerItem.get(serialNumber).getValue();
        double newAvgPrice = (prevAvgPrice * (storeList.size() + 1) - priceItem) / storeList.size();

        pairListStoreAndAvgPricePerItem.put(serialNumber, new Pair<>(storeList, newAvgPrice));
    }

    public void updatePriceItemInStore(int storeID, int serialNumber, double newPrice) {
        if (isAvailableItemInStore(storeID, serialNumber)) {
            Store store = storesById.getOrDefault(storeID, null);
            if (store != null) {
                updateAvgPrice(serialNumber, newPrice, store.getPriceItem(serialNumber));
                store.updateProductPrice(serialNumber, newPrice);
            } else {
                throw new ValueException("There in no store with id: " + storeID + " in the system.");
            }
        } else {
            throw new ValueException("In store with id '" + storeID + "' there is no item with id: " + serialNumber);
        }
    }

    private void updateAvgPrice(int serialNumber, double newPrice, double prevPrice) {
        Pair<List<Store>, Double> listStoreAndPricePair = pairListStoreAndAvgPricePerItem.get(serialNumber);
        double prevAvgPrice = listStoreAndPricePair.getValue();
        double newAvgPrice = (prevAvgPrice * (listStoreAndPricePair.getKey().size()) + newPrice - prevPrice) / listStoreAndPricePair.getKey().size();

        pairListStoreAndAvgPricePerItem.put(serialNumber, new Pair<>(listStoreAndPricePair.getKey(), newAvgPrice));
    }

    public Order createMinOrderFromItemList(LocalDate orderDateRequested, Customer customer, HashMap<Integer, Double> itemList) {
        int storeID;
        createOrder(orderDateRequested);
        for (Integer serialNumber : itemList.keySet()) {
            storeID = getStoreIDOfMinPriceOfItem(serialNumber);
            if (!tempOrder.getShoppingCarts().containsKey(storeID)) {
                createShoppingCart(storeID, customer);
            }

            addItemToCartFromStore(storeID, serialNumber, itemList.get(serialNumber));
        }

        return tempOrder;
    }

    private int getStoreIDOfMinPriceOfItem(Integer serialNumber) {
        Pair<List<Store>, Double> listStoreAndAvgPrice = pairListStoreAndAvgPricePerItem.getOrDefault(serialNumber, null);
        listStoreAndAvgPrice.getKey().sort(new Comparator<Store>() {
            @Override
            public int compare(Store o1, Store o2) {
                if (o1.getPriceItem(serialNumber) - o2.getPriceItem(serialNumber) >= 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        return listStoreAndAvgPrice.getKey().get(0).getID();
    }

    public void addCustomer(String name, int id, Location location) {
        addCustomer(new Customer(id, name, location));
    }

    public void addCustomer(Customer customer) {
        if (isLocationCaughtUp(customer.getLocation())) {
            throw new ValueException("The location " + customer.getLocation() + " already taken!");
        } else {
            customersByLocation.put(customer.getLocation(), customer);
            if (customersById.putIfAbsent(customer.getID(), customer) != null) {
                throw new ValueException("There is already customer with id '" + customer.getID() + "' in the system!");
            }

            List<Customer> customerList = customersByName.get(customer.getUsername());
            if (customerList == null) {
                customerList = new LinkedList<>();
            }

            customerList.add(customer);
            customersByName.put(customer.getUsername(), customerList);
        }
    }

    /*public List<CustomerDTO> getCustomers() {
        List<CustomerDTO> result = new LinkedList<>();

        for (Customer customer : customersById.values()) {
            result.add(customer.createCustomerDTO());
        }

        return result;
    }*/

    /*public double calculateDeliveryCost(StoreDTO store, CustomerDTO customer) {
        return store.getPricePerKilometer() * (calculateDistance(new Location(store.getLocation()), customer.getLocation()));
    }*/

    public Order createOrderFromItemList(LocalDate date, int storeID, Customer customer, HashMap<Integer, Double> itemsList) {
        createOrder(date);
        createShoppingCart(storeID, customer);
        for (Map.Entry<Integer, Double> entry : itemsList.entrySet()) {
            addItemToCartFromStore(storeID, entry.getKey(), entry.getValue());
        }

        return tempOrder;
    }

    public boolean isAvailableSales() {
        return tempDeservedSales != null && !tempDeservedSales.isEmpty();
    }

    public String getNameOfItem(int serialNumber) {
        Item item = items.getOrDefault(serialNumber, null);
        if (item != null) {
            return item.getItemName();
        } else {
            throw new ValueException("Item not Found in System!");
        }
    }

    public Order addChosenSaleItemsToCart(HashMap<SaleDTO, Integer> chosenSaleItems, Order orderToAddSales) {
        for (SaleDTO sale : chosenSaleItems.keySet()) {
            if (sale.getOperator().equals("ONE-OF")) {
                SaleDetailsDTO saleDetails = sale.getSpecificSaleDetailsOnItemFromSale(chosenSaleItems.get(sale));
                addSaleToCartFromStore(orderToAddSales, sale.getBelongToStoreID(), saleDetails);
            } else {
                for (SaleDetailsDTO saleDetails : sale.getDetails()){
                    addSaleToCartFromStore(orderToAddSales, sale.getBelongToStoreID(), saleDetails);
                }
            }
        }

        return orderToAddSales;
    }

    private void addSaleToCartFromStore(Order orderToAddSale ,int storeID, SaleDetailsDTO saleDetails) {
        SuperMarketItem item = getItemFromStore(storeID, saleDetails.getItemSerialNumber());
        orderToAddSale.addItemToShoppingCartFromStore(
                storeID, new SaleItemInShoppingCart(
                        item.getSerialNumber(),
                        item.getItemName(),
                        item.getPurchaseCategory(),
                        saleDetails.getAdditionalChargePerUnit(),
                        saleDetails.getQuantity()));

    }

    public Point getMaxCoordinateXY() {
        int maxX = 0, maxY = 0;
        for (Location location : storesByLocation.keySet()){
            if(location.getX() > maxX){
                maxX = location.getX();
            }
            if(location.getY() > maxY){
                maxY = location.getY();
            }
        }
        for (Location location : customersByLocation.keySet()){
            if(location.getX() > maxX){
                maxX = location.getX();
            }
            if(location.getY() > maxY){
                maxY = location.getY();
            }
        }

        return new Point(maxX, maxY);
    }

    public List<Point> getStoresLocation() {
        List<Point> storesLocation = new LinkedList<>();
        for (Location location : storesByLocation.keySet()){
            storesLocation.add(new Point(location.getX(), location.getY()));
        }

        return storesLocation;
    }

    public List<Point> getCustomersLocation() {
        List<Point> storesLocation = new LinkedList<>();
        for (Location location : customersByLocation.keySet()){
            storesLocation.add(new Point(location.getX(), location.getY()));
        }

        return storesLocation;
    }

    public String getOwnerZoneName() {
        return ownerZoneName;
    }

    public String getStoreOwnerName(int storeID) {
        Store store = storesById.get(storeID);
        return store.getOwnerName();
    }

    public void setOwnerZoneName(String ownerZoneName) {
        this.ownerZoneName = ownerZoneName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public ZoneDTO createZoneDTO(){
        double sum = 0;

        for (Order order : orders.values()){
            sum += order.getTotalItemsPrice();
        }

        return new ZoneDTO(ownerZoneName, zoneName, items.size(), storesById.size(), orders.size(),
                orders.size() == 0? 0: sum / orders.size());
    }

    public List<ShoppingCartDTO> getShoppingCartsOfStore(int storeID) {
        List<ShoppingCartDTO> result = new LinkedList<>();

        Store store = storesById.get(storeID);
        for (ShoppingCart shoppingCart : store.getOrders().values()){
            result.add(shoppingCart.createShoppingCartDTO());
        }

        return result;
    }

}
