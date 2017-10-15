/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.banksystem.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkbanksystem.banksystem.model.Client;

/**
 *
 * @author scavenger
 */
public class ClientDataBase implements Serializable {
    private transient static ClientDataBase m_instance = null;
    
    transient private final ReentrantLock m_locker = new ReentrantLock();
    private transient final String FILE_DB = "database.db";
    
    private ArrayList<Client> m_clientList;
    
    
    private ClientDataBase() {
        m_clientList = new ArrayList<>();
        /*verifica se existe banco de dados!*/
        File file = new File(FILE_DB);
        
        if (file.exists() ){
             load();
        } else {
            try {
                if (!file.createNewFile())
                    System.out.println("Arquivo nao criado!");
                else
                    System.out.println("Arquivo criado com sucesso!");
            } catch (IOException ex) {
                Logger.getLogger(ClientDataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
    }
    
    public synchronized static ClientDataBase getInstance() {
        if (m_instance == null)
            m_instance = new ClientDataBase();
        
        return m_instance;
    }
    
    public int getClientsNumber() {
        return m_clientList.size();
    }
    
    public void addClient(Client c) {
        try{
            m_locker.lock();
            /*Pode ser um armengue, mas eh a mesma ideia do autoIncrement dos SGBD's*/
            System.out.println("ClientDataBase::addClient() " + c);
            c.getAccount().setNumber(m_clientList.size()+1);
            
            m_clientList.add(c);
            saveState();
        } finally{
            m_locker.unlock();
        }
        
    }
    
    public Client getClientAt(int index){
        try {
            m_locker.lock();
            return m_clientList.get(index);
        } finally{
            m_locker.unlock();
        }
    }
    
    public Client getClientByAccountNumber(long account){
        Client c = null;
        m_locker.lock();
        try {
            for(int i=0; i < m_clientList.size(); i++){
                if (m_clientList.get(i).getAccount().getNumber() == account){
                    c = m_clientList.get(i);
                    break;
                }
            }
        } finally{
            m_locker.unlock();
          return c;
        }
        
    }
    
    private void saveState(){
        m_locker.lock();
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.FILE_DB));
            out.writeObject(m_clientList);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientDataBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            m_locker.unlock();
        }
    }
    public void save(){
        saveState();
    }
    
    private void load(){
        System.out.println("CARREGANDO ARQUIVO!");
        m_locker.lock();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(FILE_DB));
            try {
                m_clientList = (ArrayList<Client>) input.readObject();
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientDataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientDataBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            m_locker.unlock();
        }
    }
}