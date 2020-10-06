package dto;

import javafx.util.Pair;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SaleDTO {

    private final String saleName;
    private final int belongToStoreID;
    private final Pair<Integer, Double> conditionSale;
    private final Pair<String, List<SaleDetailsDTO>> details;

    public SaleDTO(String saleName, Pair<Integer, Double> conditionSale, Pair<String, List<SaleDetailsDTO>> details, int belongToStoreID) {
        this.saleName = saleName;
        this.conditionSale = conditionSale;
        this.details = details;
        this.belongToStoreID = belongToStoreID;
    }

    public Pair<Integer, Double> getConditionSale() {
        return conditionSale;
    }

    public Pair<String, List<SaleDetailsDTO>> getDetails() {
        return details;
    }

    public String convertConditionToString(){
        return MessageFormat.format("Buy {0} from item id: {1}", conditionSale.getKey(), conditionSale.getValue());
    }

    public String getOperator(){
        return details.getKey();
    }

    /*public String convertSaleDetailsToString(){
        String saleDetails = "And get ";
        if(details.getKey().equals("IRRELEVANT")) {
            SaleDetailsDTO saleItem = details.getValue().get(0);
            saleDetails = saleDetails.concat(MessageFormat.format("{0} from item id: {1} for {2} SHEKEL per unit.",
                    saleItem.getQuantity(), saleItem.getItemSerialNumber(), saleItem.getAdditionalChargePerUnit()));
        }else if(details.getKey().equals("ONE-OF")){
            for(SaleDetailsDTO saleItem : details.getValue()) {
                saleDetails = saleDetails.concat(MessageFormat.format("{0} from item id: {1} for {2} SHEKEL per unit ",
                        saleItem.getQuantity(), saleItem.getItemSerialNumber(), saleItem.getAdditionalChargePerUnit()));
                if(saleItem!=details.getValue().get(details.getValue().size() - 1)){
                    saleDetails = saleDetails.concat("Or get ");
                }
            }
        }else{
            double sum;
        }

        return saleDetails;
    }*/

    public List<String> convertSaleDetailsToStrings(){
        List<String> saleDetails =  new LinkedList<>(); //"And get ";
        saleDetails.add("And Get");

        for(SaleDetailsDTO saleItem : details.getValue()) {
            saleDetails.add(MessageFormat.format("{0} {3} from item id: {1} for {2}",
                    saleItem.getQuantity(), saleItem.getItemSerialNumber(),
                    saleItem.getAdditionalChargePerUnit() == 0? "free" : String.format("%.2f",saleItem.getAdditionalChargePerUnit()) + " SHEKEL per unit",
                    saleItem.getQuantity() ==1? "unit" : "units"));
        }

        return saleDetails;
    }

    public String getSaleName() {
        return saleName;
    }

    public SaleDetailsDTO getSpecificSaleDetailsOnItemFromSale(int itemSerialNumber){
        for(SaleDetailsDTO saleDetails : details.getValue()){
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
}
