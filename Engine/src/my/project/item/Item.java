package my.project.item;

public class Item {
    protected final int serialNumber;
    protected String itemName;
    protected final ePurchaseCategory purchaseCategory;

    public Item(int serialNumber, String itemName, ePurchaseCategory purchaseCategory) {
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.purchaseCategory = purchaseCategory;
    }

    public Item(Item item) {
        this.serialNumber = item.serialNumber;
        this.itemName = item.itemName;
        this.purchaseCategory = item.purchaseCategory;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ePurchaseCategory getPurchaseCategory() {
        return purchaseCategory;
    }
}
