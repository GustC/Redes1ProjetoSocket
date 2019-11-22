/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.GameServer.getRandom;
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
import java.util.Random;
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
    private int correctAlternative;

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
            // pega uma palavra aleatoria do banco de palavras
            
            HashMap<String,Object> responseGame;
            HashMap<String,Boolean> responsePlayer1;
            HashMap<String,Boolean> responsePlayer2;
            for(int questions = 0 ; questions < 3 ; questions++){
                

                ObjectInputStream player1input = new ObjectInputStream(socketPlayer1.getInputStream());
                ObjectInputStream player2input = new ObjectInputStream(socketPlayer2.getInputStream());
                
                responsePlayer1 = (HashMap<String,Boolean>)  player1input.readObject();
                responsePlayer2 = (HashMap<String,Boolean>)  player2input.readObject();
                
                String option = responseClient(responsePlayer1, responsePlayer2);
                
                switch(option){                
                    case "alternatives":
                        String wordRand = getRandom();// pega uma palavra random
                        String shuffedRand = GameServer.shuffle(wordRand);// embaralha a palavra selecionada
                        String question = "Qual é o formato correto da palavra "+shuffedRand+"?\n";
                        question += getAlternatives(wordRand); // monta as alternativas da questao


                        player1output = new ObjectOutputStream(socketPlayer1.getOutputStream());
                        player2output = new ObjectOutputStream(socketPlayer2.getOutputStream());
                        responseGame = new HashMap<>();
                        responseGame.put("question", question);
                        player1output.writeObject(responseGame);// envia a questao para o jogador 1
                        player2output.writeObject(responseGame);// envia a questao para o jogador 2
                        break;
                    case "result":
                        break;
                    default:
                        break;
                
                }
            }
            
            
//            ObjectInputStream player1input = new ObjectInputStream(socketPlayer1.getInputStream());
//            System.out.println("Msg recebida do player 1 = "+(String)player1input.readObject());
//            ObjectInputStream player2input = new ObjectInputStream(socketPlayer2.getInputStream());
//            System.out.println("Msg recebida do player 2 = "+(String)player1input.readObject());
//            while(true){
//                
//            }

        } catch (IOException ex) {
            System.err.println(ex.getCause());
            Logger.getLogger(GameRunning.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameRunning.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public String responseClient(HashMap<String,Boolean> responsePlayer1,HashMap<String,Boolean> responsePlayer2 ){
        if(responsePlayer1.get("getAlternatives") && responsePlayer2.get("getAlternatives"))
            return "alternatives";
        else if(responsePlayer1.get("result") && responsePlayer2.get("result"))
            return "result";
        else 
            return "";
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
