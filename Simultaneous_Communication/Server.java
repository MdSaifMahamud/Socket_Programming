import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients;
    public Server(ServerSocket serverSocket){
        this.serverSocket=serverSocket;
        this.clients=new ArrayList<>();
    }
    public void startServer(){
        try{
            // Make a thread to handle server input from console
            startServerMessageThread();
            while(!serverSocket.isClosed()){
                Socket socket=serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler=new ClientHandler(socket,this);
                clients.add(clientHandler);
                Thread t=new Thread(clientHandler);
                t.start();
            }
        }catch(IOException e){
            System.out.println("Error in server: "+e.getMessage());
            closeServerSocket();
        }
    }
    private void startServerMessageThread(){
        Thread serverMessageThread=new Thread(()->{
            Scanner scanner=new Scanner(System.in);
            System.out.println("Server message options:");
            System.out.println("To broadcast: 'Broadcast:<message>'");
            System.out.println("To send to specific client: 'client:<clientID>:<message>");
            System.out.println("To list all clients: 'list'");
            while(!serverSocket.isClosed()){
                String message=scanner.nextLine();
                if(message.toLowerCase().equals("list")){
                    listClients();
                    continue;
                }
                if(message.toLowerCase().startsWith("broadcast: ")){
                    String msg=message.substring(10);
                    broadcastMessage("Server: " + msg);
                    continue;
                }
                if(message.toLowerCase().startsWith("client:")){
                    try{
                        int colonindex=message.indexOf(':',7);
                        if(colonindex>0){
                            String clientIDstr=message.substring(7,colonindex).trim();
                            int clientID=Integer.parseInt(clientIDstr);
                            String msg=message.substring(colonindex+1).trim();
                            sendMessageToClient(clientID, "Server: "+msg);
                        }else{
                            System.out.println("Invalid client ID format. Please Use 'Client:ID:message'");
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Invalid client ID format. Please Use 'Client:ID:message'");
                        continue;
                    }
                    catch(Exception e){
                        System.out.println("Error in sending message to client: "+e.getMessage());
                    }
                }
            }
            scanner.close();
        });
        serverMessageThread.setDaemon(true);
        serverMessageThread.start();
    }
    public void broadcastMessage(String message){
        System.out.println("Broadcasting message: "+message);
        for(ClientHandler client:clients){
            client.sendMessage(message);
        }
    }
    public void sendMessageToClient(int clientID,String message){
        for(ClientHandler client:clients){
            if(client.getClientID()==clientID){
                client.sendMessage(message);
                System.out.println("Sent to client "+clientID+": "+message);
                return;
            }
        }
        System.out.println("Client with ID "+clientID+" not found.");
    }
    public void listClients(){
        System.out.println("----------Connected clients----------");
        if(clients.size()==0){
            System.out.println("No clients connected.");
            // return;
        }else{
            for(ClientHandler client:clients){
                System.out.println("Client ID: "+client.getClientID()+" | Username: "+client.getClientName());
            }
        }
        System.out.println("-------------------------------------");
        
    }

    public void removeClient(ClientHandler client){
        clients.remove(client);
        System.out.println("Client "+client.getClientName()+" disconnected.");
        System.out.println("Active clients: "+clients.size());
    }
    public void closeServerSocket(){
        try{
            if(serverSocket!=null){
                serverSocket.close();
                System.out.println("Server socket closed.");
            }
        }catch(IOException e){
            System.out.println("Error closing server socket: "+e.getMessage());
        }
    }
    public static void main(String[] args)throws IOException{
        ServerSocket serverSocket=new ServerSocket(7000);
        System.out.println("Server started on port 7000");
        Server server=new Server(serverSocket);
        server.startServer();
    }

}
