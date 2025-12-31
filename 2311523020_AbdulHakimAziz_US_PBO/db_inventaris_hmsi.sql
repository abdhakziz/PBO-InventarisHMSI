-- ============================================
-- DATABASE: db_inventaris_lab
-- Untuk Aplikasi Inventaris Laboratorium
-- ============================================

-- Membuat database jika belum ada
CREATE DATABASE IF NOT EXISTS db_inventaris_hmsi;
USE db_inventaris_hmsi;

-- ============================================
-- TABEL: inventaris
-- Menyimpan data barang inventaris lab
-- id_barang sebagai Primary Key
-- ============================================
DROP TABLE IF EXISTS inventaris;

CREATE TABLE inventaris (
    id_barang INT PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    jumlah INT NOT NULL,
    kondisi VARCHAR(30) NOT NULL,
    lokasi_lab VARCHAR(50) NOT NULL,
    tanggal_masuk DATE NOT NULL
);

-- ============================================
-- DATA CONTOH (Opsional)
-- ============================================
INSERT INTO inventaris (id_barang, nama_barang, kategori, jumlah, kondisi, lokasi_lab, tanggal_masuk) VALUES
(1, 'Mikroskop Binokuler', 'ALAT OPTIK', 5, 'BAIK', 'Lab Biologi', '2025-12-01'),
(2, 'Gelas Beaker 500ml', 'ALAT GELAS', 20, 'BAIK', 'Lab Kimia', '2025-12-05'),
(3, 'Komputer Desktop', 'ELEKTRONIK', 10, 'BAIK', 'Lab Komputer', '2025-12-10'),
(4, 'Timbangan Digital', 'ALAT UKUR', 3, 'RUSAK RINGAN', 'Lab Fisika', '2025-11-20');

-- ============================================
-- QUERY UNTUK MELIHAT DATA
-- ============================================
-- SELECT * FROM inventaris;
