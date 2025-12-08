package Model;

import java.sql.Date;

public class DatPhong {
    private int maPH;
    private int maKH;
    private String hinhThucDP;
    private Date ngNhanPH;
    private Date ngTraPH;
    
    
	public int getMaPH() {
		return maPH;
	}
	public void setMaPH(int maPH) {
		this.maPH = maPH;
	}
	public int getMaKH() {
		return maKH;
	}
	public void setMaKH(int maKH) {
		this.maKH = maKH;
	}
	public String getHinhThucDP() {
		return hinhThucDP;
	}
	public void setHinhThucDP(String hinhThucDP) {
		this.hinhThucDP = hinhThucDP;
	}
	public Date getNgNhanPH() {
		return ngNhanPH;
	}
	public void setNgNhanPH(Date ngNhanPH) {
		this.ngNhanPH = ngNhanPH;
	}
	public Date getNgTraPH() {
		return ngTraPH;
	}
	public void setNgTraPH(Date ngTraPH) {
		this.ngTraPH = ngTraPH;
	}
}
