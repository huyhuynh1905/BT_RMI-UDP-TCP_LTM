package qlsv.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connection.sinhvien.ConnectionSinhVien;
import model.SinhVien;
import qlsv.rmi.model.InterfaceQuanLiSV;

public class QuanLiSinhVien extends UnicastRemoteObject implements InterfaceQuanLiSV {

	protected QuanLiSinhVien() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<SinhVien> hienThiDanhSach() throws RemoteException {
		ArrayList<SinhVien> arsv = new ArrayList<SinhVien>();
		Connection conn = ConnectionSinhVien.getConnect();
		String query = "select MASV,HOSV,TENSV,NGAYSINH,DIACHI from SINHVIEN";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				SinhVien sv = new SinhVien(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), rs.getString(4).trim(), rs.getString(5).trim());
				arsv.add(sv);
			}
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arsv;
	}

	@Override
	public void themSinhVien(SinhVien sv) throws RemoteException {
		Connection conn = ConnectionSinhVien.getConnect();
		String query = "insert into SINHVIEN (MASV,HOSV,TENSV,NGAYSINH,DIACHI) values (?,?,?,?,?)";
		try {
			PreparedStatement prst = conn.prepareStatement(query);
			prst.setString(1, sv.getMasv());
			prst.setString(2, sv.getHosv());
			prst.setString(3, sv.getTensv());
			prst.setString(4, sv.getNgaysinh());
			prst.setString(5, sv.getDiachi());
			prst.executeUpdate();
			System.out.println("Thêm thành công!");
			prst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String xoaSinhVien(String masv) throws RemoteException {
		String kq="";
		Connection conn = ConnectionSinhVien.getConnect();
		String query = "delete from SINHVIEN where MASV=?";
		try {
			PreparedStatement prst = conn.prepareStatement(query);
			prst.setString(1, masv);
			prst.executeUpdate();
			prst.close();
			conn.close();
			kq = "Đã xoá thành công!";
			return kq;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			kq = "Xoá không thành công!";
			e.printStackTrace();
			return kq;
		}
	}

	@Override
	public void capNhatSinhVien(SinhVien sv) throws RemoteException {
		Connection con = ConnectionSinhVien.getConnect();
		String query = "update SINHVIEN set HOSV = ?,TENSV = ?,NGAYSINH = ?,DIACHI = ? where MASV = ?";
		try {
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, sv.getHosv());
			st.setString(2, sv.getTensv());
			st.setString(3, sv.getNgaysinh());
			st.setString(4, sv.getDiachi());
			st.setString(5, sv.getMasv());
			st.executeUpdate();
			System.out.println("Sửa thành công!");
			st.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public SinhVien timSinhVien(String masv) throws RemoteException {
		SinhVien sv = null;
		Connection conn = ConnectionSinhVien.getConnect();
		String query = "select MASV,HOSV,TENSV,NGAYSINH,DIACHI from SINHVIEN where MASV = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, masv);
			ResultSet rs = ps.executeQuery();
			if(rs!=null) {
				while(rs.next()) {
					sv = new SinhVien(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), 
							rs.getString(4).trim(), rs.getString(5).trim());
				}
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sv;
	}

}
