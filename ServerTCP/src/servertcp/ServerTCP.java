
package servertcp;

import game.GameRunning;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerTCP implements AutoCloseable {
	public static void main(String[] args) {
        
        ServerSocket socketTCP;
        Socket lastClient = null;
        try{
            socketTCP = new ServerSocket(8080);
            
            while(true){
                System.out.println("Aguardando conexões");
                Socket player1 = socketTCP.accept();
                System.out.println("Player 1 conectado!");
                Socket player2 = socketTCP.accept();
                System.out.println("Player 2 conectado!");
                new Thread(new GameRunning(player1,player2)).start(); 
//                if(connectionSocket.isConnected()){
//                    System.out.println("Cliente conectado!");
//                    if(lastClient != null)
//                        new Thread(new GameRunning(lastClient,connectionSocket)).start();
//                        
//                    else
//                        lastClient = connectionSocket;
//                    
//                    
//                };
            }
//            server.start();
        } catch ( Exception e ) { 
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
