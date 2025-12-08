package Model;

public class NhanVien {
	private int maNV;
    private String tenNV;
    private String chucVu;
    private String cccd;
    private String sdt;

    public NhanVien(int maNV, String tenNV, String chucVu, String CCCD, String SDT) {
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.chucVu = chucVu;
		this.cccd = CCCD;
		this.sdt = SDT;
	} 

	public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getCCCD() {
        return cccd;
    }

    public void setCCCD(String cccd) {
        this.cccd = cccd;
    }

    public String getSDT() {
        return sdt;
    }

    public void setSDT(String sdt) {
        this.sdt = sdt;
    }
}
