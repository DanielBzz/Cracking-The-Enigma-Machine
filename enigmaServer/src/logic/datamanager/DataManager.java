package logic.datamanager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class DataManager<T> {

    protected final Map<String,T> userNameToData = new HashMap<>();

    public synchronized boolean addUser(String username) {      // maybe change it to exception

        boolean userIsAdded = !isUserExists(username);

        if(userIsAdded) {
            userNameToData.put(username, null);
        }

        return userIsAdded;
    }

    public synchronized void removeUser(String username) {

        userNameToData.remove(username);
    }

    public synchronized Map<String,T> getUsers() {

        return Collections.unmodifiableMap(userNameToData);
    }

    public boolean isUserExists(String username) {

        return userNameToData.containsKey(username);
    }
}
