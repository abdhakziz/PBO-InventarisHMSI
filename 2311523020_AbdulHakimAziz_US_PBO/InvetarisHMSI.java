// Mengimpor library SQL untuk koneksi database MySQL
import java.sql.*;
// Mengimpor library Collection untuk bekerja dengan List, Set, Map
import java.util.*;
// Mengimpor class Date dari java.util untuk menangani tanggal
import java.util.Date;
// Mengimpor LocalDate untuk manipulasi tanggal modern
import java.time.LocalDate;
// Mengimpor DateTimeFormatter untuk format tanggal
import java.time.format.DateTimeFormatter;

/**
 * FILE: InvetarisHMSI.java
 * TEMA: Aplikasi Inventaris HMSI
 * DESKRIPSI: Program untuk mengelola inventaris barang HMSI dengan fitur CRUD
 */

public class InvetarisHMSI {

    // ========== KONFIGURASI DATABASE ==========
    // URL koneksi ke database MySQL di localhost dengan nama database db_inventaris_hmsi
    static final String DB_URL = "jdbc:mysql://localhost:3306/db_inventaris_hmsi";
    // Username untuk login ke database MySQL (default: root)
    static final String USER = "root";
    // Password untuk login ke database MySQL (kosong untuk user root)
    static final String PASS = "";

    // Membuat object Scanner untuk menerima input dari user melalui keyboard
    static Scanner scanner = new Scanner(System.in);

    // Method utama - titik awal eksekusi program
    public static void main(String[] args) {
        // Loop tak terbatas untuk menampilkan menu berulang-ulang
        while (true) {
            // Menampilkan header aplikasi
            System.out.println("\n=== APLIKASI INVENTARIS HMSI ===");
            // Menampilkan opsi menu Create
            System.out.println("1. Tambah Barang (Create)");
            // Menampilkan opsi menu Read
            System.out.println("2. Lihat Daftar Barang (Read)");
            // Menampilkan opsi menu Update
            System.out.println("3. Update Jumlah Barang (Update)");
            // Menampilkan opsi menu Delete
            System.out.println("4. Hapus Barang (Delete)");
            // Menampilkan opsi untuk keluar dari aplikasi
            System.out.println("5. Keluar");
            // Menampilkan prompt untuk memasukkan pilihan menu
            System.out.print("Pilih menu: ");

            // Blok try-catch untuk menangani input yang tidak valid
            try {
                // Membaca input dari user dan mengonversi ke tipe integer
                int pilihan = Integer.parseInt(scanner.nextLine());
                // Switch statement untuk menentukan aksi berdasarkan pilihan user
                switch (pilihan) {
                    // Jika user memilih 1, panggil method tambahBarang()
                    case 1:
                        tambahBarang();
                        break;
                    // Jika user memilih 2, panggil method lihatBarang()
                    case 2:
                        lihatBarang();
                        break;
                    // Jika user memilih 3, panggil method updateBarang()
                    case 3:
                        updateBarang();
                        break;
                    // Jika user memilih 4, panggil method hapusBarang()
                    case 4:
                        hapusBarang();
                        break;
                    // Jika user memilih 5, keluar dari aplikasi
                    case 5:
                        System.out.println("Terima kasih.");
                        System.exit(0);
                    // Jika pilihan tidak ada di antara 1-5, tampilkan pesan error
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                // Menangani exception ketika user memasukkan input yang bukan angka
                System.out.println("Error: Mohon masukkan angka saja!");
            }
        }
    }

    // ========== METHOD CREATE - TAMBAH BARANG BARU ==========
    static void tambahBarang() {
        // Menampilkan header input barang baru
        System.out.println("\n--- Input Barang Baru ---");

        // ========== STEP 1: INPUT ID BARANG ==========
        // Deklarasi variabel untuk menyimpan ID barang
        int idBarang = 0;
        // Blok try-catch untuk validasi input ID
        try {
            // Menampilkan prompt untuk memasukkan ID barang
            System.out.print("ID Barang: ");
            // Membaca input dan mengonversi ke tipe integer
            idBarang = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // Jika input bukan angka, tampilkan pesan error dan keluar dari method
            System.out.println("Input ID Barang salah! ID harus berupa angka.");
            return;
        }

        // ========== STEP 2: INPUT NAMA BARANG ==========
        // Menampilkan prompt untuk memasukkan nama barang
        System.out.print("Nama Barang: ");
        // Membaca input nama barang dari user
        String namaBarang = scanner.nextLine();

        // ========== STEP 3: INPUT JUMLAH BARANG ==========
        // Deklarasi variabel untuk menyimpan jumlah barang
        int jumlah = 0;
        // Blok try-catch untuk validasi input jumlah
        try {
            // Menampilkan prompt untuk memasukkan jumlah
            System.out.print("Jumlah: ");
            // Membaca input dan mengonversi ke tipe integer
            jumlah = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // Jika input bukan angka, atur jumlah ke default 1
            System.out.println("Input jumlah salah, set ke default 1.");
            jumlah = 1;
        }

        // ========== STEP 4: INPUT KONDISI BARANG ==========
        // Menampilkan prompt untuk memilih kondisi barang
        System.out.print("Kondisi (1. Baik / 2. Rusak Ringan / 3. Rusak Berat): ");
        // Deklarasi variabel untuk menyimpan pilihan kondisi
        int kondisiPilih = 0;
        // Blok try-catch untuk validasi input kondisi
        try {
            // Membaca input dan mengonversi ke tipe integer
            kondisiPilih = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            // Jika ada error, atur ke default 1 (Baik)
            kondisiPilih = 1;
        }

        // ========== STEP 5: KONVERSI PILIHAN KONDISI KE STRING ==========
        // Deklarasi variabel untuk menyimpan deskripsi kondisi
        String kondisi;
        // Switch statement untuk mengonversi pilihan ke string kondisi
        switch (kondisiPilih) {
            // Jika pilihan 2, set kondisi menjadi "RUSAK RINGAN"
            case 2:
                kondisi = "RUSAK RINGAN";
                break;
            // Jika pilihan 3, set kondisi menjadi "RUSAK BERAT"
            case 3:
                kondisi = "RUSAK BERAT";
                break;
            // Untuk pilihan lainnya (default), set kondisi menjadi "BAIK"
            default:
                kondisi = "BAIK";
                break;
        }

        // ========== STEP 6: MENDAPATKAN TANGGAL HARI INI ==========
        // Membuat object LocalDate untuk mendapatkan tanggal hari ini
        LocalDate today = LocalDate.now();

        // ========== STEP 7: KONEKSI DAN INSERT DATA KE DATABASE ==========
        // Blok try-catch untuk menangani error koneksi dan SQL
        try {
            // Memuat driver MySQL JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuat koneksi ke database dengan try-with-resource untuk auto-close
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                // Query SQL untuk memasukkan data barang baru ke tabel inventaris
                String sql = "INSERT INTO inventaris (id_barang, nama_barang, jumlah, kondisi, tanggal_masuk) VALUES (?, ?, ?, ?, ?)";

                // Membuat PreparedStatement untuk mencegah SQL injection
                PreparedStatement pstmt = conn.prepareStatement(sql);
                // Menetapkan parameter ID barang (parameter ke-1)
                pstmt.setInt(1, idBarang);
                // Menetapkan parameter nama barang (parameter ke-2)
                pstmt.setString(2, namaBarang);
                // Menetapkan parameter jumlah barang (parameter ke-3)
                pstmt.setInt(3, jumlah);
                // Menetapkan parameter kondisi barang (parameter ke-4)
                pstmt.setString(4, kondisi);
                // Menetapkan parameter tanggal masuk (parameter ke-5)
                pstmt.setDate(5, java.sql.Date.valueOf(today));

                // Menjalankan query INSERT untuk memasukkan data ke database
                pstmt.executeUpdate();
                // Menampilkan pesan sukses beserta ID barang yang disimpan
                System.out.println(
                        ">> Berhasil disimpan! ID Barang: " + idBarang);
            }
        } catch (ClassNotFoundException e) {
            // Menangani exception ketika driver MySQL tidak ditemukan
            System.out.println("ERROR DRIVER: File .jar MySQL belum terbaca!");
        } catch (SQLException e) {
            // Menangani exception ketika ada error pada koneksi atau query database
            System.out.println("Gagal koneksi database: " + e.getMessage());
        }
    }

    // ========== METHOD READ - LIHAT DAFTAR BARANG ==========
    static void lihatBarang() {
        // Membuat ArrayList untuk menyimpan log audit (untuk menghitung jumlah data)
        List<String> auditLog = new ArrayList<>();

        // Blok try-catch untuk menangani error koneksi dan SQL
        try {
            // Memuat driver MySQL JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuat koneksi ke database dengan try-with-resource untuk auto-close
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    // Membuat Statement untuk menjalankan query
                    Statement stmt = conn.createStatement();
                    // Menjalankan query SELECT untuk mengambil semua data dari tabel inventaris
                    ResultSet rs = stmt.executeQuery("SELECT * FROM inventaris")) {

                // Menampilkan header tabel
                System.out.println("\n--- Daftar Inventaris HMSI ---");
                // Menampilkan kolom-kolom tabel dengan format yang rapi
                System.out.printf("%-10s %-25s %-8s %-15s %-15s \n", "ID_Barang", "Nama Barang",
                        "Jumlah", "Kondisi", "Tgl Masuk");
                // Menampilkan garis pemisah untuk memisahkan header dengan data
                System.out.println(
                        "--------------------------------------------------------------------------------------------------------------");

                // Deklarasi flag untuk mendeteksi apakah ada data yang ditampilkan
                boolean adaData = false;
                // Looping untuk membaca setiap baris data dari hasil query
                while (rs.next()) {
                    // Set flag menjadi true karena ada data yang dibaca
                    adaData = true;
                    // Mengambil nilai ID barang dari kolom "id_barang"
                    int idBarang = rs.getInt("id_barang");
                    // Mengambil nilai nama barang dari kolom "nama_barang"
                    String namaBarang = rs.getString("nama_barang");
                    // Mengambil nilai jumlah dari kolom "jumlah"
                    int jumlah = rs.getInt("jumlah");
                    // Mengambil nilai kondisi dari kolom "kondisi"
                    String kondisi = rs.getString("kondisi");
                    // Mengambil nilai tanggal masuk dari kolom "tanggal_masuk"
                    java.sql.Date tglMasuk = rs.getDate("tanggal_masuk");

                    // Menampilkan data dalam format tabel yang rapi menggunakan printf
                    System.out.printf("%-10d %-25s %-8d %-15s %-15s\n", idBarang, namaBarang,
                            jumlah, kondisi, tglMasuk);
                    // Menambahkan log untuk setiap data yang ditampilkan
                    auditLog.add("Data ID Barang " + idBarang + " ditampilkan.");
                }

                // Mengecek apakah ada data atau tidak
                if (!adaData) {
                    // Jika tidak ada data, tampilkan pesan ini
                    System.out.println("Belum ada data inventaris.");
                } else {
                    // Jika ada data, tampilkan jumlah total barang yang ditampilkan
                    System.out.println("Total barang: " + auditLog.size());
                }
            }
        } catch (ClassNotFoundException e) {
            // Menangani exception ketika driver MySQL tidak ditemukan
            System.out.println("ERROR DRIVER: File .jar MySQL belum terbaca!");
        } catch (SQLException e) {
            // Menangani exception ketika ada error pada koneksi atau query database
            e.printStackTrace();
        }
    }

    // ========== METHOD UPDATE - EDIT DATA BARANG ==========
    static void updateBarang() {
        // Blok try-catch untuk menangani error koneksi dan SQL
        try {
            // Memuat driver MySQL JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuat koneksi ke database dengan try-with-resource untuk auto-close
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                // Menampilkan prompt untuk memasukkan ID barang yang akan diedit
                System.out.print("Masukkan ID Barang yang akan diedit: ");
                // Membaca input ID barang dari user
                String inputId = scanner.nextLine();
                // Mengonversi input ke tipe integer
                int idBarang = Integer.parseInt(inputId);

                // Menampilkan prompt untuk memasukkan jumlah barang yang baru
                System.out.print("Masukkan Jumlah Baru: ");
                // Membaca input dan mengonversi ke tipe integer untuk jumlah baru
                int jumlahBaru = Integer.parseInt(scanner.nextLine());

                // Query SQL untuk mengupdate data jumlah barang berdasarkan ID
                String sql = "UPDATE inventaris SET jumlah=? WHERE id_barang=?";
                // Membuat PreparedStatement untuk mencegah SQL injection
                PreparedStatement pstmt = conn.prepareStatement(sql);
                // Menetapkan parameter jumlah barang baru (parameter ke-1)
                pstmt.setInt(1, jumlahBaru);
                // Menetapkan parameter ID barang (parameter ke-2)
                pstmt.setInt(2, idBarang);

                // Menjalankan query UPDATE dan menyimpan jumlah baris yang terpengaruh
                int affectedRows = pstmt.executeUpdate();
                // Mengecek apakah ada baris yang terpengaruh oleh update
                if (affectedRows > 0) {
                    // Jika ada baris yang ter-update, tampilkan pesan sukses
                    System.out.println(">> Data berhasil diperbarui!");
                } else {
                    // Jika tidak ada baris yang ter-update, berarti ID tidak ditemukan
                    System.out.println(">> ID Barang tidak ditemukan.");
                }
            }
        } catch (ClassNotFoundException e) {
            // Menangani exception ketika driver MySQL tidak ditemukan
            System.out.println("ERROR DRIVER: File .jar MySQL belum terbaca!");
        } catch (Exception e) {
            // Menangani exception lainnya seperti NumberFormatException atau SQLException
            System.out.println("Error Update: " + e.getMessage());
        }
    }

    // ========== METHOD DELETE - HAPUS DATA BARANG ==========
    static void hapusBarang() {
        // Blok try-catch untuk menangani error koneksi dan SQL
        try {
            // Memuat driver MySQL JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuat koneksi ke database dengan try-with-resource untuk auto-close
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                // Menampilkan prompt untuk memasukkan ID barang yang akan dihapus
                System.out.print("Masukkan ID Barang yang akan dihapus: ");
                // Membaca input dan mengonversi ke tipe integer untuk ID barang
                int idBarang = Integer.parseInt(scanner.nextLine());

                // Query SQL untuk menghapus data barang berdasarkan ID
                String sql = "DELETE FROM inventaris WHERE id_barang=?";
                // Membuat PreparedStatement untuk mencegah SQL injection
                PreparedStatement pstmt = conn.prepareStatement(sql);
                // Menetapkan parameter ID barang (parameter ke-1)
                pstmt.setInt(1, idBarang);

                // Menjalankan query DELETE dan menyimpan jumlah baris yang terpengaruh
                int affectedRows = pstmt.executeUpdate();
                // Mengecek apakah ada baris yang terpengaruh oleh delete
                if (affectedRows > 0) {
                    // Jika ada baris yang terhapus, tampilkan pesan sukses
                    System.out.println(">> Data berhasil dihapus!");
                } else {
                    // Jika tidak ada baris yang terhapus, berarti ID tidak ditemukan
                    System.out.println(">> ID Barang tidak ditemukan.");
                }
            }
        } catch (ClassNotFoundException e) {
            // Menangani exception ketika driver MySQL tidak ditemukan
            System.out.println("ERROR DRIVER: File .jar MySQL belum terbaca!");
        } catch (SQLException e) {
            // Menangani exception ketika ada error pada koneksi atau query database
            System.out.println("Error Delete: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Menangani exception ketika input ID bukan angka
            System.out.println("ID Barang harus angka!");
        }
    }
}

// ========== CLASS PENDUKUNG - INVENTARIS HMSI ==========
// Kelas-kelas berikut adalah struktur pendukung untuk kategorisasi barang inventaris

// Interface untuk mendefinisikan operasi barang
interface OperasiBarang {
    // Method abstrak untuk mendapatkan deskripsi barang
    String getDeskripsi();
}

// Kelas abstrak yang mengimplementasikan interface OperasiBarang
// Kelas ini berfungsi sebagai parent class untuk semua jenis barang
abstract class BarangSekre implements OperasiBarang {
    // Variabel untuk menyimpan nama barang
    protected String namaBarang;

    // Constructor untuk menginisialisasi nama barang
    public BarangSekre(String namaBarang) {
        // Menetapkan nama barang melalui parameter constructor
        this.namaBarang = namaBarang;
    }
}

// Kelas untuk Alat Optik yang mewarisi dari BarangSekre
class AlatOptik extends BarangSekre {
    // Constructor untuk menginisialisasi alat optik dengan nama barang
    public AlatOptik(String namaBarang) {
        // Memanggil constructor parent class dengan parameter nama barang
        super(namaBarang);
    }

    // Override method getDeskripsi dari interface OperasiBarang
    @Override
    public String getDeskripsi() {
        // Mengembalikan deskripsi untuk kategori alat optik
        return "Alat optik untuk pengamatan (mikroskop, lup, dll)";
    }
}

// Kelas untuk Alat Gelas yang mewarisi dari BarangSekre
class AlatGelas extends BarangSekre {
    // Constructor untuk menginisialisasi alat gelas dengan nama barang
    public AlatGelas(String namaBarang) {
        // Memanggil constructor parent class dengan parameter nama barang
        super(namaBarang);
    }

    // Override method getDeskripsi dari interface OperasiBarang
    @Override
    public String getDeskripsi() {
        // Mengembalikan deskripsi untuk kategori alat gelas
        return "Peralatan gelas HMSI (beaker, tabung reaksi, dll)";
    }
}

// Kelas untuk Peralatan Elektronik yang mewarisi dari BarangSekre
class Elektronik extends BarangSekre {
    // Constructor untuk menginisialisasi elektronik dengan nama barang
    public Elektronik(String namaBarang) {
        // Memanggil constructor parent class dengan parameter nama barang
        super(namaBarang);
    }

    // Override method getDeskripsi dari interface OperasiBarang
    @Override
    public String getDeskripsi() {
        // Mengembalikan deskripsi untuk kategori peralatan elektronik
        return "Peralatan elektronik (komputer, oscilloscope, dll)";
    }
}

// Kelas untuk Alat Ukur yang mewarisi dari BarangSekre
class AlatUkur extends BarangSekre {
    // Constructor untuk menginisialisasi alat ukur dengan nama barang
    public AlatUkur(String namaBarang) {
        // Memanggil constructor parent class dengan parameter nama barang
        super(namaBarang);
    }

    // Override method getDeskripsi dari interface OperasiBarang
    @Override
    public String getDeskripsi() {
        // Mengembalikan deskripsi untuk kategori alat ukur
        return "Alat pengukuran (timbangan, penggaris, jangka sorong, dll)";
    }
}