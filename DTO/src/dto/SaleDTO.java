package dto;

import javafx.util.Pair;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SaleDTO {

    private String saleName;
    private int belongToStoreID;
    private String operator;
    private Pair<Integer, Double> conditionSale;
    private int conditionSaleItemID;
    private double conditionSaleAmount;
    private String saleItemName;
    private String conditionSaleInString;
    private List<SaleDetailsDTO> details;
    private List<String> saleDetailsInListString;
    private String youDeserveSentence;

    public SaleDTO(){

    }

    public SaleDTO(String saleName, String saleItemName, Pair<Integer, Double> conditionSale, Pair<String, List<SaleDetailsDTO>> details, int belongToStoreID) {
        this.saleName = saleName;
        this.saleItemName = saleItemName;
        this.conditionSale = conditionSale;
        this.details = details.getValue();
        this.belongToStoreID = belongToStoreID;
        conditionSaleInString = convertConditionToString();
        saleDetailsInListString = convertSaleDetailsToStrings();
        operator = details.getKey();
        youDeserveSentence = createYouDeserveSentence();
        conditionSaleItemID = conditionSale.getKey();
        conditionSaleAmount = conditionSale.getValue();
    }

    public void setBelongToStoreID(int belongToStoreID) {
        this.belongToStoreID = belongToStoreID;
    }

    public void setConditionSale(Pair<Integer, Double> conditionSale) {
        this.conditionSale = conditionSale;
    }

    public void setConditionSaleInString(String conditionSaleInString) {
        this.conditionSaleInString = conditionSaleInString;
    }

    public void setDetails(List<SaleDetailsDTO> details) {
        this.details = details;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setSaleDetailsInListString(List<String> saleDetailsInListString) {
        this.saleDetailsInListString = saleDetailsInListString;
    }

    public void setSaleItemName(String saleItemName) {
        this.saleItemName = saleItemName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public void setYouDeserveSentence(String youDeserveSentence) {
        this.youDeserveSentence = youDeserveSentence;
    }

    public String getSaleItemName() {
        return saleItemName;
    }

    public Pair<Integer, Double> getConditionSale() {
        return conditionSale;
    }

    public List<SaleDetailsDTO> getDetails() {
        return details;
    }

    public String convertConditionToString(){
        return MessageFormat.format("You Bought {0} {2} of {3} (Serial No. {1})", conditionSale.getKey(), conditionSale.getValue(),conditionSale.getKey() == 1? "unit" : "units", saleItemName);
    }

    public String getOperator(){
        return operator;
    }

    private String createYouDeserveSentence(){
        if (operator.equals("ONE-OF")){
            return ("You deserve to execute one of the following:");
        } else if (operator.equals("ALL-OR-NOTHING")){
            return ("You deserve to execute all the following:");
        } else {
            return ("You deserve to execute:");
        }
    }

    public List<String> convertSaleDetailsToStrings(){
        List<String> saleDetails =  new LinkedList<>();

        for(SaleDetailsDTO saleItem : details) {
            saleDetails.add(MessageFormat.format("{0} {3} of {4} ( Serial No. {1} ) for {2}",
                    saleItem.getQuantity(), saleItem.getItemSerialNumber(),
                    saleItem.getAdditionalChargePerUnit() == 0? "free" : String.format("%.2f",saleItem.getAdditionalChargePerUnit()) + " ₪ per unit",
                    saleItem.getQuantity() ==1? "unit" : "units",
                    saleItem.getItemName()));
        }

        return saleDetails;
    }

    public String getSaleName() {
        return saleName;
    }

    public SaleDetailsDTO getSpecificSaleDetailsOnItemFromSale(int itemSerialNumber){
        for(SaleDetailsDTO saleDetails : details){
            if(saleDetails.getItemSerialNumber() == itemSerialNumber){
                return saleDetails;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return saleName;
    }

    public int getBelongToStoreID() {
        return belongToStoreID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleDTO saleDTO = (SaleDTO) o;
        return saleName.equals(saleDTO.getSaleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleName, belongToStoreID, conditionSale, details);
    }

    public String getConditionSaleInString() {
        return conditionSaleInString;
    }

    public List<String> getSaleDetailsInListString() {
        return saleDetailsInListString;
    }

    public String getYouDeserveSentence() {
        return youDeserveSentence;
    }

    public int getConditionSaleItemID() {
        return conditionSaleItemID;
    }

    public void setConditionSaleItemID(int conditionSaleItemID) {
        this.conditionSaleItemID = conditionSaleItemID;
    }

    public double getConditionSaleAmount() {
        return conditionSaleAmount;
    }

    public void setConditionSaleAmount(double conditionSaleAmount) {
        this.conditionSaleAmount = conditionSaleAmount;
    }
}
