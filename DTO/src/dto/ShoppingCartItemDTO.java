package dto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.MessageFormat;

public class ShoppingCartItemDTO {
    private final int serialNumber;
    private final String itemName;
    private final String purchaseCategory;
    private final double price;
    private double amount;
    private double TotalPrice;
    private boolean IsFromSale;

    /*private SimpleIntegerProperty SerialNumber;
    private SimpleStringProperty ItemName;
    private SimpleStringProperty PurchaseCategory;
    private SimpleDoubleProperty Price;
    private SimpleDoubleProperty Amount;
    private SimpleDoubleProperty TotalPrice;
    private SimpleBooleanProperty IsFromSale;*/

    public ShoppingCartItemDTO(int serialNumber, String itemName, String purchaseCategory, double price, double amount, boolean fromSale){
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.purchaseCategory = purchaseCategory;
        this.price = price;
        this.amount = amount;
        /*SerialNumber = new SimpleIntegerProperty(serialNumber);
        ItemName = new SimpleStringProperty(itemName);
        PurchaseCategory = new SimpleStringProperty(purchaseCategory);
        Price = new SimpleDoubleProperty(price);
        Amount = new SimpleDoubleProperty(amount);*/
        TotalPrice = price * amount;
        IsFromSale = fromSale;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPurchaseCategory() {
        return purchaseCategory;
    }

    public double getPrice() {
        return price;
    }

    public double getAmount() {
        return amount;
    }

    public double getTotalPrice(){
        return TotalPrice;
    }

    public boolean getIsFromSale(){
        return IsFromSale;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Serial Number: {1}{0}Name: {2}{0}Purchase Category: {3}{0}" +
                        "Price: {4}{0}Amount: {5}{0}Total price(price * amount): {6}{0}",
                System.lineSeparator(),
                serialNumber,
                itemName,
                purchaseCategory,
                String.format("%.2f",price),
                amount,
                TotalPrice);
    }
}
