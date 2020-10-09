package my.project.manager;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import my.project.user.User;

import java.util.*;

public class SystemManager {
    private static int userID = 1000;
    public static boolean isInnerInfoChangedInSomeZone = false;
    //    private final StoreManager storeManager = new StoreManager();
    private final ZonesManager zonesManager = new ZonesManager();
    private final UserManager userManager = new UserManager();
    private HashMap<String, ZoneManager> zonesByName = new HashMap<>();

    public void addZone(String zoneName, ZoneManager zone){
        if (zonesByName.putIfAbsent(zoneName, zone) != null){
            throw new ValueException("There is already an area named " + zoneName);
        } else {
            zonesManager.addZonesToManagerList(zone.createZoneDTO());
        }
    }

    public ZoneManager getZone(String zoneName){
        ZoneManager returnZone = zonesByName.getOrDefault(zoneName, null);
        if (returnZone == null){
            throw new ValueException("There is no zone named " + zoneName);
        }

        return returnZone;
    }

    public void addUser(String username, String userType) {
        userManager.addUser(username, userType, userID++);
    }

    public boolean isUserExists(String username) {
        return userManager.isUserExists(username);
    }

    public Collection<User> getUsers() {
        return userManager.getUsers();
    }

    public User.eUserType getUserType(String user) {
        return userManager.getUserType(user);
    }

    public ZonesManager getZonesManager() {
        return zonesManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
