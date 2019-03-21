package qlsv.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connection.sinhvien.ConnectionSinhVien;
import model.SinhVien;

public class TCPQliSVServer {
	
	//Gán cứng
	ArrayList<SinhVien> arsv = new ArrayList();
	
	//Sẻver
	public void serve() {
		try {
			ServerSocket ser = new ServerSocket(1234);
			Socket socket = ser.accept();
			while(true) {
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String select = br.readLine();
				if(select.equals("6")) {
					System.out.println("Đóng kết nối!");
					br.close();
					socket.close();
					ser.close();
					break;
				}
				switch(select) {
					case "1":
						layDanhSach();
						ObjectOutputStream obo = new ObjectOutputStream(socket.getOutputStream());
						obo.writeObject(arsv);
						obo.flush();
						System.out.println("Đã trả về client!");
						//obo.close();
						break;
					case "2":
						ObjectInputStream obi = new ObjectInputStream(socket.getInputStream());
						SinhVien sv = (SinhVien) obi.readObject();
						themSinhVien(sv);
						//obi.close();
						break;
					case "3":
						BufferedReader deleReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String dele = deleReader.readLine();
						PrintStream pr = new PrintStream(socket.getOutputStream());
						pr.println(xoaSinhVien(dele));
						break;
					case "4":
						ObjectInputStream obiupdate = new ObjectInputStream(socket.getInputStream());
						SinhVien svupdate = (SinhVien) obiupdate.readObject();
						capNhatSinhVien(svupdate);
						break;
					case "5":
						BufferedReader brsearch = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						ObjectOutputStream obosearch = new ObjectOutputStream(socket.getOutputStream());
						obosearch.writeObject(timSinhVien(brsearch.readLine()));
						break;
					default:
						continue;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Lấy danh sách sinh viên
	public void layDanhSach() {
		arsv.clear();
		Connection conn = ConnectionSinhVien.getConnect();
		String query = "select MASV,HOSV,TENSV,NGAYSINH,DIACHI from SINHVIEN";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				SinhVien sv = new SinhVien(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				arsv.add(sv);
			}
			//Đóng
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Thêm
	public void themSinhVien(SinhVien sv) {
		Connection con = ConnectionSinhVien.getConnect();
		String query = "insert into SINHVIEN (MASV,HOSV,TENSV,NGAYSINH,DIACHI) values (?,?,?,?,?)";
		try {
			PreparedStatement prst = con.prepareStatement(query);
			prst.setString(1, sv.getMasv());
			prst.setString(2, sv.getHosv());
			prst.setString(3, sv.getTensv());
			prst.setString(4, sv.getNgaysinh());
			prst.setString(5, sv.getDiachi());
			prst.executeUpdate();
			System.out.println("Thêm thành công!");
			prst.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Xoá
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
	//Caạp nhật:
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
	//Tìm
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
		TCPQliSVServer serv = new TCPQliSVServer();
		serv.serve();
	}

}
