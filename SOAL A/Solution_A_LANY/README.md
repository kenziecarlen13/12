# UG12: LANY Ticket Promotor

## Deskripsi Singkat
Aplikasi ini merupakan sistem manajemen inventaris tiket konser LANY. Karena integritas data tiket sangat krusial, koneksi ke database dan manajemen sesi login harus terpusat. Anda ditugaskan untuk mengimplementasikan **Singleton Design Pattern** agar akses ke database dan manajemen sesi (login) terpusat, aman, dan tidak menyebabkan kebocoran koneksi atau memori.

## Daftar Tugas

Anda hanya perlu melengkapi dua file yang berada di dalam package `org.week.lany` yang memiliki tag `// TODO`:

### 1. `LanyDBConnection.java`
Tugas Anda adalah merubah kelas ini menjadi Singleton *Thread-Safe* untuk mengelola koneksi SQLite ke database `lany_tickets.db`.
- **Atribut Static**: Buat sebuah atribut `private static Connection instance;`.
- **Modifikasi Konstruktor**: Ubah *modifier* konstruktor menjadi `private` agar class tidak bisa di-instansiasi secara bebas menggunakan kata kunci `new`.
- **Implementasi `getInstance()`**: Lengkapi *method* `public static synchronized Connection getInstance() throws SQLException`. Di dalamnya, periksa apakah `instance` bernilai `null` atau koneksi sudah tertutup (`isClosed()`). Jika ya, inisialisasi koneksi baru dengan `DriverManager.getConnection("jdbc:sqlite:lany_tickets.db")`. Terakhir, kembalikan `instance`.

### 2. `LanySessionManager.java`
Tugas Anda adalah merubah kelas ini menjadi Singleton *Thread-Safe* untuk menyimpan data pengguna yang sedang login.
- **Atribut Static**: Buat sebuah atribut `private static LanySessionManager instance;`.
- **Modifikasi Konstruktor**: Ubah *modifier* konstruktor menjadi `private`.
- **Implementasi `getInstance()`**: Lengkapi *method* `public static synchronized LanySessionManager getInstance()`. Di dalamnya, periksa apakah `instance` bernilai `null`. Jika ya, buat objek `LanySessionManager` baru. Terakhir, kembalikan `instance`.

## Informasi Login
Gunakan kredensial berikut untuk melakukan pengujian saat aplikasi dijalankan:
- **Username**: `admin`
- **Password**: `admin`
