package Model;

import java.sql.Timestamp;

public class HoaDon {
    private int maHD;
    private int maKH;
    private String tenHD;
    private double tongTien;
    private String tinhTrangTT;
    private String hinhThucTT;
    private Timestamp ngayTT;
    private String tenKH;
    private String SDT;
    private int maPH;

    public HoaDon(int maHD, int maKH, String tenHD, double tongTien, String tinhTrangTT, String hinhThucTT, Timestamp ngayTT) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.tenHD = tenHD;
        this.tongTien = tongTien;
        this.tinhTrangTT = tinhTrangTT;
        this.hinhThucTT = hinhThucTT;
        this.ngayTT = ngayTT;
    }

    public int getMaHD() { return maHD; }
    public void setMaHD(int maHD) { this.maHD = maHD; }
    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }
    public String getTenHD() { return tenHD; }
    public void setTenHD(String tenHD) { this.tenHD = tenHD; }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    public String getTinhTrangTT() { return tinhTrangTT; }
    public void setTinhTrangTT(String tinhTrangTT) { this.tinhTrangTT = tinhTrangTT; }
    public String getHinhThucTT() { return hinhThucTT; }
    public void setHinhThucTT(String hinhThucTT) { this.hinhThucTT = hinhThucTT; }
    public Timestamp getNgayTT() { return ngayTT; }
    public void setNgayTT(Timestamp ngayTT) { this.ngayTT = ngayTT; }
    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }
    public String getSDT() { return SDT; }
    public void setSDT(String SDT) { this.SDT = SDT; }
    public int getMaPH() { return maPH; }
    public void setMaPH(int maPH) { this.maPH = maPH; }
}