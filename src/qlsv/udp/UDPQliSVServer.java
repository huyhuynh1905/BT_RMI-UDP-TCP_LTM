package qlsv.udp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connection.sinhvien.ConnectionSinhVien;
import model.SinhVien;

public class UDPQliSVServer {

	//Server:
	public void serve() {
		System.out.println("Starting connect...");
		try {
			DatagramSocket dsocket = new DatagramSocket(1235);
			while(true) {
				byte[] reveiceArray = new byte[256];
				DatagramPacket dskreveice = new DatagramPacket(reveiceArray, reveiceArray.length);
				dsocket.receive(dskreveice);
				String receive = new String(dskreveice.getData()).trim();
				if(receive.equals("6")) {
					System.out.println("Tắt Server!");
					dsocket.close();
					break;
				}
				switch(receive) {
					case "1":
						ByteArrayOutputStream bout = new ByteArrayOutputStream();
						ObjectOutputStream obo = new ObjectOutputStream(bout);
						obo.writeObject(hienThiDanhSach());
						byte[] hienthi = bout.toByteArray();
						DatagramPacket dataHt = new DatagramPacket(hienthi, hienthi.length, dskreveice.getAddress(), dskreveice.getPort());
						//dsocket = new DatagramSocket();
						dsocket.send(dataHt);
						break;
					case "2":
						byte[] receiAdd = new byte[1024];
						DatagramPacket packetAdd = new DatagramPacket(receiAdd, receiAdd.length);
						dsocket.receive(packetAdd);
						byte[] data = packetAdd.getData();
						ByteArrayInputStream arrInAdd = new ByteArrayInputStream(data);
						ObjectInputStream obinAdd = new ObjectInputStream(arrInAdd);
						SinhVien sv = (SinhVien) obinAdd.readObject();
						themSinhVien(sv);
						break;
					case "3":
						byte[] receiDel = new byte[256];
						DatagramPacket packetDel = new DatagramPacket(receiDel, receiDel.length);
						dsocket.receive(packetDel);
						String masv = new String(packetDel.getData()).trim();
						String repli = xoaSinhVien(masv);
						DatagramPacket repDel = new DatagramPacket(repli.getBytes(), repli.getBytes().length, dskreveice.getAddress(),dskreveice.getPort());
						dsocket.send(repDel);
						break;
					case "4":
						//Nhận
						byte[] arrUpd = new byte[1024];
						DatagramPacket packetUpd = new DatagramPacket(arrUpd, arrUpd.length);
						dsocket.receive(packetUpd);
						ByteArrayInputStream binUpd = new ByteArrayInputStream(packetUpd.getData());
						ObjectInputStream obiUpd = new ObjectInputStream(binUpd);
						SinhVien svUpd = (SinhVien) obiUpd.readObject();
						capNhatSinhVien(svUpd);
						break;
					case "5":
						byte[] arrStrSea = new byte[256];
						DatagramPacket packetSearchRec = new DatagramPacket(arrStrSea, arrStrSea.length);
						dsocket.receive(packetSearchRec);
						String masvStr = new String(packetSearchRec.getData()).trim();
						ByteArrayOutputStream boutSearch = new ByteArrayOutputStream();
						ObjectOutputStream oboSearch = new ObjectOutputStream(boutSearch);
						oboSearch.writeObject(timSinhVien(masvStr));
						byte[] arrSrearch = boutSearch.toByteArray();
						DatagramPacket packetRepSearch = new DatagramPacket(arrSrearch, arrSrearch.length,dskreveice.getAddress(),dskreveice.getPort());
						dsocket.send(packetRepSearch);
						break;
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//Hiển thị sinh viên:
	public ArrayList<SinhVien> hienThiDanhSach(){
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
	//Thêm sinh viên
	public void themSinhVien(SinhVien sv) {
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
	//Xoá sinh viên:
	public String xoaSinhVien(String masv) {
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
	//Cập nhật
	public void capNhatSinhVien(SinhVien sv) {
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
	//Tìm sinh viên
	public SinhVien timSinhVien(String masv) {
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
	
	
	public static void main(String[] args) {
		UDPQliSVServer sev = new UDPQliSVServer();
		sev.serve();
	}

}
