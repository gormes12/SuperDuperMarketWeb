package dto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.MessageFormat;

public class ShoppingCartItemDTO {
   /* private final int serialNumber;
    private final String itemName;
    private final String purchaseCategory;
    private final double price;
    private double amount;*/

    private SimpleIntegerProperty SerialNumber;
    private SimpleStringProperty ItemName;
    private SimpleStringProperty PurchaseCategory;
    private SimpleDoubleProperty Price;
    private SimpleDoubleProperty Amount;
    private SimpleDoubleProperty TotalPrice;
    private SimpleBooleanProperty IsFromSale;

    public ShoppingCartItemDTO(int serialNumber, String itemName, String purchaseCategory, double price, double amount, boolean fromSale){
        /*this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.purchaseCategory = purchaseCategory;
        this.price = price;
        this.amount = amount;*/
        SerialNumber = new SimpleIntegerProperty(serialNumber);
        ItemName = new SimpleStringProperty(itemName);
        PurchaseCategory = new SimpleStringProperty(purchaseCategory);
        Price = new SimpleDoubleProperty(price);
        Amount = new SimpleDoubleProperty(amount);
        TotalPrice = new SimpleDoubleProperty(price * amount);
        IsFromSale = new SimpleBooleanProperty(fromSale);
    }

    public int getSerialNumber() {
        return SerialNumber.get();
    }

    public String getItemName() {
        return ItemName.get();
    }

    public String getPurchaseCategory() {
        return PurchaseCategory.get();
    }

    public double getPrice() {
        return Price.get();
    }

    public double getAmount() {
        return Amount.get();
    }

    public double getTotalPrice(){
        return TotalPrice.get();
    }

    public boolean getIsFromSale(){
        return IsFromSale.get();
    }

    @Override
    public String toString() {
        return MessageFormat.format("Serial Number: {1}{0}Name: {2}{0}Purchase Category: {3}{0}" +
                        "Price: {4}{0}Amount: {5}{0}Total price(price * amount): {6}{0}",
                System.lineSeparator(),
                SerialNumber.get(),
                ItemName.get(),
                PurchaseCategory.get(),
                String.format("%.2f",Price.get()),
                Amount.get(),
                getTotalPrice());
    }
}
