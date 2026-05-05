package org.week.keshi;

import java.util.HashMap;
import java.util.Map;

public class KeshiSessionManager {
    private static KeshiSessionManager instance;
    private String currentUser;
    private String username;
    private String role;
    private Map<String, Integer> ticketClickCounter = new HashMap<>();

    private KeshiSessionManager() {}

    public static synchronized KeshiSessionManager getInstance() {
        if (instance == null) {
            instance = new KeshiSessionManager();
        }
        return instance;
    }

    public void setCurrentUser(String user) { this.currentUser = user; }
    public String getCurrentUser() { return currentUser; }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public void setRole(String role) { this.role = role; }
    public String getRole() { return role; }

    public void incrementClick(String ticketName) {
        ticketClickCounter.merge(ticketName, 1, Integer::sum);
    }

    public Map<String, Integer> getTicketClickCounter() { return ticketClickCounter; }

    /**
     * Logout: reset session data KECUALI ticketClickCounter
     * agar Admin tetap bisa melihat statistik klik.
     */
    public void logout() {
        this.currentUser = null;
        this.username = null;
        this.role = null;
    }
}
