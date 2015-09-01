/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alberto
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    static final int PORT = 5000;
    public ArrayList<SocketInfo> ClientsList = new ArrayList<>();
    PrintConnectedClients printThreads = null;
    public static void main(String[] args) {
        Server thisServer = new Server();
        thisServer.start();    
    }
    
    public void start()
    {
        // this socket will be used so the server is always listening for incoming
        // connections
        ServerSocket listener = null;
        // this will be the socket the client and the server connect to and will
        // be handled in a thread
        Socket socket = null;
        //These scheduledExecutors are used to display the connected clients
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new PrintConnectedClients(), 0, 1, TimeUnit.SECONDS);
        
        try
        {
            listener = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(listener.getInetAddress());
        while(true)
        {
            System.out.println(listener.getInetAddress());
            try {
                System.out.println(listener.getInetAddress());
                socket = listener.accept();
            } catch (IOException e){
                System.out.println("I/O error: " + e);
                e.printStackTrace();
            }
            new ConnectClient(socket);
        }
    }
    
    private class ConnectClient extends Thread
    {
        private Socket soc;
        
        public ConnectClient(Socket s)
        {
            soc = s;
            ClientsList.add(new SocketInfo(soc));
        }
    }
    
    public class PrintConnectedClients implements Runnable 
    {
        @Override
        public void run()
        {
            Date date = new Date();
            System.out.println("-----------" + date.toString() + "----------");
            
            if(ClientsList.isEmpty())
                System.out.println("NO CONNECTIONS");
            else
            {
                for(int i = 0; i < ClientsList.size(); i++)
                {
                    ClientsList.get(i).PrintSockInfo();
                }
            }
        }
    }
    
    private class SocketInfo
    {
        public InetAddress addr;
        public InetAddress local_addr;
        public int port;
        public int local_port;
        public SocketInfo(Socket soc)
        {
            addr       = soc.getInetAddress();
            local_addr = soc.getLocalAddress();
            port       = soc.getPort();
            local_port = soc.getPort();
        }
        
        public void PrintSockInfo()
        {
            System.out.println("Address: " + addr.getHostAddress() + "\t Port: " + port);
        }
    }
    
}
