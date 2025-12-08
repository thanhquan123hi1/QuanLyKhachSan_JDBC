package Model;


import java.sql.Timestamp;

public class SuDungDichVu {
	
	private int maKH;
    private int maDV;
    private int soLuong;
    private Timestamp thoiGian;
	public int getMaKH() {
		return maKH;
	}
	public void setMaKH(int maKH) {
		this.maKH = maKH;
	}
	public int getMaDV() {
		return maDV;
	}
	public void setMaDV(int maDV) {
		this.maDV = maDV;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public Timestamp getThoiGian() {
		return thoiGian;
	}
	public void setThoiGian(Timestamp thoiGian) {
		this.thoiGian = thoiGian;
	}
}
