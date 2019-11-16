/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttcp;

import game.GameRunning;
import java.util.Scanner;

/**
 *
 * @author Casa
 */
public class ClientTCP {

    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        GameRunning x = new GameRunning();

        int port = 8080;
        String host = "localhost";
        try{
            int op;
            System.out.println("1 - Jogar\n2 - Sair");
            op = scanner.nextInt();
            if(op == 1){                
//                Client client = new Client(host,port);
                x.setup();
                x.loading();
                x.startGame();
//                x.SendFromServer();
//                x.tearDown();
            } 
                
        } catch (Exception e ){
            System.err.println(e.getMessage());
        }
    }
    
}
