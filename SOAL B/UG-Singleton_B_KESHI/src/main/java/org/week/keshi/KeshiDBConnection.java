package org.week.keshi;

import java.sql.*;

public class KeshiDBConnection {
    // TODO: Deklarasikan atribut statis privat untuk menyimpan instance dari kelas ini (Singleton).

    public KeshiDBConnection() {
        // TODO: Ubah modifier konstruktor menjadi private untuk mencegah instansiasi eksternal.
    }

    public static Connection getInstance() throws SQLException {
        // TODO: Implementasikan metode Thread-Safe Singleton untuk mengembalikan instance koneksi database. Inisialisasi koneksi JDBC ke database SQLite jika instance bernilai null atau koneksi terputus.
        return null;
    }

    public static void createUsersTable() {
    }

    public static boolean validateUser(String username, String password) {
        // TODO: Tuliskan query SELECT menggunakan PreparedStatement untuk memvalidasi keberadaan username dan password pada tabel users.
        return false;
    }

    public static boolean registerUser(String username, String password) {
        // TODO: Tuliskan query INSERT menggunakan PreparedStatement untuk menambahkan data akun pengguna baru ke tabel users.
        return false;
    }
}
