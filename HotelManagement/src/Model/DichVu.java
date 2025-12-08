package Model;

public class DichVu {
    private int maDV;
    private String tenDV;
    private double giaDV;
    
    
    
    
    
	public DichVu(int maDV, String tenDV, double giaDV) {
		super();
		this.maDV = maDV;
		this.tenDV = tenDV;
		this.giaDV = giaDV;
	}
	public int getMaDV() {
		return maDV;
	}
	public void setMaDV(int maDV) {
		this.maDV = maDV;
	}
	public String getTenDV() {
		return tenDV;
	}
	public void setTenDV(String tenDV) {
		this.tenDV = tenDV;
	}
	public double getGiaDV() {
		return giaDV;
	}
	public void setGiaDV(double giaDV) {
		this.giaDV = giaDV;
	}
}
