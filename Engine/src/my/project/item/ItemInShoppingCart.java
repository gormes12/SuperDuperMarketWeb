package my.project.item;

public class ItemInShoppingCart implements ShoppingCartItem{
    private final int serialNumber;
    private final String itemName;
    private final ePurchaseCategory purchaseCategory;
    private final double price;
    private double amount;
//    private boolean isFromSale;

    public ItemInShoppingCart(int serialNumber, String itemName, ePurchaseCategory purchaseCategory, double price, double amount) {
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.purchaseCategory = purchaseCategory;
        this.price = price;
        this.amount = amount;
    }

    public ItemInShoppingCart(SuperMarketItem item, double amount) {
        serialNumber = item.getSerialNumber();
        itemName = item.getItemName();
        purchaseCategory = item.getPurchaseCategory();
        price = item.getPrice();
        this.amount = amount;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalPriceOfItem() {
        return price * amount;
    }

    public String getItemName() {
        return itemName;
    }

    public ePurchaseCategory getPurchaseCategory() {
        return purchaseCategory;
    }

    public boolean isFromSale() {
        return false;
    }
}

