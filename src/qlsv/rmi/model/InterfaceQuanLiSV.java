package qlsv.rmi.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import model.SinhVien;

public interface InterfaceQuanLiSV extends Remote {
	
	//hiển thị
	ArrayList<SinhVien> hienThiDanhSach() throws RemoteException;
	
	//Thêm
	void themSinhVien(SinhVien sv) throws RemoteException;
	
	//Xoá
	String xoaSinhVien(String masv) throws RemoteException;
	
	//Cập nhật
	void capNhatSinhVien(SinhVien sv) throws RemoteException;
	
	//Tim
	SinhVien timSinhVien(String masv) throws RemoteException;

	
}
