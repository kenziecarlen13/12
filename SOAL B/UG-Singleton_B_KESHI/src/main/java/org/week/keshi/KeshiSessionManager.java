package org.week.keshi;

public class KeshiSessionManager {
    public KeshiSessionManager() {}
    // TODO: Gunakan atribut static untuk menyimpan satu-satunya instance dari class ini
    
    private String currentUser;

    // TODO: Implementasikan Singleton untuk Promotor.
    public static KeshiSessionManager getInstance() {
        return null; // TODO: Implement this properly
    }
    
    public void setCurrentUser(String user) { this.currentUser = user; }
    public String getCurrentUser() { return currentUser; }
}

