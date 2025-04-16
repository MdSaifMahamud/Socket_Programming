import java.io.*;
import java.net.*;

public class Server_Lab {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6000);
        System.out.println("Server is connected at port no: " + ss.getLocalPort());

        System.out.println("Waiting for the client...\n");
        Socket s = ss.accept();
        System.out.println("Client connected from port: " + s.getPort());

        DataInputStream input = new DataInputStream(s.getInputStream());
        DataOutputStream output = new DataOutputStream(s.getOutputStream());
        String str = "";

        while (true) {
            str = input.readUTF();

            if (str.equalsIgnoreCase("stop")) {
                output.writeUTF("Server shutting down connection.");
                break;
            }

            String response;
            try {
                int number = Integer.parseInt(str);
                if (isPrime(number)) {
                    response = number + " is a Prime number";
                } else {
                    response = number + " is Not a Prime number";
                }
            } catch (NumberFormatException e) {
                String lower = str.toLowerCase();
                boolean isPalindrome = isPalindrome(lower);
                response = "Lowercase: " + lower + " | " + (isPalindrome ? "Palindrome" : "Not Palindrome");
            }

            System.out.println("Processed: " + response);
            output.writeUTF(response);
        }

        s.close();
        input.close();
        output.close();
        ss.close();
    }

    // Prime check
    public static boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0) return false;
        }
        return true;
    }


    public static boolean isPalindrome(String text) {
        String cleaned = text.replaceAll("\\s+", "").toLowerCase();
        int left = 0, right = cleaned.length() - 1;
        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}