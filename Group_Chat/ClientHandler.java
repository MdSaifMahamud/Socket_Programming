import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    public ClientHandler(Socket socket){
        try{
            this.socket=socket;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername=bufferedReader.readLine();
            clientHandlers.add(this);
            broadCastMessage("SERVER: "+clientUsername+" has entered the chat!!!");
        }catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

	@Override
	public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                messageFromClient=bufferedReader.readLine();
                if(messageFromClient == null) {
                    System.out.println(clientUsername + " has disconnected.");
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break; // Exit the loop when client disconnects
                }
                broadCastMessage(messageFromClient);
            }catch(IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
	}
    public void broadCastMessage(String messageToSend){
        for(ClientHandler clientHandler:clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                   clientHandler.bufferedWriter.write(messageToSend); 
                   clientHandler.bufferedWriter.newLine();
                   clientHandler.bufferedWriter.flush();
                }
            }catch(IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCastMessage("SERVER: "+clientUsername+" has left the chat!!!");
    }
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        removeClientHandler();
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
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
