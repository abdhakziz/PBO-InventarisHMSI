import java.sql.*;
import java.util.*;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * FILE: InvetarisHMSI.java
 * TEMA: Aplikasi Inventaris HMSI
 */

public class InvetarisHMSI {

    // Konfigurasi Database
    static final String DB_URL = "jdbc:mysql://localhost:3306/db_inventaris_hmsi";
    static final String USER = "root";
    static final String PASS = "";

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== APLIKASI INVENTARIS HMSI ===");
            System.out.println("1. Tambah Barang (Create)");
            System.out.println("2. Lihat Daftar Barang (Read)");
            System.out.println("3. Update Jumlah Barang (Update)");
            System.out.println("4. Hapus Barang (Delete)");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");

            try {
                int pilihan = Integer.parseInt(scanner.nextLine());
                switch (pilihan) {
                    case 1:
                        tambahBarang();
                        break;
                    case 2:
                        lihatBarang();
                        break;
                    case 3:
                        updateBarang();
                        break;
                    case 4:
                        hapusBarang();
                        break;
                    case 5:
                        System.out.println("Terima kasih.");
                        System.exit(0);
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Mohon masukkan angka saja!");
            }
        }
    }

    // --- METODE CREATE ---
    static void tambahBarang() {
        System.out.println("\n--- Input Barang Baru ---");

        // Input ID Barang
        int idBarang = 0;
        try {
            System.out.print("ID Barang: ");
            idBarang = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input ID Barang salah! ID harus berupa angka.");
            return;
        }

        System.out.print("Nama Barang: ");
        String namaBarang = scanner.nextLine();


        int jumlah = 0;
        try {
            System.out.print("Jumlah: ");
            jumlah = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input jumlah salah, set ke default 1.");
            jumlah = 1;
        }

        System.out.print("Kondisi (1. Baik / 2. Rusak Ringan / 3. Rusak Berat): ");
        int kondisiPilih = 0;
        try {
            kondisiPilih = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            kondisiPilih = 1;
        }

        String kondisi;
        switch (kondisiPilih) {
            case 2:
                kondisi = "RUSAK RINGAN";
                break;
            case 3:
                kondisi = "RUSAK BERAT";
                break;
            default:
                kondisi = "BAIK";
                break;
        }


        LocalDate today = LocalDate.now();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "INSERT INTO inventaris (id_barang, nama_barang, jumlah, kondisi, tanggal_masuk) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, idBarang);
                pstmt.setString(2, namaBarang);
                pstmt.setInt(3, jumlah);
                pstmt.setString(4, kondisi);
                pstmt.setDate(5, java.sql.Date.valueOf(today));

                pstmt.executeUpdate();
                System.out.println(
                        ">> Berhasil disimpan! ID Barang: " + idBarang);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR DRIVER: File .jar MySQL belum terbaca!");
        } catch (SQLException e) {
            System.out.println("Gagal koneksi database: " + e.getMessage());
        }
    }

    // --- METODE READ ---
    static void lihatBarang() {
        List<String> auditLog = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM inventaris")) {

                System.out.println("\n--- Daftar Inventaris HMSI ---");
                System.out.printf("%-10s %-25s %-8s %-15s %-15s \n", "ID_Barang", "Nama Barang",
                        "Jumlah", "Kondisi", "Tgl Masuk");
                System.out.println(
                        "--------------------------------------------------------------------------------------------------------------");

                boolean adaData = false;
                while (rs.next()) {
                    adaData = true;
                    int idBarang = rs.getInt("id_barang");
                    String namaBarang = rs.getString("nama_barang");
                    int jumlah = rs.getInt("jumlah");
                    String kondisi = rs.getString("kondisi");
                    java.sql.Date tglMasuk = rs.getDate("tanggal_masuk");

                    System.out.printf("%-10d %-25s %-8d %-15s %-15s\n", idBarang, namaBarang,
                            jumlah, kondisi, tglMasuk);
                    auditLog.add("Data ID Barang " + idBarang + " ditampilkan.");
                }

                if (!adaData) {
                    System.out.println("Belum ada data inventaris.");
                } else {
                    System.out.println("Total barang: " + auditLog.size());
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR DRIVER: File .jar MySQL belum terbaca!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- METODE UPDATE ---
    static void updateBarang() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                System.out.print("Masukkan ID Barang yang akan diedit: ");
                String inputId = scanner.nextLine();
                int idBarang = Integer.parseInt(inputId);

                System.out.print("Masukkan Jumlah Baru: ");
                int jumlahBaru = Integer.parseInt(scanner.nextLine());

                String sql = "UPDATE inventaris SET jumlah=? WHERE id_barang=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, jumlahBaru);
                pstmt.setInt(2, idBarang);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println(">> Data berhasil diperbarui!");
                } else {
                    System.out.println(">> ID Barang tidak ditemukan.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR DRIVER: File .jar MySQL belum terbaca!");
        } catch (Exception e) {
            System.out.println("Error Update: " + e.getMessage());
        }
    }

    // --- METODE DELETE ---
    static void hapusBarang() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                System.out.print("Masukkan ID Barang yang akan dihapus: ");
                int idBarang = Integer.parseInt(scanner.nextLine());

                String sql = "DELETE FROM inventaris WHERE id_barang=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, idBarang);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println(">> Data berhasil dihapus!");
                } else {
                    System.out.println(">> ID Barang tidak ditemukan.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR DRIVER: File .jar MySQL belum terbaca!");
        } catch (SQLException e) {
            System.out.println("Error Delete: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ID Barang harus angka!");
        }
    }
}

// ==========================================
// CLASS PENDUKUNG - INVENTARIS HMSI
// ==========================================

interface OperasiBarang {

    String getDeskripsi();
}

abstract class BarangSekre implements OperasiBarang {
    protected String namaBarang;

    public BarangSekre(String namaBarang) {
        this.namaBarang = namaBarang;
    }

}

class AlatOptik extends BarangSekre {
    public AlatOptik(String namaBarang) {
        super(namaBarang);

    }


    @Override
    public String getDeskripsi() {
        return "Alat optik untuk pengamatan (mikroskop, lup, dll)";
    }
}

class AlatGelas extends BarangSekre {
    public AlatGelas(String namaBarang) {
        super(namaBarang);

    }



    @Override
    public String getDeskripsi() {
        return "Peralatan gelas HMSI (beaker, tabung reaksi, dll)";
    }
}

class Elektronik extends BarangSekre {
    public Elektronik(String namaBarang) {
        super(namaBarang);

    }


    @Override
    public String getDeskripsi() {
        return "Peralatan elektronik (komputer, oscilloscope, dll)";
    }
}

class AlatUkur extends BarangSekre {
    public AlatUkur(String namaBarang) {
        super(namaBarang);

    }



    @Override
    public String getDeskripsi() {
        return "Alat pengukuran (timbangan, penggaris, jangka sorong, dll)";
    }
}