package my.project.manager;

import dto.StoreDTO;
import dto.ZoneDTO;

import java.util.ArrayList;
import java.util.List;

public class ZonesManager {

    private final List<ZoneManager> zonesList = new ArrayList<>();

    public synchronized void addZonesToManagerList(ZoneManager zone){
        zonesList.add(zone);
    }

    public synchronized List<ZoneDTO> getZonesEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > zonesList.size()) {
            fromIndex = 0;
        }

        return convertZoneToDTO(zonesList.subList(fromIndex, zonesList.size()));
    }

    public int getVersion() {
        return zonesList.size();
    }

    private List<ZoneDTO> convertZoneToDTO (List<ZoneManager> zoneManagerList){
        List<ZoneDTO> result = new ArrayList<>();
        for (ZoneManager zoneManager : zoneManagerList){
            result.add(zoneManager.createZoneDTO());
        }

        return result;
    }
}
