package Model;

import java.sql.Timestamp;

public class KhachHang {
    private int maKH;
    private String tenKH;
    private String quocTich;
    private String cccd_VISA;
    private String sdt;
    private Timestamp ngNhanPH;
    private Timestamp ngTraPH;
    private int maPH;

    public KhachHang() {
    }

    public KhachHang(int maKH, String tenKH, String quocTich, String cccd_VISA, String sdt, Timestamp ngNhanPH, Timestamp ngTraPH, int maPH) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.quocTich = quocTich;
        this.cccd_VISA = cccd_VISA;
        this.sdt = sdt;
        this.ngNhanPH = ngNhanPH;
        this.ngTraPH = ngTraPH;
        this.maPH = maPH;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    public String getCCCD_VISA() {
        return cccd_VISA;
    }

    public void setCCCD_VISA(String cccd_VISA) {
        this.cccd_VISA = cccd_VISA;
    }

    public String getSDT() {
        return sdt;
    }

    public void setSDT(String sdt) {
        this.sdt = sdt;
    }

    public Timestamp getNgNhanPH() {
        return ngNhanPH;
    }

    public void setNgNhanPH(Timestamp ngNhanPH) {
        this.ngNhanPH = ngNhanPH;
    }

    public Timestamp getNgTraPH() {
        return ngTraPH;
    }

    public void setNgTraPH(Timestamp ngTraPH) {
        this.ngTraPH = ngTraPH;
    }

    public int getMaPH() {
        return maPH;
    }

    public void setMaPH(int maPH) {
        this.maPH = maPH;
    }
}