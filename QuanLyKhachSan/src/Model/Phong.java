package Model;

public class Phong {
    private int maPH;
    private String loaiPH;
    private String tinhTrangPH;
    private double giaPH;
    
    public Phong() {
    	super();
    }
    
    public Phong(int maPH, String loaiPH, String tinhTrangPH, double giaPH) {
    	this.maPH = maPH;
    	this.loaiPH = loaiPH;
    	this.tinhTrangPH = tinhTrangPH;
    	this.giaPH = giaPH;
    }
    
	public int getMaPH() {
		return maPH;
	}
	public void setMaPH(int maPH) {
		this.maPH = maPH;
	}
	public String getLoaiPH() {
		return loaiPH;
	}
	public void setLoaiPH(String loaiPH) {
		this.loaiPH = loaiPH;
	}
	public String getTinhTrangPH() {
		return tinhTrangPH;
	}
	public void setTinhTrangPH(String tinhTrangPH) {
		this.tinhTrangPH = tinhTrangPH;
	}
	public double getGiaPH() {
		return giaPH;
	}
	public void setGiaPH(double giaPH) {
		this.giaPH = giaPH;
	}
}
