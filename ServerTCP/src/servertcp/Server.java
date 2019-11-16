/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

/**
 *
 * @author Casa
 */
import game.GameServer;
import static game.GameServer.getRandom;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Random;

public class Server extends Thread {
 
    private DatagramSocket socket;
    private ServerSocket socketTCP;
    private boolean running;
    private byte[] buf = new byte[256];
    private int correctAlternative;
    private int pontos;
//    private GameServer = new GameServer();
    
    public Server() throws SocketException, IOException{
        socketTCP = new ServerSocket(8080);
//        socket = new DatagramSocket(4445);
    }

    public ServerSocket getSocketTCP() {
        return socketTCP;
    }
    
    @Override
    public void run(){
        
    }
 
//    @Override
//    public void run(){
//        running = true;
//        try{
//            while (running) {
//                buf = new byte[256];
//                DatagramPacket packet = new DatagramPacket(buf, buf.length);
//                socket.receive(packet);
//                InetAddress address = packet.getAddress();
//                int port = packet.getPort();
//                String received = new String(packet.getData(), 0, packet.getLength());
//                
//                System.out.println("Server - Recive - "+received);
//                if (received.equals("close")) {
//                    break;
//                } 
//                ExecuteOption(received,address,port);
//                
//            }
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        } finally {            
//            socket.close();
//        }
//    }
    
    void ExecuteOption(String received,InetAddress address,int port) throws IOException{
        byte[] temp;
        DatagramPacket packet;
        switch(received){
            case "play":
                // Solicitou inicio do jogo
                String msg = "Bem vindo!";
                this.pontos = 0;
                temp = msg.getBytes();
                System.out.println("Server - Start - "+temp.length);
                packet = new DatagramPacket(temp, 10, address, port);
                break;
            case "get":
                String wordRand = getRandom();
                String shuffedRand = GameServer.shuffle(wordRand);
                String question = "Qual é o formato correto da palavra "+shuffedRand+"?\n";
                question += getAlternatives(wordRand); 
                temp = question.getBytes();
                packet = new DatagramPacket(temp, question.length()+1, address, port);
                break;
            case "result":
                String msgEnd = "Fim de jogo!\nSeu resultado foi de "+this.pontos+" pontos";                
                temp = msgEnd.getBytes();
                packet = new DatagramPacket(temp, temp.length, address, port);
                socket.send(packet);
                socket.close();
                this.running = false;
            default:
                // Recebeu resposta do jogador 
//                String resposta = received.substring(received.indexOf("resp-"));
//                if(received.lastIndexOf("resp-")>0){
                    String shuffled = GameServer.shuffle(received);      
                    System.out.println("Server - Resposta - "+received.substring(5));
                    System.out.println("Server - Alternativa Resposta - "+this.correctAlternative);
                    if(Integer.parseInt(received.substring(5)) == this.correctAlternative){
                        temp = "Correto!".getBytes();
                        pontos++;
                    }
                    else
                        temp = "Resposta invalida!".getBytes();
                    
                    packet = new DatagramPacket(temp, temp.length, address, port); 
                    break;
//                }
//                packet = new DatagramPacket("".getBytes(), 0, address, port); // erro - n recebeu resp-
//                break;                
        }
        socket.send(packet);
    }
    
    public String getAlternatives(String wordCorrect){
        String message = "";
        String[] alternatives = new String[]{wordCorrect,GameServer.shuffle(wordCorrect),GameServer.shuffle(wordCorrect),GameServer.shuffle(wordCorrect)};        
        for (int i = 0; i < alternatives.length; i++) {
            Random r = new Random();
            int indexRand = r.nextInt(4);
            int indexCorrect;
            Boolean flag = false, flag2 = false;
            String temp = alternatives[i];
            if(temp == wordCorrect)
                flag = true;
            String temp2 = alternatives[indexRand];
            if(temp2 == wordCorrect)
                flag2 = true;
            alternatives[i] = temp2;
            alternatives[indexRand] = temp;
            if(flag)
                this.correctAlternative = indexRand+1;
            if(flag2)
                this.correctAlternative = i+1;
        }
        message += "1) "+alternatives[0]+"\n";
        message += "2) "+alternatives[1]+"\n";
        message += "3) "+alternatives[2]+"\n";
        message += "4) "+alternatives[3]+"\n";
        return message;        
        
    }
}