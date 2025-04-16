// package Practise;
import java.net.*;
import java.io.*;
// import java.util.Scanner;
public  class Client_two_way {
    public static void main(String[] args) throws IOException {
        Socket s=new Socket("localhost", 6000);
        System.out.println("Client is connected at server Handshaking port :" +s.getPort());
        System.out.println("Client's Communication Port: "+s.getLocalPort());
        System.out.println("-------------Client is connceted-----------------");
        System.out.println("Enter the message to send to server: ");
        DataOutputStream output=new DataOutputStream(s.getOutputStream());
        DataInputStream input=new DataInputStream(s.getInputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String str="";
        while(!str.equals("exit")){
            str=br.readLine();
            output.writeUTF(str);
            output.flush();
            String serverResponse = input.readUTF(); // Receive response
            System.out.println("Server Says: " + serverResponse);
            if(str.equalsIgnoreCase("stop")){
                break;
            }
        }
        output.close();
        s.close();  
    }
}