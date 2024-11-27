package service;

import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

public class UserSession {

    private static volatile UserSession instance;

    private final String userName;
    private final String password;
    private final String privileges;

    private UserSession(String userName, String password, String privileges) {
        this.userName = userName;
        this.password = password;
        this.privileges = privileges;
        savePref();
    }
    private  void savePref() {
        synchronized (Preferences.userRoot()) {
            Preferences userPreferences = Preferences.userRoot();
            userPreferences.put("USERNAME", userName);
            userPreferences.put("PASSWORD", password);
            userPreferences.put("PRIVILEGES", privileges);
        }
    }

    public static UserSession getInstance(String userName,String password, String privileges) {
        if(instance == null) {
            synchronized (UserSession.class) {
                if (instance == null) {
                    instance = new UserSession(userName, password, privileges);
                }
            }
        }
        return instance;
    }

    public static UserSession getInstance(String userName,String password) {
        return getInstance(userName, password, "NONE");
    }
    public  String getUserName() {
        return this.userName;
    }

    public  String getPassword() {
        return this.password;
    }

    public  String getPrivileges() {
        return this.privileges;
    }

    @Override
    public  String toString() {
        return "UserSession{" +
                "userName='" + this.userName + '\'' +
                ", privileges=" + this.privileges +
                '}';
    }
}
