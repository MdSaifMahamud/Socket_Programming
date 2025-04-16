// import java.io.*;
// import java.net.*;
// import java.util.Scanner;

// public class Client_Lab{
//     public static void main(String[] args) throws IOException {
//         Socket s = new Socket("localhost", 5000); // Connect to the server
//         DataInputStream input = new DataInputStream(s.getInputStream());
//         DataOutputStream output = new DataOutputStream(s.getOutputStream());

//         Scanner scanner = new Scanner(System.in);
//         String userInput = "";

//         while (true) {
//             System.out.print("Enter a number or a string (type 'stop' to exit): ");
//             userInput = scanner.nextLine();

//             output.writeUTF(userInput); // Send input to server

//             String serverResponse = input.readUTF(); // Receive response
//             System.out.println("Server Response: " + serverResponse);

//             if (userInput.equalsIgnoreCase("stop")) {
//                 break;
//             }
//         }

//         s.close();
//         input.close();
//         output.close();
//         scanner.close();
//     }
// }

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client_Lab{
    public static void main(String[] args) throws IOException {
        Socket s=new Socket("localhost", 6000); // Connect to the server
        DataInputStream input=new DataInputStream(s.getInputStream());
        DataOutputStream output=new DataOutputStream(s.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String str="";
        Scanner sc=new Scanner(System.in);
        while(true){
            System.out.print("Enter a number or a string (type 'stop' to exit): ");
            str=sc.nextLine();
            output.writeUTF(str); // Send input to server
            String serverResponse = input.readUTF(); // Receive response
            System.out.println("Server Response: " + serverResponse);
            if(str.equalsIgnoreCase("stop")){
                break;
            }
        }
        s.close();
        input.close();
        output.close();
        sc.close();
    }
}