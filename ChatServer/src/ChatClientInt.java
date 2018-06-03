import java.rmi.*;

public interface ChatClientInt extends Remote{

	public void sendMessage(String mensagem)throws RemoteException;
	
	public boolean sendData(String filename, byte[] data, int len) throws RemoteException;
	
	public boolean receiveData(ChatServerInt a, String filename) throws RemoteException;
	
	public String getName()throws RemoteException ;
	
}
