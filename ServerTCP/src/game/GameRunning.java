/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Casa
 */
public class GameRunning implements Runnable{
    Socket socketPlayer1;
    Socket socketPlayer2;
    ObjectInputStream objectInputStream;

    public GameRunning() {
    }

    public GameRunning(Socket socketPlayer1, Socket socketPlayer2) {
        this.socketPlayer1 = socketPlayer1;
        this.socketPlayer2 = socketPlayer2;
        
    }

    
    @Override
    public void run() {
        System.out.println("Partida criada entre "+socketPlayer1.getPort()+" e "+socketPlayer2.getPort());
        DataOutputStream outToClient,outToClient2;
        BufferedReader br , br2;
        try {

            ObjectOutputStream player1output = new ObjectOutputStream(socketPlayer1.getOutputStream());
            ObjectOutputStream player2output = new ObjectOutputStream(socketPlayer2.getOutputStream());
            HashMap response = new HashMap<String,Boolean>();
            response.put("isConnected", true);
            System.out.println("Msg enviada para "+socketPlayer1.getPort());
            player1output.writeObject(response);
            System.out.println("Msg enviada para "+socketPlayer2.getPort());
            player2output.writeObject(response);
            
       
            ObjectInputStream player1input = new ObjectInputStream(socketPlayer1.getInputStream());
            System.out.println("Msg recebida do player 1 = "+(String)player1input.readObject());
            ObjectInputStream player2input = new ObjectInputStream(socketPlayer2.getInputStream());
            System.out.println("Msg recebida do player 2 = "+(String)player1input.readObject());
//            while(true){
//                
//            }

        } catch (IOException ex) {
            System.err.println(ex.getCause());
            Logger.getLogger(GameRunning.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getCause());
            Logger.getLogger(GameRunning.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
