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
   
    public Client(Socket socket,String username){
        try{
            this.socket=socket;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username=username;
        }catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner=new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend=scanner.nextLine();
                bufferedWriter.write(username+" : "+messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        }catch(IOException e ){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
                public void run() {
                    String messageFromGroupChat; 

                    while(socket.isConnected()){
                        try{
                            messageFromGroupChat=bufferedReader.readLine();
                            if(messageFromGroupChat == null){
                                System.out.println("Server has closed the connection");
                                closeEverything(socket,bufferedReader,bufferedWriter);
                                break;
                            }
                            System.out.println(messageFromGroupChat);
                        }catch(IOException e){
                            closeEverything(socket,bufferedReader,bufferedWriter);
                            break;
                        }
                    }
               
                }
        }).start();
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter your username for group chat: ");
        String username=scanner.nextLine();
        Socket socket=new Socket("localhost",5000);
        Client client=new Client(socket,username);
        client.listenForMessage();
        client.sendMessage();

    }
}
