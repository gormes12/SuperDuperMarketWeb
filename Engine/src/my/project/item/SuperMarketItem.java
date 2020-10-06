package my.project.item;

import dto.OrderDTO;
import dto.SuperMarketItemDTO;

import java.text.MessageFormat;

public class SuperMarketItem extends Item {
    private double price;
    private double totalItemSold;

    public SuperMarketItem(Item item, double price, double totalItemSold) {
        super(item);
        this.price = price;
        this.totalItemSold = totalItemSold;
    }

    public SuperMarketItem(int serialNumber, String itemName, ePurchaseCategory purchaseCategory, double price, double totalItemSold) {
        super(serialNumber, itemName, purchaseCategory);
        this.price = price;
        this.totalItemSold = totalItemSold;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalItemSold() {
        return totalItemSold;
    }

    public void addAmountToTotalItemSold(double amount){
        totalItemSold += amount;
    }

    public void setTotalItemSold(double totalItemSold) {
        this.totalItemSold = totalItemSold;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Serial Number: {1}{0}Name: {2}{0}" +
                        "Purchase Category: {3}{0} Price(per unit):{4}{0} Total Item Sold: {5}{0}",
                System.lineSeparator(),
                getSerialNumber(),
                getItemName(),
                getPurchaseCategory(),
                price,
                totalItemSold);
    }

    public SuperMarketItemDTO createSuperMarketItemDTO() {
        return new SuperMarketItemDTO(serialNumber, itemName, purchaseCategory.toString(), price, totalItemSold);
    }

}
