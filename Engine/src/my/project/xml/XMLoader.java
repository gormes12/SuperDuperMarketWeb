package my.project.xml;

import my.project.item.Item;
import my.project.item.ePurchaseCategory;
import my.project.sale.SaleDetails;
import my.project.manager.ZoneManager;
import my.project.xml.jaxb.schema.generated.*;
import my.project.location.Location;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class XMLoader {

    private final static String JAXB_XML_PACKAGE_NAME = "my.project.xml.jaxb.schema.generated";
    private static ZoneManager zoneManager;

    public static ZoneManager getZoneFrom(InputStream inputStream, String ownerZoneUsername) throws JAXBException, FileNotFoundException {
        SuperDuperMarketDescriptor superDuperMarketDescriptor = deserializeFrom(inputStream);
        zoneManager = new ZoneManager(superDuperMarketDescriptor.getSDMZone().getName().trim(), ownerZoneUsername);
        tryToCreateValidSystem(superDuperMarketDescriptor, ownerZoneUsername);
        checkIfAllItemsInTheSystemAreOfferedToSell();
        return zoneManager;
    }

    private static void checkIfAllItemsInTheSystemAreOfferedToSell() {
        zoneManager.isThereItemThatNotOfferedForSell();
    }

    private static void tryToCreateValidSystem(SuperDuperMarketDescriptor superDuperMarketDescriptor, String ownerZoneUsername) {
        tryToCreateValidStockItems(superDuperMarketDescriptor.getSDMItems().getSDMItem());
        tryToCreateValidStores(superDuperMarketDescriptor.getSDMStores().getSDMStore(), ownerZoneUsername);
    }

    private static void tryToCreateValidStores(List<SDMStore> sdmStore, String ownerStoreUsername) {
        for (SDMStore store : sdmStore) {
            zoneManager.addStore(ownerStoreUsername ,store.getName(), store.getId(),
                    new Location(store.getLocation().getX(), store.getLocation().getY()), store.getDeliveryPpk());
            tryAddItemsToStore(store.getId(), store.getSDMPrices().getSDMSell());
            if(store.getSDMDiscounts() !=null) {
                tryAddSalesToStore(store.getId(), store.getSDMDiscounts().getSDMDiscount());
            }
        }
    }

    private static void tryAddSalesToStore(int storeID, List<SDMDiscount> sdmDiscount) {
        for (SDMDiscount discount : sdmDiscount){
            List<SaleDetails> detailsList = new LinkedList<>();
            for (SDMOffer details : discount.getThenYouGet().getSDMOffer()){
                String itemName = zoneManager.getNameOfItem(details.getItemId());
                detailsList.add(new SaleDetails(itemName, details.getItemId(), details.getQuantity(), details.getForAdditional()));
            }
            zoneManager.addSaleToStore(storeID,discount.getName(), zoneManager.getNameOfItem(discount.getIfYouBuy().getItemId()), discount.getIfYouBuy().getItemId(), discount.getIfYouBuy().getQuantity(),
                    discount.getThenYouGet().getOperator(), detailsList);
        }
    }

    private static void tryAddItemsToStore(int storeID, List<SDMSell> sdmSell) {
        for (SDMSell item : sdmSell) {
            zoneManager.addItemToStore(storeID, item.getItemId(), item.getPrice());
        }
    }

    private static void tryToCreateValidStockItems(List<SDMItem> sdmItem) {
        for (SDMItem item : sdmItem) {
            ePurchaseCategory purchaseCategory = ePurchaseCategory.Quantity;
            if (item.getPurchaseCategory().equals(ePurchaseCategory.Weight.toString())) {
                purchaseCategory = ePurchaseCategory.Weight;
            }
            zoneManager.addItem(new Item(item.getId(), item.getName(), purchaseCategory));
        }
    }

    private static SuperDuperMarketDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (SuperDuperMarketDescriptor) u.unmarshal(in);
    }
}
