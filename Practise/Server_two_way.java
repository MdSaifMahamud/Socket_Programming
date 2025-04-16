// package Practise;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Server_two_way {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6000);
        System.out.println("Server is connected at port no: " + serverSocket.getLocalPort());
        System.out.println("Server is connecting");
        System.out.println("-------------Waiting for client-----------------");
        Socket socket = serverSocket.accept();
        System.out.println("Client requested is accepted at port no: " + socket.getPort());
        System.out.println("Server's Communication Port: " + socket.getLocalPort());
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        while (true) {
            str = input.readUTF();
            if (str.equalsIgnoreCase("stop")) {
                output.writeUTF("Server shutting down connection.");
                break;
            }

            System.out.println("Client says: " + str);
            String news=br.readLine();
            output.writeUTF(news);
            output.flush();
            if(news.equalsIgnoreCase("stop")){
                break;
            }
        }
        socket.close();
        input.close();
        output.close();
        serverSocket.close();
    
    }
}
