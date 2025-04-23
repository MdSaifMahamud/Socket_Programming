import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    public Client(Socket socket, String username) throws IOException{
        try{
            this.socket=socket;
            this.username=username;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException e){
            closeEverything();
        }
    }
    public void sendMessage() throws IOException{
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner=new Scanner(System.in);
            System.out.println("Connected to server.");
            System.out.println("If you type normally its will send to the server");
            System.out.println("You can send multiple messages to the server");
            System.out.println("Upcoming message will be two types:");
            System.out.println("1.Server may reply your message privately");
            System.out.println("2. Server Can broadcast message for all");
            System.out.println("Type 'quit' or 'exit' to leave");
            System.out.println("\n------------------------------------------------");
            while(socket.isConnected()){
                String messageToSend=scanner.nextLine();
                if(messageToSend.toLowerCase().equals("quit") || messageToSend.toLowerCase().equals("qxit")){
                    closeEverything();
                    break;
                }
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            scanner.close();

        }catch(IOException e){

            closeEverything();
        }
    }
    public void closeEverything() throws IOException{
        if(bufferedReader!=null){bufferedReader.close();}
        if(bufferedWriter!=null){bufferedWriter.close();}
        if(socket!=null){socket.close();}
        
    }
    public void listenMessage(){
        new Thread(()->{
            String messageFromServer;
            try{
                while(socket.isConnected()){
                    messageFromServer=bufferedReader.readLine();
                    if(messageFromServer==null){
                        System.out.println("Sorry!!!\nServer disconnected");
                        break;
                    }
                    System.out.println(messageFromServer);
                }
            }catch(IOException e){
                try {
                    closeEverything();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }
        }).start();
    }
    public static void main(String[] args) throws UnknownHostException, IOException {
            Scanner scanner=new Scanner(System.in);
            System.out.print("Please, Enter Your Name: ");
            String username=scanner.nextLine();

            Socket socket =new Socket("localhost",7000);
            Client client=new Client(socket, username);
            client.listenMessage();
            client.sendMessage();
            scanner.close();
    }
   
}
