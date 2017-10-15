/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.banksystem.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 */

public abstract class Account implements Serializable {
    
    //private static long m_accountCounter;
    
    private long m_number;
    private double m_value;
    private byte m_type;
    
    private String m_typeDescription;
    private ArrayList<Operation> m_operationList;
    
    protected transient final ReentrantLock m_locker;
    
    public transient static final byte ACCOUNT_NORMAL = 0;
    public transient static final byte ACCOUNT_SPECIAL = 1;
    
    public transient static final String ACCOUNT_TYPE_NORMAL = "NORMAL";
    public transient static final String ACCOUNT_TYPE_SPECIAL = "SPECIAL";
    
    public Account(byte type) {
        setType(type);
        m_operationList = new ArrayList<>();
        m_locker = new ReentrantLock();
    }
    
    
    public final void setType(byte type){ m_type = type; }
    
    public final void setBalance(double value) {
       // m_locker.lock();
        //try {
            m_value = value; 
        //}
        //finally{
         //   m_locker.unlock();
        //}
    }
    
    public void setNumber(long number){ m_number = number; }
    
    public final long getNumber(){ return m_number; }
    
    public double getBalance() {
        //m_locker.lock();
        //try{
            return m_value; 
        //}
        //finally{
           // m_locker.unlock();
        //}
    }
    
    public final byte getType(){ return m_type; }
    
    public final String getAccountDescription(){
        if (m_type == ACCOUNT_NORMAL)
            return "NORMAL";
        else return "ESPECIAL";
    }
    
    public final void setTypeDescription(String desc){
        m_typeDescription = desc;
    }
    
    public String getTypeDesciription(){
        return m_typeDescription;
    }
    
    @Override
    public String toString(){
        return "Número: " + m_number + ";" +
                "Tipo: " + m_typeDescription + ";" +
                "Saldo: " + m_value + ";";
    }
    
    public void addOperation(Operation op){
        m_operationList.add(op);
    }
    
    public Operation getOperationAt(int index){
        return m_operationList.get(index);
    }
    
    public int OperationsTotal(){
        return m_operationList.size();
    }
    
}
