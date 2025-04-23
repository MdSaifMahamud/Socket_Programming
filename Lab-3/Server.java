import java.net.ServerSocket;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server{
    private ServerSocket serverSocket;
    private static List<User>users=new CopyOnWriteArrayList<>();
    private static Set<String>completedTransactionsID=ConcurrentHashMap.newKeySet();
    
    public static void main(String[] args){
        addUser();
    }
    private static void addUser(){
        users.add(new User("12345678","1234",10000));
        users.add(new User("87654321","4321",20000));
        users.add(new User("12121212","0000", 1000));
        users.add(new User("00000000","1111", 5000));
        users.add(new User("22222222","2222", 10000));
        users.add(new User("33333333","3333", 20000));
    }
    static class User{
        private String cardNo;
        private String pin;
        private volatile int balance;
        public User(String cardNoString,String pin,int balance){
            this.cardNo=cardNoString;
            this.balance=balance;
            this.pin=pin;
        }
        public String getCardNo(){
            return this.cardNo;
        }
        public String getPin(){
            return pin;
        }   
        public int getBalance(){
            return balance;
        }
        public void setBalance(int balance){
            this.balance = balance;
        }
    }
}