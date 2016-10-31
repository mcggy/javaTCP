import  java.net.*;
import java.io.*;

public class Client{

	public static void main(String[] args) throws IOException{
		Socket socket = new Socket("127.0.0.1",30000);
		new Thread(new ClientThread(socket)).start();
		//BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		System.out.println("please input user's name:");
		String name = null;
		BufferedReader wt = new BufferedReader(new InputStreamReader(System.in));
		while((name=wt.readLine())!=null) {  
            			out.println(name);  
            			out.flush();  
        		}  
		
		
	}
}
class ClientThread implements Runnable{
	private Socket s;
	BufferedReader in = null;
	public ClientThread(Socket s) throws IOException{
		this.s =s;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	public void run(){
		try{
			String information = null;
			while ((information=in.readLine())!=null) {
				System.out.println(information);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}