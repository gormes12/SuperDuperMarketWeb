package my.project.manager;

import dto.StoreDTO;

import java.util.ArrayList;
import java.util.List;

public class StoreManager {
    private final List<StoreDTO> storesList = new ArrayList<>();

    public synchronized void addStoresToManagerList(List<StoreDTO> stores){
        storesList.addAll(stores);
    }

    public synchronized List<StoreDTO> getStoresEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > storesList.size()) {
            fromIndex = 0;
        }
        return storesList.subList(fromIndex, storesList.size());
    }

    public int getVersion() {
        return storesList.size();
    }
}
