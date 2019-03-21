package qlsv.rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import model.SinhVien;
import qlsv.rmi.model.InterfaceQuanLiSV;
import qlsv.rmi.server.QuanLiSinhVien;

public class QliSVRMIClient {
	
	public void client() {
		Scanner sc = new Scanner(System.in);
		InterfaceQuanLiSV qlsv = null;
		try {
			qlsv = (InterfaceQuanLiSV) Naming.lookup("rmi://localhost:1236/calRemote");
			while(true) {
				showMenu();
				System.out.print("Nhập lựa chọn:");
				String select = sc.nextLine();
				if(select.equals("6")) {
					System.out.println("Kết thúc chương trình!");
					break;
				}
				switch(select) {
					case "1":
						ArrayList<SinhVien> arsv = qlsv.hienThiDanhSach();
						for(int i=0;i<arsv.size();i++) {
							System.out.println(arsv.get(i).getMasv().trim()+" - "+arsv.get(i).getHosv().trim()+" "+arsv.get(i).getTensv().trim()+" - "+
									arsv.get(i).getNgaysinh().trim()+" - "+arsv.get(i).getDiachi().trim());
						}
						break;
					case "2":
						qlsv.themSinhVien(themSinhVien());
						break;
					case "3":
						System.out.print("Nhập mã sv muốn xoá:");
						String xoa = sc.nextLine();
						System.out.println(qlsv.xoaSinhVien(xoa));
						break;
					case "4":
						qlsv.capNhatSinhVien(capNhatSinhVien());
						break;
					case "5":
						System.out.print("Nhập mã số sinh viên cần tìm:");
						String masvsearch = sc.nextLine();
						SinhVien sv = qlsv.timSinhVien(masvsearch);
						if(sv!=null) {
							System.out.println(sv.getMasv()+" - "+sv.getHosv()+" "+sv.getTensv()+" - "+sv.getNgaysinh()+
									" - "+sv.getDiachi());;
						} else System.out.println("Không tìm thấy sinh viên!");
						break;
					default:
						System.out.println("Nhập sai lựa chọn, hãy nhập lại!");
						break;
						
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
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
	//Thêm sinh viên
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
	//Cập nhật
	public SinhVien capNhatSinhVien() {
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
	
	public static void main(String[] args) {
		QliSVRMIClient cl = new QliSVRMIClient();
		cl.client();
	}

}
