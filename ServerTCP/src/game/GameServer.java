/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Casa
 */
public class GameServer {
    static String[] palavras = new String[]{"bola","tapa","olho","capa","predio","quadrado"};
    static public String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
    
    static public String getRandom(){
        String response; 
        Random r = new Random();
        int indexRand = r.nextInt(palavras.length);
        response = palavras[indexRand];
        return response;
    }
    
    
}
