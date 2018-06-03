import java.rmi.*;
import java.rmi.server.*;

public class StartServer {
	
	public static void main(String[] args) {
		try {
			 	java.rmi.registry.LocateRegistry.createRegistry(1099);
			 	
				ChatServerInt b=new ChatServer();	
				Naming.rebind("rmi://127.0.0.1/asdChat", b);
				System.out.println("[System] Servidor de chat está ativo.");
		}
		catch (Exception e) {
					System.out.println("Erro no servidor de chat: " + e);
		}
	}

}
