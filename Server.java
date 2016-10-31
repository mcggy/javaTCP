import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  

public class Server{

	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
	public static final int PORT = 30000;
	public static void main(String[] args) throws IOException{
		ServerSocket server = new ServerSocket(PORT);
		while(true){
			Socket client = server.accept();
			socketList.add(client);
			new Thread(new ServerThread(client)).start();
		}
	}	
}
class ServerThread implements Runnable{
	//public ArrayList<String> lines = new ArrayList<String>();
	public  final File users = new File("/home/mcg/Code/Final2/users.txt");
	Socket s = null;
	BufferedReader in = null;
	public ServerThread(Socket s) throws IOException{
		this.s = s;
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));

	}
	public void run(){
		try{		
			String content = null;
			 Pattern pattern = Pattern  
                			.compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");  
			while((content=readFromClient())!=null){
				Matcher matcher = pattern  
               			 .matcher(content);  
				PrintWriter out = new PrintWriter(s.getOutputStream());
				if (matcher.find()) {
					try{
						URL url = new URL(content);
						BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
						String data = br.readLine();
						while (data!=null) {
							out.println(data);
							out.flush();
							data = br.readLine();
						}
					}catch(MalformedURLException ex){
						ex.printStackTrace();
						out.println("请确定网页输入正确");
						out.flush();
					}
					
				}else{
					String userline = findInformationByName(users,content);
					System.out.println(content);  
					out.println(userline);
					out.flush();
				}
			}
				
				
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String readFromClient(){
		try{
			return in.readLine();
		}catch (IOException e) {
			Server.socketList.remove(s);
		}
		return null;
	}
	 public  String findInformationByName(File file,String user){
		Scanner scanner=null;
		String line = null;
		String name = null;
		String password = null;
		try{
			scanner = new Scanner(new FileInputStream(file));
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			scanner = new Scanner(System.in);
		}
		try{
			
			while(scanner.hasNextLine()){
				// name = scanner.next();
				// password =scanner.next();
				String linetmp = scanner.nextLine();
				if((linetmp.substring(0,linetmp.lastIndexOf(" "))).equals(user)){
					//line = name +" "+password;
					line = linetmp;
				}
			}
		}catch(Exception e1){
			e1.printStackTrace();
		}
		scanner.close();
		return line;
	}
}
