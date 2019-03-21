package qlsv.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import model.SinhVien;

public class TCPQliSVClient {
	
	//Clien
	public void client() {
		try {
			Socket socket = new Socket("localhost",1234);
			Scanner sc = new Scanner(System.in);
			while(true) {
				showMenu();
				System.out.print("Nhập lựa chọn của bạn:");
				String select = sc.nextLine();
				PrintStream pr = new PrintStream(socket.getOutputStream());
				pr.println(select);
				if(select.equals("6")) {
					System.out.println("Thoát!");
					pr.close();
					socket.close();
					break;
				}
				switch(select) {
					case "1":
						ObjectInputStream obi = new ObjectInputStream(socket.getInputStream());
						ArrayList<SinhVien> arsv = new ArrayList<SinhVien>();
						arsv = (ArrayList<SinhVien>) obi.readObject();
						for(int i =0; i<arsv.size();i++) {
							System.out.println(arsv.get(i).getMasv().trim()+" - "+arsv.get(i).getHosv().trim()+" "+arsv.get(i).getTensv().trim()+" - "+
									arsv.get(i).getNgaysinh().trim()+" - "+arsv.get(i).getDiachi().trim());
						}
						break;
					case "2":
						SinhVien sv = themSinhVien();
						ObjectOutputStream obo = new ObjectOutputStream(socket.getOutputStream());
						obo.writeObject(sv);
						obo.flush();
						//obo.close();
						break;
					case "3":
						PrintStream delete = new PrintStream(socket.getOutputStream());
						delete.println(maXoa());
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String kq = br.readLine();
						System.out.println(kq);
						break;
					case "4":
						SinhVien svcn = capNhat();
						ObjectOutputStream obocn = new ObjectOutputStream(socket.getOutputStream());
						obocn.writeObject(svcn);
						obocn.flush();
						//obo.close();
						break;
					case "5":
						System.out.print("Nhập id muốn tìm:");
						String masv = sc.nextLine();
						pr.println(masv);
						ObjectInputStream obisearch = new ObjectInputStream(socket.getInputStream());
						SinhVien svsearch = (SinhVien) obisearch.readObject();
						if(svsearch!=null) {
							System.out.println(svsearch.getMasv()+" - "+svsearch.getHosv()+" "+svsearch.getTensv()+" - "+svsearch.getNgaysinh()+" - "+svsearch.getDiachi());;
						} else System.out.println("Không tìm thấy sinh viên!");
						break;
					default:
						System.out.println("Nhập sai lựa chọn, hãy nhập lại!");
						break;
						
				}
				
				
			}
		} catch (UnknownHostException e) {
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

	
	//Menu
	public void showMenu() {
		System.out.println("+-------------------Menu-----------------+");
		System.out.println("|1. Xem danh dách sinh viên              |");
		System.out.println("|2. Thêm sinh viên                       |");
		System.out.println("|3. Xoá sinh viên                        |");
		System.out.println("|4. Cập nhật thông tin sinh viên         |");
		System.out.println("|5. Tìm sinh viên                        |");
		System.out.println("|6. Exit                                 |");
		System.out.println("+----------------------------------------+");
	}
	//Thêm sinh viên:
	public SinhVien themSinhVien() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Nhập mã sv:");
		String masv = sc.nextLine();
		System.out.print("Nhập họ tên đệm:");
		String hosv = sc.nextLine();
		System.out.print("Nhập tên:");
		String tensv = sc.nextLine();
		System.out.print("Nhập ngày sinh (yyyy-MM-dd):");
		String ngaysinh = sc.nextLine();
		System.out.print("Nhập địa chỉ:");
		String diachi = sc.nextLine();
		SinhVien sv = new SinhVien(masv, hosv, tensv, ngaysinh, diachi);
		return sv;
	}
	//Xoá:
	public String maXoa() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Nhập mã sinh viên muốn xoá:");
		String dele = sc.nextLine();
		return dele;
	}
	//Cập nhật:
	public SinhVien capNhat() {
		String capnhat = "";
		Scanner sc = new Scanner(System.in);
		System.out.print("Nhập mã sv cần cập nhật:");
		String masv = sc.nextLine();
		System.out.print("Nhập Họ vè tên đệm sv cần cập nhật:");
		String hosv = sc.nextLine();
		System.out.print("Nhập tên sv cần cập nhật:");
		String tensv = sc.nextLine();
		System.out.print("Nhập ngày sinh sv cần cập nhật (yyyy-MM-dd):");
		String ngaysinh = sc.nextLine();
		System.out.print("Nhập địa chỉ sv cần cập nhật:");
		String diachi = sc.nextLine();
		SinhVien sv = new SinhVien(masv, hosv, tensv, ngaysinh, diachi);
		return sv;
	}
	//Tìm sinh viên:
	public static void main(String[] args) {
		TCPQliSVClient cl = new TCPQliSVClient();
		cl.client();
	}
}
