import java.io.IOException;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args)throws IOException {
        Socket s=new Socket("localhost", 5000);
        System.out.println("Client is connected at server Handshaking port :" +s.getPort());
        System.out.println("Client's Communication Port: "+s.getLocalPort());
        System.out.println("-------------Client is connceted-----------------");
        System.out.println("Enter the message to send to server: ");
        DataOutputStream output=new DataOutputStream(s.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String str="";
        while(!str.equals("exit")){
            str=br.readLine();
            output.writeUTF(str);
            output.flush();
        }
        output.close();
        s.close();  
    }
}
