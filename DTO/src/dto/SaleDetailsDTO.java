package dto;

public class SaleDetailsDTO {

    private final int itemSerialNumber;
    private final double quantity;
    private final double additionalChargePerUnit;
    private final String itemName;


    public SaleDetailsDTO(String itemName, int itemSerialNumber, double quantity, double additionalChargePerUnit) {
        this.itemSerialNumber = itemSerialNumber;
        this.quantity = quantity;
        this.additionalChargePerUnit = additionalChargePerUnit;
        this.itemName = itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getAdditionalChargePerUnit() {
        return additionalChargePerUnit;
    }

    public int getItemSerialNumber() {
        return itemSerialNumber;
    }

    public String getItemName() {
        return itemName;
    }
}
