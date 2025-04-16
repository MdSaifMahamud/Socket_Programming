import java.io.IOException;
import java.io.*;
import java.net.*;
public class Server{
    public static void main(String[] args)throws IOException{
        ServerSocket ss=new ServerSocket(5000);
        System.out.println("Server is connceted at port  no: "+ss.getLocalPort());
        System.out.println("Server is connceting");
        System.out.println("-------------Waiting for client-----------------");

        String str="";
        while(!str.equals("exit")){
            Socket s =ss.accept();
            System.out.println("Client requested is accepted at port no : "+s.getPort());
            System.out.println("Server's Communication Port: "+s.getLocalPort());
            DataInputStream input=new DataInputStream(s.getInputStream());
            str=input.readUTF();
            System.out.println("Client says: "+str);
            s.close();
            input.close();
        }
        
    }
}
