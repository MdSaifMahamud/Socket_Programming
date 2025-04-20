import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {
    private static final AtomicInteger idCounter=new AtomicInteger(1);
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int clientID;
    private String clientName;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        try{
            this.socket = socket;
            this.server = server;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientID=idCounter.getAndIncrement();
            this.clientName=bufferedReader.readLine();
            sendMessage("Welcome to the server, "+clientName+"!\nYour Client ID is: "+clientID);
            server.broadcastMessage("Client "+clientName+" has joined the server.");

        }catch(IOException e){
            closeEverything();
        }
        // Initialize bufferedReader and other resources here
    }
    @Override
    public void run(){
        String messageFromClient;
        try{
            while(socket.isConnected()){
                messageFromClient=bufferedReader.readLine();
                if(messageFromClient==null){
                    break;
                }
                if(messageFromClient.startsWith("/msg ")){
                    // 
                }else{
                    System.out.println("Message from "+clientName+": "+messageFromClient);
                    // server.broadcastMessage(messageFromClient);
                    sendMessage("Server : Message received" );
                }
            }
        }catch(IOException e){
            closeEverything();
        }
    }
    public void sendMessage(String message){
        try{
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException e){
            System.out.println("Error sending message to client: "+e.getMessage());
        }
    }
    public int getClientID(){
        return clientID;
    }
    public String getClientName(){
        return clientName;
    }
    public void  closeEverything(){
        server.removeClient(this);
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch(IOException e){
            System.out.println("Error closing resources: "+e.getMessage());
        }finally{
            server.removeClient(this);
        }
    }
}
