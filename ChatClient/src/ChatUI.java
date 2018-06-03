import javax.swing.*;
import javax.swing.border.*;
 
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;
 
public class ChatUI{
	
	private ChatClient client;
	private ChatServerInt server;
	
	public void doConnect(){
		if (connect.getText().equals("Conectar")){
	    	if (name.getText().length()<2){
	    		JOptionPane.showMessageDialog(frame, "É necessário informar um nome."); 
	    		return;
	    	}
	    	
	    	if (ip.getText().length()<2){
	    		JOptionPane.showMessageDialog(frame, "É necessário informar um IP."); 
	    		return;
	    	}
	    	
	    	try{
				client = new ChatClient(name.getText());
	    		client.setGUI(this);
				server=(ChatServerInt)Naming.lookup("rmi://"+ip.getText()+"/asdChat");
				server.login(client);
				updateUsers(server.getConnected());
			    connect.setText("Desconectar");			    
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();JOptionPane.showMessageDialog(frame, "Erro na conexão.");
	    	}		  
		}
		
		else{
	    	  	updateUsers(null);
	    	  	connect.setText("Conectar");
		}
	}  
  
	public void sendText(){
		if (connect.getText().equals("Conectar")){
			JOptionPane.showMessageDialog(frame, "Você deve se conectar antes."); 
			return;	
		}
		
		String st=tf.getText();
		st="["+name.getText()+"] "+st;
		tf.setText("");
		try{
    	  	server.publish(st);
  	  	}
		catch(Exception e){
			e.printStackTrace();
		}
	}
 
	public void writeMsg(String st){  
		if(tx.getText().equals("")) {
			tx.setText(st);
		}
		else {
			tx.setText(tx.getText()+"\n"+st);
		}  
	}
 
	public void updateUsers(Vector v){
		DefaultListModel listModel = new DefaultListModel();
		if(v!=null) for (int i=0;i<v.size();i++){
		try{
			String tmp=((ChatClientInt)v.get(i)).getName();
			listModel.addElement(tmp);
		}
		catch(Exception e){
			e.printStackTrace();}
		}
		lst.setModel(listModel);
  }
  
	public static void main(String [] args){
		ChatUI c=new ChatUI();
	}  
  
  //User Interface code.
	public ChatUI(){
		frame =new JFrame("Chat em grupo");
		
	    JPanel main = new JPanel();
	    JPanel top = new JPanel();
	    JPanel cn = new JPanel();
	    JPanel bottom = new JPanel();
	    
	    ip=new JTextField();
	    tf=new JTextField();
	    name=new JTextField();
	    tx=new JTextArea();
	    connect = new JButton("Conectar");
	    JButton bt = new JButton("Enviar mensagem");
	    JButton bt_ba = new JButton("Baixar arquivo");
	    JButton bt_ea = new JButton("Enviar arquivo");
	    lst=new JList();
	    
	    main.setLayout(new BorderLayout(5,5));         
	    top.setLayout(new GridLayout(1,0,5,5));   
	    cn.setLayout(new BorderLayout(5,5));
	    bottom.setLayout(new GridLayout(1,0,5,5));
	    top.add(new JLabel("Nickname: "));
	    top.add(name);    
	    top.add(new JLabel("Endereço do servidor: "));
	    top.add(ip);
	    top.add(connect);
	    cn.add(new JScrollPane(tx), BorderLayout.CENTER);        
	    cn.add(lst, BorderLayout.EAST);
	    bottom.add(tf);    
	    bottom.add(bt);
	    bottom.add(bt_ba);
	    bottom.add(bt_ea);
	    main.add(top, BorderLayout.NORTH);
	    main.add(cn, BorderLayout.CENTER);
	    main.add(bottom, BorderLayout.SOUTH);
	    main.setBorder(new EmptyBorder(10, 10, 10, 10) );
    
	    //Events
	    connect.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){ 
	    		doConnect();   
	    		}  
	    });
	    
	    bt.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){ 
	    		sendText();
	    	}  
	    });
	    
	    bt_ba.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){ 
	    		try {
	    			String filename=tf.getText();
					server.receiveData(client, filename);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}  
	    });
	    
	    bt_ea.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){ 
	    		try {
	    			String filename=tf.getText();
					client.receiveData(server, filename);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}  
	    });
	    
	    tf.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){ 
	    		sendText();   
	    	}  
	    });
	    
	    frame.setContentPane(main);
	    frame.setSize(800,800);
	    frame.setVisible(true);  
	  }
	
	  JTextArea tx;
	  JTextField tf,ip, name;
	  JButton connect;
	  JList lst;
	  JFrame frame;
}