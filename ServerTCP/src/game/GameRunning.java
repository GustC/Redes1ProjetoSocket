/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static game.GameServer.NUMBER_QUESTIONS;
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
        int pointsPlayer1 = 0 ,pointsPlayer2 = 0;
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
            HashMap<String,Object> responsePlayer1;
            HashMap<String,Object> responsePlayer2;
            Boolean end = false;
            int questions = 0 ;
            while(socketPlayer1.isConnected() && socketPlayer2.isConnected()){
                System.out.println("Questao "+questions);
                ObjectInputStream player1input = null ;
                ObjectInputStream player2input = null ;
                if(socketPlayer1.isConnected())
                    player1input = new ObjectInputStream(socketPlayer1.getInputStream());
                if(socketPlayer2.isConnected())
                    player2input = new ObjectInputStream(socketPlayer2.getInputStream());
                
                responsePlayer1 = (HashMap<String,Object>)  player1input.readObject();
                responsePlayer2 = (HashMap<String,Object>)  player2input.readObject();
                
                String option = responseClient(responsePlayer1, responsePlayer2);
                System.out.println("Opçao recebida = "+ option);
                
                String message, message2;
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
                    case "response":
                        int answerPlayer1, answerPlayer2;
                        answerPlayer1 = (int) responsePlayer1.get("alternative");
                        answerPlayer2 = (int) responsePlayer2.get("alternative");
                        
                        player1output = new ObjectOutputStream(socketPlayer1.getOutputStream());
                        player2output = new ObjectOutputStream(socketPlayer2.getOutputStream());                        
                        
                        
                        if(answerPlayer1 == this.correctAlternative){
                            pointsPlayer1 ++;
                            message = "Resposta correta!";
                        } else {
                            message = "Resposta incorreta!";
                        }
                        
                        if(answerPlayer2 == this.correctAlternative){
                            pointsPlayer2 ++;
                            message2 = "Resposta correta!";
                        } else {                            
                            message2 = "Resposta incorreta!";
                        }
                        
                        responseGame = new HashMap<>(); 
                        responseGame.put("result", message);
                        player1output.writeObject(responseGame);
                        responseGame = new HashMap<>(); 
                        responseGame.put("result", message2);// envia a questao para o jogador 1
                        player2output.writeObject(responseGame);
                        questions++;
                        break;
                    case "result":
                       if(pointsPlayer1>pointsPlayer2){
                           message = "Você ganhou!\nSua pontuação foi:"+pointsPlayer1;
                           message2 = "Você perdeu!\nSua pontuação foi:"+pointsPlayer2;
                       }
                       else if(pointsPlayer1<pointsPlayer2){
                           message2 = "Você ganhou!\nSua pontuação foi:"+pointsPlayer2;
                           message = "Você perdeu!\nSua pontuação foi:"+pointsPlayer1;
                       }else{
                           message = "Houve um empate!\nSua pontuação foi:"+pointsPlayer1+"\nPontuação do oponente:"+pointsPlayer2;
                           message2 = "Houve um empate!\nSua pontuação foi:"+pointsPlayer2+"\nPontuação do oponente:"+pointsPlayer1;
                       }
                        
                       
                        responseGame = new HashMap<>(); 
                        responseGame.put("result", "teste");
                        player1output.writeObject(responseGame);
                        responseGame = new HashMap<>(); 
                        responseGame.put("result", "teste");// envia a questao para o jogador 1
                        player2output.writeObject(responseGame);
                        break;
                    case "end":    
                        responseGame = new HashMap<>(); 
                        responseGame.put("end", true);
                        player1output.writeObject(responseGame);
                        responseGame = new HashMap<>(); 
                        responseGame.put("end", true);// envia a questao para o jogador 1
                        player2output.writeObject(responseGame); 
                        end = true;
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
    
    public String responseClient(HashMap<String,Object> responsePlayer1,HashMap<String,Object> responsePlayer2 ){
        String optionPlayer1 = responsePlayer1.get("option").toString();
        String optionPlayer2 = responsePlayer2.get("option").toString();
        
        if(optionPlayer1.equals("getAlternatives") && optionPlayer2.equals("getAlternatives"))
            return "alternatives";
        else if(optionPlayer1.equals("response") && optionPlayer2.equals("response"))
            return "response";        
        else if(optionPlayer1.equals("result") && optionPlayer2.equals("result"))
            return "result";
        else if(optionPlayer1.equals("end") && optionPlayer2.equals("end"))
            return "end";
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
