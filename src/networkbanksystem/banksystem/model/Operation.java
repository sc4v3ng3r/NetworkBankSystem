/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.banksystem.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 */

public abstract class Operation implements Serializable {
    private String m_operationTypeDescription;
    private int m_operationType;
    private long m_date;
    protected final ReentrantLock m_locker = new ReentrantLock();
    //protected long m_executionTime;
    
    public transient static final int OPERATION_TYPE_CREATION = 1;
    public transient static final int OPERATION_TYPE_QUERY = 2;
    public transient static final int OPERATION_TYPE_CREDIT = 3;
    public transient static final int OPERATION_TYPE_DEBIT = 4;
    
    public transient static final String OPERATION_TYPE_DESCRIPTION_CREATION = "CRIAÇÃO";
    public transient static final String OPERATION_TYPE_DESCRIPTION_QUERY = "CONSULTA";
    public transient static final String OPERATION_TYPE_DESCRIPTION_CREDIT = "CRÉDITO";
    public transient static final String OPERATION_TYPE_DESCRIPTION_DEBIT = "DÉBITO";
    
    public Operation(int type) {
        setDate(System.currentTimeMillis());
        setType(type);
        
        switch(type){
            case OPERATION_TYPE_CREATION:
                setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_CREATION);
                break;
            case OPERATION_TYPE_CREDIT:
                setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_CREDIT);
                break;
            case OPERATION_TYPE_DEBIT:
                setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_DEBIT);
                break;
            case OPERATION_TYPE_QUERY:
                setTypeDesctiption(OPERATION_TYPE_DESCRIPTION_QUERY);
                break;
            default:
                break;
        }
        
    }
    
    public Operation() {
        setDate(System.currentTimeMillis());
    }
    
    public final void setTypeDesctiption(String type ){ m_operationTypeDescription = type; }
    private void setDate(long date){ m_date = date; }
    
    public final String getTypeDescription(){ return m_operationTypeDescription; }
    public final long getDate(){ return m_date; }
    
    public final String getDateString(){ 
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = new Date(m_date);
        return dateFormatter.format(date); 
    }
    
    public int getType(){ return m_operationType;}
    public void setType(int type){ m_operationType = type;}
    //public final String getDescription(){ return m_description; }
    
    public final long getId(){ return this.hashCode(); }
    
    public abstract boolean execute();
    
    public abstract String getDetails();
    
    @Override
    public String toString(){
        return "Operation::toString NÃO IMPLEMENTADO AINDA!";
    }
}