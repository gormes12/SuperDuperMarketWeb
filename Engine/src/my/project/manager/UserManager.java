package my.project.manager;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import my.project.user.Customer;
import my.project.user.StoreOwner;
import my.project.user.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class UserManager {
    private final HashMap<String, User> users;

    public UserManager() {
        users = new HashMap<>();
    }

    public synchronized void addUser(String username, String userType, int id) {
        if (isUserExists(username)) {
            throw new ValueException("User Name: " + username + " already exist!");
        }
        if (userType.equals("StoreOwner")) {
            users.put(username, new StoreOwner(id, username));
        } else{
            users.put(username, new Customer(id, username));
        }
    }

    public synchronized void removeUser(String username) {
        users.remove(username);
    }

    public synchronized Collection<User> getUsers() {
        return Collections.unmodifiableCollection(users.values());
    }

    public boolean isUserExists(String username) {
        return users.containsKey(username);
    }

    public User.eUserType getUserType(String username) {
        if(isUserExists(username)){
            return users.get(username).getUserType();
        } else {
            throw new ValueException("Username " + username + " does not exist");
        }
    }

    public synchronized User getUser(String username) {
        if(isUserExists(username)){
            return users.get(username);
        } else {
            throw new ValueException("Username " + username + " does not exist");
        }
    }
}
