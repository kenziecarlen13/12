package org.week.keshi;

import java.util.HashMap;
import java.util.Map;

public class KeshiSessionManager {
    private String currentUser;
    private String username;
    private String role;
    private Map<String, Integer> ticketClickCounter = new HashMap<>();

    public KeshiSessionManager() {
    }

    public static KeshiSessionManager getInstance() {
        // TODO: Implementasikan metode Singleton untuk mengembalikan instance dari SessionManager.
        return null;
    }

    public void setCurrentUser(String user) { this.currentUser = user; }
    public String getCurrentUser() { return currentUser; }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public void setRole(String role) { this.role = role; }
    public String getRole() { return role; }

    public void incrementClick(String ticketName) {
        // TODO: Implementasikan logika untuk menambahkan jumlah klik berdasarkan nama tiket ke dalam struktur data Map (ticketClickCounter).
    }

    public Map<String, Integer> getTicketClickCounter() { return ticketClickCounter; }

    public void logout() {
        // TODO: Implementasikan logika pembersihan sesi pengguna (logout). Pastikan data ticketClickCounter tidak ikut terhapus agar histori klik dapat divisualisasikan oleh Admin.
    }
}
