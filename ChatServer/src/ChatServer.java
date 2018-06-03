import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatServer extends UnicastRemoteObject implements ChatServerInt{
	
	private Vector v=new Vector();
	private String file="";
	
	public ChatServer() throws RemoteException{}
	
	@Override
	public boolean login(ChatClientInt a) throws RemoteException {
		System.out.println(a.getName() + "  se conectou.");	
		a.sendMessage("Você se conectou com sucesso.");
		publish(a.getName()+ " acabou de se conectar.");
		v.add(a);
		return true;		
	}
	@Override
	public void publish(String s) throws RemoteException {
		System.out.println(s);
		for(int i=0;i<v.size();i++){
		    try{
		    	ChatClientInt tmp=(ChatClientInt)v.get(i);
			tmp.sendMessage(s);
		    }catch(Exception e){
		    	System.out.println("Erro no envio de mensagem a um dos clientes!");
		    }
		}		
	}
	@Override
	public boolean sendData(String filename, byte[] data, int len) throws RemoteException {
		try{
        	File f=new File(filename);
        	f.createNewFile();
        	FileOutputStream out=new FileOutputStream(f,true);
        	out.write(data,0,len);
        	out.flush();
        	out.close();
        	System.out.println("Arquivo enviado.");
        }catch(Exception e){
        	e.printStackTrace();
        }
		return true;
	}
	@Override
	public Vector getConnected() throws RemoteException {
		return v;
	}

	@Override
	public boolean receiveData(ChatClientInt a, String filename) throws RemoteException {
		file=filename;
		
		 try{
			 File f1=new File(file);			 
			 FileInputStream in=new FileInputStream(f1);			 				 
			 byte [] mydata=new byte[1024*1024];						
			 int mylen=in.read(mydata);
			 while(mylen>0){
				 a.sendData(f1.getName(), mydata, mylen);	 
				 mylen=in.read(mydata);				 
			 }
			 a.sendMessage("Arquivo " + filename +" encaminhado com sucesso para " + a.getName() + ".");
		 }
		 catch(Exception e){
			 a.sendMessage("Erro ao recuperar arquivo." + filename);
			 return false;
		 }
		 
		 return true;
	}
	
}
