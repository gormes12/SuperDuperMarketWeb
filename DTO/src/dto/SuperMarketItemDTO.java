package dto;

import java.text.MessageFormat;

public class SuperMarketItemDTO {
    private final int serialNumber;
    private final String itemName;
    private final String purchaseCategory;
    private final double price;
    private final double totalItemSold;

    public SuperMarketItemDTO(int serialNumber, String itemName, String purchaseCategory, double price, double totalItemSold) {
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.purchaseCategory = purchaseCategory;
        this.price = price;
        this.totalItemSold = totalItemSold;
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

    public double getTotalItemSold() {
        return totalItemSold;
    }

    public String toString() {
        return MessageFormat.format("Serial Number: {1}{0}Name: {2}{0}" +
                        "Purchase Category: {3}{0}Price: {4}{0}Total Item Sold: {5}{0}",
                System.lineSeparator(),
                getSerialNumber(),
                getItemName(),
                getPurchaseCategory(),
                String.format("%.2f",price),
                totalItemSold);
    }
}
