/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.banksystem.model;

import java.io.Serializable;

/**
 *
 * @author scavenger
 */

public class Client implements Serializable {
    
    private String m_name;
    private Account m_account;
    
    public Client(String name){
        setName(name);
    }
    
    public Client(String name, Account account){
        setName(name);
        setAccount(account);
    }
    
    protected void setAccount(Account account){ 
        m_account = account;
    }
    
    public void setName(String name){ m_name = name; }
    
    public String getName(){ return m_name; }
    
    public Account getAccount(){ return m_account; }
    
    public int getId(){ return this.hashCode();}
    
    @Override
    public String toString(){
        return "Nome: " + m_name +
                "\nConta " + m_account;
    }


}
