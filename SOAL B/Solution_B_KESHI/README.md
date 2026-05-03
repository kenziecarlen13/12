# UG12: KESHI "Requiem" Tour - Ticket Inventory Management

Aplikasi ini adalah sistem manajemen inventaris tiket konser KESHI "Requiem" Tour. Integritas data ketersediaan kursi (*availability*) sangat krusial. Oleh karena itu, Anda ditugaskan untuk mengimplementasikan **Singleton Design Pattern** agar akses ke database dan manajemen sesi (login) terpusat, aman, dan tidak terjadi duplikasi koneksi yang memboroskan memori.

## Tujuan Praktikum
Memahami dan mengimplementasikan **Singleton Design Pattern** secara *Thread-Safe* pada Java.

## Apa yang Harus Dikerjakan?

Anda HANYA perlu melengkapi dua kelas Java yang berada di dalam *package* org.week.keshi. Perhatikan petunjuk // TODO di dalam file berikut:

### 1. KeshiDBConnection.java (Manajemen Database)
Tugas Anda adalah merubah kelas ini menjadi Singleton untuk mengelola koneksi SQLite ke keshi_tickets.db.
- **Ubah Konstruktor**: Ganti *modifier* konstruktor menjadi private agar tidak bisa di-instansiasi dengan perintah 
ew dari luar kelas.
- **Buat Atribut Static**: Buat sebuah variabel private static Connection instance;.
- **Lengkapi getInstance()**: Implementasikan *method* public static synchronized Connection getInstance() throws SQLException. 
  - Jika instance bernilai 
ull atau koneksi sudah tertutup (isClosed()), buat koneksi baru menggunakan DriverManager.getConnection("jdbc:sqlite:keshi_tickets.db").
  - Kembalikan nilai instance tersebut.

### 2. KeshiSessionManager.java (Manajemen Sesi Login Promotor)
Tugas Anda adalah merubah kelas ini menjadi Singleton untuk menyimpan *state* sesi pengguna yang sedang *login*.
- **Ubah Konstruktor**: Ganti *modifier* konstruktor menjadi private.
- **Buat Atribut Static**: Buat sebuah variabel private static KeshiSessionManager instance;.
- **Lengkapi getInstance()**: Implementasikan *method* public static synchronized KeshiSessionManager getInstance().
  - Jika instance bernilai 
ull, inisialisasi dengan 
ew KeshiSessionManager().
  - Kembalikan nilai instance tersebut.

## Catatan Tambahan
- Pastikan Anda menggunakan kata kunci synchronized pada *method* getInstance() agar implementasi Singleton Anda bersifat *Thread-Safe*.
- **Jangan mengubah** baris kode atau kelas lain selain bagian // TODO pada kedua file yang disebutkan di atas.
- Untuk menguji program yang Anda lengkapi, masukkan Username admin dan Password admin saat Login.
