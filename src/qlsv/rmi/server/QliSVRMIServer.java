package qlsv.rmi.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class QliSVRMIServer {

	
	public void serve() {
		try {
			LocateRegistry.createRegistry(1236);
			System.out.println("Server running...");
			QuanLiSinhVien qlsv = new QuanLiSinhVien();
			Naming.rebind("rmi://localhost:1236/calRemote", qlsv);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		QliSVRMIServer sv = new QliSVRMIServer();
		sv.serve();
	}

}
