package my.project.sale;

import dto.SaleDetailsDTO;

public class SaleDetails {
    private final int itemSerialNumber;
    private String itemName;
    private double quantity;
    private double additionalChargePerUnit;

    public SaleDetails(String itemName, int itemSerialNumber, double quantity, double additionalChargePerUnit) {
        this.itemSerialNumber = itemSerialNumber;
        this.quantity = quantity;
        this.additionalChargePerUnit = additionalChargePerUnit;
        this.itemName = itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getAdditionalChargePerUnit() {
        return additionalChargePerUnit;
    }

    public void setAdditionalChargePerUnit(double additionalChargePerUnit) {
        this.additionalChargePerUnit = additionalChargePerUnit;
    }

    public int getItemSerialNumber() {
        return itemSerialNumber;
    }

    public SaleDetailsDTO createSaleDetailsDTO() {
        return new SaleDetailsDTO(itemName, itemSerialNumber, quantity, additionalChargePerUnit);
    }
}
