import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private String clientName;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public Client(Socket socket,String clientName){
        this.socket=socket;
        this.clientName=clientName;
        try{
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(Exception e){
            closeEverything();
        }
    }
    public void sendMessage(){
        try{
            bufferedWriter.write(clientName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner=new Scanner(System.in);

            System.out.println("Connected to server. Commands:");
            System.out.println("- Type normally to send messages to server");
            System.out.println("- Use '/msg clientId message' to send direct messages to specific clients");
            System.out.println("- Type '/quit' to exit");
            while(socket.isConnected()){
                String messageToSend=scanner.nextLine();
                if(messageToSend.equalsIgnoreCase("/quit")){
                    closeEverything();
                    break;
                }
                bufferedWriter.write(clientName);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            scanner.close();
        }catch(IOException e){
            closeEverything();
        }
    }
    public void listenForMessages(){
        new Thread(()->{
            String messageFromServer;
            try{
                while(socket.isConnected()){
                    messageFromServer=bufferedReader.readLine();
                    if(messageFromServer==null){
                        break;
                    }
                    System.out.println(messageFromServer);
                }
            }catch(IOException e){
                closeEverything();
            }
        }).start();
    }
    public void  closeEverything(){
        try{
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch(Exception e){
            System.out.println("Error closing resources: "+e.getMessage());
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter you username: ");
        String clientName = scanner.nextLine();
        Socket socket = new Socket("localhost", 7000); // Replace with server IP and port
        Client client=new Client(socket, clientName);
        client.listenForMessages();
        client.sendMessage();
        
        scanner.close();
    }
}
