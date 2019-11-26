
package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;


/**
 *
 * @author Casa
 */
public class GameRunning {
    EchoClient client;
    Scanner scanner = new Scanner(System.in);
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    
    ObjectInputStream serverObjectInputStream ;    
    ObjectOutputStream serverObjectOutputStream;
 
    public void setup() throws SocketException,UnknownHostException, IOException{
        client = new EchoClient();        
    }
    
    
    public Boolean loading() throws InterruptedException, IOException, ClassNotFoundException{
        
        System.out.println("Aguardando partida");
        int timer = 0;
        String x;

        ObjectInputStream serverObjectInputStream = new ObjectInputStream(client.socket.getInputStream());
        HashMap<String,Object> listOfMessages = (HashMap<String,Object>) serverObjectInputStream.readObject();
        Boolean error = (Boolean) listOfMessages.getOrDefault("error", false);
        if(error){
            System.out.println(listOfMessages.get("message"));            
        } else if ((Boolean)listOfMessages.get("isConnected")){
            System.out.println(Game.START_MESSAGE);   
            return true;
        }
        return false;
    }
    
    public void startGame()throws IOException, InterruptedException, ClassNotFoundException {
        int questions = 0;
        HashMap<String,Object> listOfMessages;
        HashMap<String,Object> resp;
        Boolean error = false;
        while(questions < Game.NUMBER_QUESTIONS){
            serverObjectOutputStream = new ObjectOutputStream(client.socket.getOutputStream());
            resp = new HashMap<>();
            resp.put("option", "getAlternatives");
            serverObjectOutputStream.writeObject(resp);
            
            serverObjectInputStream = new ObjectInputStream(client.socket.getInputStream());
            listOfMessages = (HashMap<String,Object>) serverObjectInputStream.readObject();
            error = (Boolean) listOfMessages.getOrDefault("error", false);
            if(error){
                System.out.println(listOfMessages.get("message"));
                return;
            }                
            System.out.println(listOfMessages.get("question"));
            
            int alternative = selectAlternative();
            
            serverObjectOutputStream = new ObjectOutputStream(client.socket.getOutputStream());
            resp = new HashMap<>();            
            
            resp.put("option", "response");
            resp.put("alternative", alternative);
            
            serverObjectOutputStream.writeObject(resp);
            
            serverObjectInputStream = new ObjectInputStream(client.socket.getInputStream());
            listOfMessages = (HashMap<String,Object>) serverObjectInputStream.readObject();
            error = (Boolean) listOfMessages.getOrDefault("error", false);
            if(error){
                System.out.println(listOfMessages.get("message"));
                return;
            }
            System.out.println(listOfMessages.get("result"));  
            questions++;
        }
        getResult();
        closeConnection();
    }
    
    
    public int selectAlternative(){
        int alternative;
        do{
            System.out.print("digite a sua resposta : ");
            alternative = scanner.nextInt();
            if(alternative < 1 || alternative > 4)
                System.out.println("Alternativa invalida! Digite um valor entre 1 e 4 ");
        }while(alternative < 1 || alternative > 4);
        return alternative;
    }
    
    public void getResult() throws IOException, ClassNotFoundException{
        serverObjectOutputStream = new ObjectOutputStream(client.socket.getOutputStream());
        HashMap<String,Object> resp = new HashMap<>();      
        resp.put("option", "result");
        serverObjectOutputStream.writeObject(resp);
        
        serverObjectInputStream = new ObjectInputStream(client.socket.getInputStream());
        HashMap<String,Object> messageFromServer = (HashMap<String,Object>) serverObjectInputStream.readObject();

        System.out.println(messageFromServer.get("result"));
    }
    
    private void closeConnection() throws IOException {
        serverObjectOutputStream = new ObjectOutputStream(client.socket.getOutputStream());
        HashMap<String,Object> resp = new HashMap<>();      
        resp.put("option", "end");
        serverObjectOutputStream.writeObject(resp);
        
        
    }
 
   
}
