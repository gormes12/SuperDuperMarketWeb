package my.project.item;

public interface ShoppingCartItem {

    /*int serialNumber;
    String itemName;
    ePurchaseCategory purchaseCategory;
    double price;
    double amount;
    boolean isFromSale;*/

    /*public ShoppingCartItem(int serialNumber, String itemName, ePurchaseCategory purchaseCategory, double price, double amount) {
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.purchaseCategory = purchaseCategory;
        this.price = price;
        this.amount = amount;
    }*/

    /*public ShoppingCartItem(SuperMarketItem item, double amount) {
        serialNumber = item.getSerialNumber();
        itemName = item.getItemName();
        purchaseCategory = item.getPurchaseCategory();
        price = item.getPrice();
        this.amount = amount;
    }
*/
    int getSerialNumber();/* {
        return serialNumber;
    }*/

    double getAmount();/* {
        return amount;
    }*/

    double getPrice();/* {
        return price;
    }*/

    void setAmount(double amount); /*{
        this.amount = amount;
    }*/

    double getTotalPriceOfItem();/* {
        return price * amount;
    }*/

    String getItemName();/* {
        return itemName;
    }*/

    ePurchaseCategory getPurchaseCategory();/* {
        return purchaseCategory;
    }*/

    boolean isFromSale();
}
