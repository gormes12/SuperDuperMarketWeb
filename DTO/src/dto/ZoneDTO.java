package dto;

public class ZoneDTO {

    private final String ownerName;
    private final String zoneName;
    private final int totalItemType;
    private final int totalStores;
    private final int totalOrders;
    private final double avgOrderPrice;

    public ZoneDTO(String ownerName, String zoneName, int totalItemType,
                   int totalStores, int totalOrders, double avgOrderPrice){
        this.ownerName = ownerName;
        this.zoneName = zoneName;
        this.totalItemType = totalItemType;
        this.totalStores = totalStores;
        this.totalOrders = totalOrders;
        this.avgOrderPrice = avgOrderPrice;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getTotalItemType() {
        return totalItemType;
    }

    public int getTotalStores() {
        return totalStores;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getAvgOrderPrice() {
        return avgOrderPrice;
    }
}
