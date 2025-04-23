import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable{
    private Socket socket;
    private Server server;
    private BufferedReader bufferReader;
    private BufferedWriter bufferedWriter;
    private int clientID;
    private String userName;
    private static final AtomicInteger idCounter=new AtomicInteger(1);
    

    public ClientHandler(Socket socket, Server server){
        try{
            this.server=server;
            this.socket=socket;
            this.bufferReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientID=idCounter.getAndIncrement();
            this.userName=bufferReader.readLine();
            sendMessage("Welcome to the server, "+userName);
        }catch(IOException e){
            closeEverything();
        }
        
    }
    @Override 
    public void run(){
        String messageFromClient;
        try{
            while(socket.isConnected()){
                messageFromClient=bufferReader.readLine();
                if(messageFromClient==null){
                    break;
                }else{
                    System.out.println("Client "+clientID+" ( "+userName+" ): "+messageFromClient);
                    // sendMessage();
                }
            }
        }catch(IOException e){
            closeEverything();;
        }
    }
    public void sendMessage(String message){
        try{
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void closeEverything(){
        server.removeClient(this);
        try{
            if(bufferReader!=null){
                bufferReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket!=null){socket.close();}
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public int getClientID(){
        return this.clientID;
    }
    public String getUserName(){
        return this.userName;
    }
}
