package dto;

public class SaleDetailsDTO {

    private int itemSerialNumber;
    private double quantity;
    private double additionalChargePerUnit;
    private String itemName;

    public SaleDetailsDTO(){

    }

    public void setAdditionalChargePerUnit(double additionalChargePerUnit) {
        this.additionalChargePerUnit = additionalChargePerUnit;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemSerialNumber(int itemSerialNumber) {
        this.itemSerialNumber = itemSerialNumber;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

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
