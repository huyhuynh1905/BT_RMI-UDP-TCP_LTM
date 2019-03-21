package model;

import java.io.Serializable;

public class SinhVien implements Serializable {
	public String masv;
	public String hosv;
	public String tensv;
	public String ngaysinh;
	public String diachi;
	
	
	
	public SinhVien(String masv, String hosv, String tensv, String ngaysinh, String diachi) {
		super();
		this.masv = masv;
		this.hosv = hosv;
		this.tensv = tensv;
		this.ngaysinh = ngaysinh;
		this.diachi = diachi;
	}
	public String getMasv() {
		return masv;
	}
	public void setMasv(String masv) {
		this.masv = masv;
	}
	public String getHosv() {
		return hosv;
	}
	public void setHosv(String hosv) {
		this.hosv = hosv;
	}
	public String getTensv() {
		return tensv;
	}
	public void setTensv(String tensv) {
		this.tensv = tensv;
	}
	public String getNgaysinh() {
		return ngaysinh;
	}
	public void setNgaysinh(String ngaysinh) {
		this.ngaysinh = ngaysinh;
	}
	public String getDiachi() {
		return diachi;
	}
	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}
	

}
