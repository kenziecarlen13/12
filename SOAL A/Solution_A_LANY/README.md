# Aplikasi Manajemen Tiket Konser LANY - Singleton Implementation

## 1. Deskripsi Proyek
Aplikasi manajemen tiket konser LANY berbasis JavaFX dengan arsitektur dua arah (Admin dan User). Proyek ini mewajibkan penggunaan Design Pattern Singleton untuk pengelolaan koneksi database SQLite (`lany_tickets.db`) serta manajemen sesi (stateful tracking) aktivitas pengguna selama aplikasi berjalan.

## 2. Instruksi Pengerjaan & Daftar File
Selesaikan blok kode `// TODO` pada file-file berikut (Catatan: Pada versi Solution, kode ini sudah terimplementasi sebagai referensi):

- **LanyDBConnection.java**: Implementasikan pola Singleton Thread-Safe dan tulis query JDBC (SELECT, INSERT) untuk validasi login serta pendaftaran user baru.
- **LanySessionManager.java**: Bangun struktur Singleton dan logika perekaman klik tiket ke dalam objek Map. Pastikan data tracking tidak terhapus saat fungsi `logout()` dipanggil.
- **LoginController.java**: Implementasikan otentikasi ganda. Akun `admin`/`admin` sebagai Admin statis, dan akun lain divalidasi ke database sebagai User.
- **LanyInventoryController.java**: Atur proteksi UI (nonaktifkan input/tombol edit jika role adalah 'User') dan hubungkan event listener tabel ke fungsi perekaman klik di `SessionManager`.
- **PieChartController.java**: Sinkronisasikan data grafik agar mengambil sumber data langsung dari objek Map di `SessionManager`.

## 3. Bobot Penilaian (Total 100 Poin)
| Kriteria Penilaian | Bobot |
| :--- | :---: |
| Implementasi Singleton Connection & JDBC Query | 25 Poin |
| Implementasi Singleton Session & Logika Tracking | 20 Poin |
| Logika Autentikasi & Pemisahan Role (Admin/User) | 15 Poin |
| Proteksi Antarmuka (UI) & Integrasi Event Listener | 25 Poin |
| Visualisasi Data Statistik dari Singleton | 15 Poin |

## 4. Skenario Pengujian
- **Kasus 1 (Auth & DB)**: Login sebagai admin statis, logout, lalu daftarkan akun baru via UI. Verifikasi akun baru berhasil login sebagai User.
- **Kasus 2 (Role Protection)**: Saat login sebagai User, pastikan kolom Nama, Harga, Stok, serta tombol Simpan/Hapus dalam kondisi disabled.
- **Kasus 3 (Persistence Singleton)**: Login sebagai User -> Klik beberapa tiket di tabel -> Logout -> Login kembali sebagai Admin -> Buka Grafik. Verifikasi data klik User tadi muncul secara akurat di Pie Chart (Membuktikan data tersimpan di memori Singleton).
