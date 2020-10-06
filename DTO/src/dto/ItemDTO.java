package dto;

import java.text.MessageFormat;

public class ItemDTO {
    private final int serialNumber;
    private final String itemName;
    private final String purchaseCategory;
    private final int howManyStoreSold;
    private final double averagePrice;
    private final double howManyTimeSold;

    public ItemDTO(int serialNumber, String itemName, String purchaseCategory, int howManyStoreSold, double averagePrice, double howManyTimeSold){
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.purchaseCategory = purchaseCategory;
        this.howManyStoreSold = howManyStoreSold;
        this.averagePrice = averagePrice;
        this.howManyTimeSold = howManyTimeSold;
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

    public int getHowManyStoreSold() {
        return howManyStoreSold;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public double getHowManyTimeSold() {
        return howManyTimeSold;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Serial Number: {1}{0}Name: {2}{0}Purchase Category: {3}{0}" +
                        "The amount of stores who sells this item: {4}{0}Average Price: {5}{0}How many times it sold: {6}{0}",
                System.lineSeparator(),
                serialNumber,
                itemName,
                purchaseCategory,
                howManyStoreSold,
                String.format("%.2f",averagePrice),
                howManyTimeSold);
    }
}
