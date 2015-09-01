
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alberto
 */
public class Client {

    static public String SERVER_NAME = "127.0.0.1";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client c = new Client();       
        c.start();
    }

    public void start()
    {
        InetSocketAddress addrs = new InetSocketAddress(SERVER_NAME, 5000);
        Socket soc = null;
        soc = new Socket();
        try {
            soc.connect(addrs);
            System.out.println("connected");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

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
