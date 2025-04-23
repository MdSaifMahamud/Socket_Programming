import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server{
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler>clients;
    public Server(ServerSocket serverSocket){
        this.serverSocket=serverSocket;
        this.clients=new ArrayList<>();
    }
    public void startServer(){
        try{

        
            //  Start a thread to handle server console input
            startServerMessageThread();
            // 
            while(!serverSocket.isClosed()){
                Socket socket=serverSocket.accept() ;
                System.out.println("New Client is connected");
                ClientHandler clientHandler=new ClientHandler(socket,this);
                clients.add(clientHandler);
                Thread t=new Thread(clientHandler);
                t.start();
            }
        }catch(IOException e){
            e.printStackTrace();
            closeServer();  
        }

    }
    public void closeServer(){
        try{
            if(serverSocket!=null){
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void startServerMessageThread(){
        Thread serverMessageThread=new Thread(()->{
            Scanner scanner=new Scanner(System.in);
            System.out.println("Server Message Option");
            System.out.println("To broadcast: 'broadcast:<message>'");
            System.out.println("To send specific client: 'client:clientID:<message>'");
            System.out.println("To see all clients: 'list'");
            System.out.println("-----------------------------------------------------");
            while (!serverSocket.isClosed()){
                String input=scanner.nextLine();
                if(input.toLowerCase().trim().equals("list")){
                    listClients();
                    continue;
                }
                if(input.toLowerCase().startsWith("client:")){
                    try{
                        int colIndex=input.indexOf(':',7); // find second index of colon
                        if(colIndex>0){
                            String clientIDstr=input.substring(7,colIndex);
                            int clientID =Integer.parseInt(clientIDstr);
                            String ActualMessage=input.substring(colIndex+1);
                            //  now this message have to send the legitimate client 
                            sendMessageToClient(clientID, "SERVER(Priavte): "+ActualMessage);
                            // 
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Invalid Client ID");
                        e.printStackTrace();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                if(input.toLowerCase().startsWith("broadcast:")){   // if server wants to send message to all the clients 
                    String message=input.substring(10);
                    // Have to boradcast to all the client
                    broadcast("Server: "+message);
                    continue;
                }
            }
        });
        serverMessageThread.setDaemon(true);
        serverMessageThread.start();
    }
    private void listClients(){ // if server want to  see the  total clients then it works
        System.out.println("\n----- Connected Clients -----");
        if(clients.isEmpty()){
            System.out.println("No Clients are connected");
        }else{
            for(ClientHandler client:clients){
                System.out.println("Client ID: "+client.getClientID()+" | Username: "+client.getUserName());
            }
            
        }
        System.out.println("----------------------------\n");
    }
    private void sendMessageToClient(int clientID,String message){
        for(ClientHandler client:clients){
            if(client.getClientID()==clientID){
                client.sendMessage(message);
                System.out.println("Sent to the client "+clientID+": "+message);
                return;
            }
            System.out.println("Client with ID "+clientID+" is not found\nPlease try again");
        }
    }
    public void removeClient(ClientHandler clientHandler){
        clients.remove(clientHandler);
        System.out.println("Client "+clientHandler.getClientID()+" ( "+clientHandler.getUserName()+" ) disconnected");
    }
    public void broadcast(String message){
        System.out.println("Broadcasting message: "+message);
        for(ClientHandler client:clients){
            client.sendMessage(message);
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(7000);
        Server server=new Server(serverSocket);
        server.startServer();
    }
}
