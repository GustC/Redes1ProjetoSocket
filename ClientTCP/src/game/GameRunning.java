
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
    
 
    public void setup() throws SocketException,UnknownHostException, IOException{
//        server = new EchoServer();
        client = new EchoClient();
        
        outToServer = new DataOutputStream(client.socket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
//        os = new DataOutputStream(client.getOutputStream());
//		is = new BufferedReader(new InputStreamReader(client.getInputStream()));
//        server.start();
    }
    
    
    public void loading() throws InterruptedException, IOException, ClassNotFoundException{
        
        System.out.println("Aguardando partida");
        int timer = 0;
        String x;

        ObjectInputStream objectInputStream = new ObjectInputStream(client.socket.getInputStream());
        HashMap<String,Boolean> listOfMessages = (HashMap<String,Boolean>) objectInputStream.readObject();
        System.out.println("Message received from the server : " +listOfMessages.get("isConnected"));
        if(listOfMessages.get("isConnected")){
            System.out.println(Game.START_MESSAGE);        
        }
//        if()
        
//        OutputStream os = client.socket.getOutputStream();
//        OutputStreamWriter osw = new OutputStreamWriter(os);
//        BufferedWriter bw = new BufferedWriter(osw);
//        bw.write("dalee \n");
//        System.out.println("Message sent to the server : "+"dalee");
//        
//        InputStream is = client.socket.getInputStream();
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr);
//        String message = br.readLine();
//        System.out.println("Message received from the server : " +message);
        
        
        
//        System.out.print("From server : "+x);
         
//        while(true){            
//            System.out.print(".");
//            Thread.sleep(1000);
////            System.out.print(inFromServer.readLine());
//            System.out.print(".");
//            Thread.sleep(1000);
////            System.out.print(inFromServer.readLine());
//            System.out.print(".");
//            Thread.sleep(1000);
//           
//            System.out.print(x);
//            System.out.print("\rAguardando partida");            
//        
    }
    
    public void startGame()throws IOException, InterruptedException, ClassNotFoundException {
        String echo = client.sendEcho("play");
        System.out.println(echo);
        int questions = 0;
        while(questions < Game.NUMBER_QUESTIONS){
            ObjectOutputStream output = new ObjectOutputStream(client.socket.getOutputStream());
            HashMap<String,Object> resp = new HashMap<>();
            resp.put("option", "getAlternatives");
            output.writeObject(resp);
            
            ObjectInputStream objectInputStream = new ObjectInputStream(client.socket.getInputStream());
            HashMap<String,Object> listOfMessages = (HashMap<String,Object>) objectInputStream.readObject();
            
            System.out.println(listOfMessages.get("question"));
            
            int op;
            do{
                System.out.print("digite a sua resposta : ");
                op = scanner.nextInt();
                if(op < 1 || op > 4)
                    System.out.println("Alternativa invalida! Digite um valor entre 1 e 4 ");
            }while(op < 1 || op > 4);
            
            output = new ObjectOutputStream(client.socket.getOutputStream());
            resp = new HashMap<>();            
            
            resp.put("option", "response");
            resp.put("alternative", op);
            
            output.writeObject(resp);
            
            objectInputStream = new ObjectInputStream(client.socket.getInputStream());
            listOfMessages = (HashMap<String,Object>) objectInputStream.readObject();
            
            System.out.println(listOfMessages.get("result"));  
            questions++;
        }
        
//        if(echo.equals("Bem Vindo!"));{
//            int respostas = 0;
//            do{
//                echo = client.sendEcho("get");      
//                System.out.println(echo);
//                int op;
//                do{
//                    System.out.print("digite a sua resposta : ");
//                    op = scanner.nextInt();
//                    if(op < 1 || op > 4)
//                        System.out.println("Alternativa invalida! Digite um valor entre 1 e 4 ");
//                }while(op < 1 || op > 4);
//                echo = client.sendEcho("resp-"+op);
//                System.out.println(echo);
//                respostas++;
//                Thread.sleep(1500);                
//            }while(respostas < 10);
//            echo = client.sendEcho("result");
//            System.out.println(echo);
//        }
        
    }
 
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException{
        String echo = client.sendEcho("hello server");
//        echo = client.sendEcho("server is working");
        System.out.println(echo.equals("hello server"));
    }
    
    public void SendFromServer() throws IOException {
        String echo = client.sendEcho("Bola");
        System.out.println(echo.equals("Daleee server"));
    }
 
    public void tearDown() throws IOException{
        client.sendEcho("close");
        client.close();
    }
}
