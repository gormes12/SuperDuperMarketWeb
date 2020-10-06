package my.project.manager;

import dto.StoreDTO;
import dto.ZoneDTO;

import java.util.ArrayList;
import java.util.List;

public class ZonesManager {

    private final List<ZoneDTO> zonesList = new ArrayList<>();

    public synchronized void addZonesToManagerList(ZoneDTO zone){
        zonesList.add(zone);
    }

    public synchronized List<ZoneDTO> getZonesEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > zonesList.size()) {
            fromIndex = 0;
        }
        return zonesList.subList(fromIndex, zonesList.size());
    }

    public int getVersion() {
        return zonesList.size();
    }
}
