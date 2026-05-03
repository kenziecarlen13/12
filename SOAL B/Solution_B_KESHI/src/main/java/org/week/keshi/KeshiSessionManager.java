package org.week.keshi;

public class KeshiSessionManager {
    private static KeshiSessionManager instance;
    private String currentUser;

    private KeshiSessionManager() {}

    public static synchronized KeshiSessionManager getInstance() {
        if (instance == null) {
            instance = new KeshiSessionManager();
        }
        return instance;
    }
    
    public void setCurrentUser(String user) { this.currentUser = user; }
    public String getCurrentUser() { return currentUser; }
}

