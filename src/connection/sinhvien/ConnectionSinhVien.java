package connection.sinhvien;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSinhVien {
	public static Connection getConnect() {
		Connection conn = null;
		String url = "jdbc:jtds:sqlserver://localhost:1433/QUANLISV";
		String user = "sa";
		String pass = "123456789";
		//Kết nối:
		try {
			conn = DriverManager.getConnection(url,user,pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

}
