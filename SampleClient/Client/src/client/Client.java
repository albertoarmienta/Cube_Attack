
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alberto
 */
public class Client {

    static public String SERVER_NAME = "beartoes.ddns.net";
    static public int PORT = 50000;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client c = new Client();       
        try {
            c.start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() throws UnknownHostException, IOException
    {
        InetSocketAddress addrs = new InetSocketAddress(SERVER_NAME, PORT);
        Socket soc = new Socket(SERVER_NAME, PORT);
        /*
        try {
            soc.connect(addrs);
            System.out.println("connected");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
                */

        while(true)
        {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
