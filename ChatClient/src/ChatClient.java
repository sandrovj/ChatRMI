import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
 
public class ChatClient  extends UnicastRemoteObject implements ChatClientInt{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private ChatUI ui;	
	
	public ChatClient (String n) throws RemoteException {
		name=n;
	}
	
	public void sendMessage(String st) throws RemoteException{
		System.out.println(st);
		ui.writeMsg(st);
	}
	public String getName() throws RemoteException{
		return name;
	}
	
	public void setGUI(ChatUI t){ 
		ui=t ; 
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
	public boolean receiveData(ChatServerInt a, String filename) throws RemoteException {
		
		 try{
			 File f1=new File(filename);			 
			 FileInputStream in=new FileInputStream(f1);			 				 
			 byte [] mydata=new byte[1024*1024];						
			 int mylen=in.read(mydata);
			 while(mylen>0){
				 a.sendData(f1.getName(), mydata, mylen);	 
				 mylen=in.read(mydata);				 
			 }
			 a.publish(name+" enviou o arquivo " + filename +".");
		 }
		 catch(Exception e){
			 a.publish("Erro ao enviar arquivo " + filename + "para o servidor.");
			 return false;
		 }
		 
		 return true;
	}	
}