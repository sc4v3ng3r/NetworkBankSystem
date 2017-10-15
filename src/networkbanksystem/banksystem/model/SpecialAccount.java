/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.banksystem.model;

/**
 *
 * @author scavenger
 */
public class SpecialAccount extends Account {
    private double m_credit;
    
    public SpecialAccount(double credit) {
        super(ACCOUNT_SPECIAL);
        setTypeDescription(ACCOUNT_TYPE_SPECIAL);
        setCredit(credit); 
    }
    
    public final void setCredit(double credit){ m_credit = credit;}
    
    public final double getCredit(){ return m_credit; }
    
    public double getBalanceTotal() {
        double credit = getCredit();
       // m_locker.lock();
       // try{
            return credit + super.getBalance();
        //} finally{
         //   m_locker.unlock();
        //}
    }
    
    @Override
    public String toString(){
        return  "Número: " + getNumber() + ";" +
                "Tipo: " + getTypeDesciription() + ";" +
                "Saldo Total: " + getBalanceTotal() + ";" +
                "Saldo Parcial:" + getBalance() + ";" + 
                "Crédito: " + getCredit();
    }
}
