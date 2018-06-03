import java.rmi.*;
import java.util.*;

public interface ChatServerInt extends Remote {
	
	public boolean login(ChatClientInt a)throws RemoteException ;
	
	public void publish(String s)throws RemoteException ;
	
	public boolean sendData(String filename, byte[] data, int len) throws RemoteException;
	
	public boolean receiveData(ChatClientInt a, String filename) throws RemoteException;
	
	public Vector getConnected() throws RemoteException;
	
}
