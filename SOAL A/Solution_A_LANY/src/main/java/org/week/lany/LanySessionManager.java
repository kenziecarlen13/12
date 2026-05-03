package org.week.lany;

public class LanySessionManager {
    private static LanySessionManager instance;
    private String currentUser;

    private LanySessionManager() {}

    public static synchronized LanySessionManager getInstance() {
        if (instance == null) {
            instance = new LanySessionManager();
        }
        return instance;
    }
    
    public void setCurrentUser(String user) { this.currentUser = user; }
    public String getCurrentUser() { return currentUser; }
}

