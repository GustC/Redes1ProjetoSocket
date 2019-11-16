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
 
    private byte[] buf;
 
    public EchoClient() throws SocketException, UnknownHostException, IOException{
        
        address = InetAddress.getByName("localhost");
        socket = new Socket("localhost", 8080);        
    }
    
    public boolean isConnectedToServer(){
        return socket.isConnected();
    }

    
 
    public String sendEcho(String msg) throws IOException{
        buf = msg.getBytes();
        System.out.println("Client - send - "+msg);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
//        socket.send(packet);
        byte[] buf_temp = new byte[256];
        packet = new DatagramPacket(buf_temp, buf_temp.length);
//        socket.receive(packet);
        String received = new String(
          packet.getData(), 0, packet.getLength());
        return received;
    }
    
 
    public void close() {
        buf = "".getBytes();
//        socket.close();
    }
}