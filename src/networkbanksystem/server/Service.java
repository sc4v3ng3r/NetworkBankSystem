/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author scavenger
 */
public class Service implements Runnable{
    private static Service m_instance = null;
    private boolean m_running;
    private Thread m_selfThread;
    private ServerSocket m_serverSocket;
    private int m_servicePort;
    private ArrayList<ClientSession> m_clients;
    
    
    private Service(){
        m_running = false;
        m_servicePort = 1234;
        m_clients = new ArrayList<>();
    }
    
    public static synchronized Service getInstance(){
        if (m_instance == null)
            m_instance = new Service();
        
        return m_instance;
    }
    
    public void setServicePort(int port){
        if (!m_running)
            m_servicePort = port;
    }
    
    public int getServicePort(){ return m_servicePort; }
    
    public void start() {
        if (m_running)
            return;
        
        m_selfThread = new Thread(this);
        m_running = true;
        m_selfThread.start();
       
    }
    
    @Override
    public void run() {
        try {
            m_serverSocket = new ServerSocket(m_servicePort);
            while (m_running) {
                try {
                    Socket clientSocket = m_serverSocket.accept();
                    ClientSession session = new ClientSession(clientSocket);
                    m_clients.add(session);
                    session.start();

                } catch (SocketException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, "SERVICE STOPPED OK!");
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
