package game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
    Socket socket;
    private InetAddress address;    
 
 
    public EchoClient() throws SocketException, UnknownHostException, IOException{
        
        address = InetAddress.getByName("localhost");
        socket = new Socket("localhost", 8080);        
    }
    
    public boolean isConnectedToServer(){
        return socket.isConnected();
    }

}