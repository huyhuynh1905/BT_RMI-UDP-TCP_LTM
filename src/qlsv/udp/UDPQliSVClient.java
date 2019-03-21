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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import model.SinhVien;

public class UDPQliSVClient {

	public void client() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			showMenu();
			System.out.print("Nhập lựa chọn:");
			String select = sc.nextLine();
			try {
				DatagramPacket dpk = new DatagramPacket(select.getBytes(), select.getBytes().length, InetAddress.getLocalHost(), 1235);
				DatagramSocket dsk = new DatagramSocket();
				dsk.send(dpk);
				if(select.equals("6")) {
					dsk.close();
					System.out.println("Tắt kết nối!");
					break;
				}
				//Chức năng:
				switch(select) {
					case "1":
						byte[] recHt = new byte[1024];
						DatagramPacket dataHt = new DatagramPacket(recHt,recHt.length);
						dsk.receive(dataHt);
						byte[] data = dataHt.getData();
						ByteArrayInputStream arrIn = new ByteArrayInputStream(data);
						ObjectInputStream obiHt = new ObjectInputStream(arrIn);
						ArrayList<SinhVien> arsv = (ArrayList<SinhVien>) obiHt.readObject();
						for(int i=0;i<arsv.size();i++) {
							System.out.println(arsv.get(i).getMasv()+" - "+arsv.get(i).getHosv()+" "+arsv.get(i).getTensv()+" - "+
									arsv.get(i).getNgaysinh()+" - "+arsv.get(i).getDiachi());
						}
						break;
					case "2":
						ByteArrayOutputStream arrOutAdd = new ByteArrayOutputStream();
						ObjectOutputStream oboAdd = new ObjectOutputStream(arrOutAdd);
						oboAdd.writeObject(themSinhVien());
						byte[] sendAdd = arrOutAdd.toByteArray();
						dpk = new DatagramPacket(sendAdd, sendAdd.length, InetAddress.getLocalHost(), 1235);
						dsk.send(dpk);
						break;
					case "3":
						System.out.print("Nhập mã sinh viên cần xoá:");
						String masv = sc.nextLine();
						DatagramPacket packetDelete = new DatagramPacket(masv.getBytes(), masv.length(), InetAddress.getLocalHost(), 1235);
						dsk.send(packetDelete);
						//nhận kết quả
						byte[] kq = new byte[256];
						DatagramPacket packetKq = new DatagramPacket(kq, kq.length);
						dsk.receive(packetKq);
						String kqStr = new String(packetKq.getData()).trim();
						System.out.println(kqStr);
						break;
					case "4":
						SinhVien svUpd = capNhatSinhVien();
						ByteArrayOutputStream boutUpd =  new ByteArrayOutputStream();
						ObjectOutputStream oboUpd = new ObjectOutputStream(boutUpd);
						oboUpd.writeObject(svUpd);
						byte[] arrUdp = boutUpd.toByteArray();
						DatagramPacket packetUpd = new DatagramPacket(arrUdp, arrUdp.length, InetAddress.getLocalHost(),1235);
						dsk.send(packetUpd);
						break;
					case "5":
						System.out.print("Nhập id muốn tìm:");
						String masvSearch = sc.nextLine();
						DatagramPacket packetSearch = new DatagramPacket(masvSearch.getBytes(), masvSearch.getBytes().length,InetAddress.getLocalHost(),1235);
						dsk.send(packetSearch);
						//Nhận
						byte[] arrSear = new byte[1024];
						DatagramPacket packetRecSear = new DatagramPacket(arrSear, arrSear.length);
						dsk.receive(packetRecSear);
						ByteArrayInputStream binSear = new ByteArrayInputStream(packetRecSear.getData());
						ObjectInputStream obinSearch = new ObjectInputStream(binSear);
						SinhVien svSear = (SinhVien) obinSearch.readObject();
						if(svSear!=null) {
							System.out.println(svSear.getMasv()+" - "+svSear.getHosv()+" "+svSear.getTensv()+" - "+svSear.getNgaysinh()+" - "+svSear.getDiachi());;
						} else System.out.println("Không tìm thấy sinh viên!");
						break;
					default:
						System.out.println("Nhập sai lựa chọn, hãy nhập lại!");
						break;
				}
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	//Cập nhật:
	public SinhVien capNhatSinhVien() {
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
	
	
	public static void main(String[] args) {
		UDPQliSVClient cl = new UDPQliSVClient();
		cl.client();
	}

}
