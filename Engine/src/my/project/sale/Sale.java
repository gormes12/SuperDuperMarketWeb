package my.project.sale;

import dto.SaleDTO;
import dto.SaleDetailsDTO;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class Sale {
    private String saleName;
    private int belongToStoreID;
    private final Pair<Integer, Double> conditionSale;
    private final Pair<String, List<SaleDetails>> details;

    public Sale(String saleName, int itemSerialNumber, double quantityOf, int storeID, String operator) {

        this.saleName = clearLeadingSpacesFromSaleName(saleName);
        conditionSale = new Pair<>(itemSerialNumber, quantityOf);
        details = new Pair<>(operator, new LinkedList<>());
        belongToStoreID = storeID;
    }

    private String clearLeadingSpacesFromSaleName(String saleName) {
        return saleName.trim();
    }

    public Pair<Integer, Double> getConditionSale() {
        return conditionSale;
    }

    public Pair<String, List<SaleDetails>> getDetails() {
        return details;
    }

    public void addSaleDetails(SaleDetails saleDetails) {
        details.getValue().add(saleDetails);
    }

    public void addSaleDetails(String itemName, int itemSerialNumber, double quantity, double additionalPrice) {
        addSaleDetails(new SaleDetails(itemName, itemSerialNumber, quantity, additionalPrice));
    }

    public String getSaleName() {
        return saleName;
    }

    public SaleDTO createSaleDTO() {
        List<SaleDetailsDTO> saleDetailsList = new LinkedList<>();

        for (SaleDetails saleDetails : details.getValue()) {
            saleDetailsList.add(saleDetails.createSaleDetailsDTO());
        }

        return new SaleDTO(saleName, new Pair<>(conditionSale.getKey(), conditionSale.getValue()), new Pair<>(details.getKey(), saleDetailsList), belongToStoreID);
    }

    //    public void removeSaleDetails()
    public enum eSaleOperator {
        OneOf,
        AllOrNothing
    }
}
