/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.banksystem.model;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger
 * Essa classe é utilizada como representação de operações bancárias 
 * que não são MOVIMENTAÇÕES BANCÁRIAS, ou seja, não são operações
 * que envolvem VALORES em suas execuções, tais como CRIAR UMA CONTA,
 * SALDO DE UMA CONTA.
 */
public class BankingOperation extends Operation {
    private final Client m_client;
    private double m_oldBalance;
    //private final ReentrantLock m_locker = new ReentrantLock();
    
    public BankingOperation(int type, Client client) {
        super(type);
        m_client = client;
    }
    
    public Client getClient(){ return m_client; }
     
    @Override
    public String getDetails() {
        m_locker.lock();
        try{
            Account account = this.m_client.getAccount();
            switch(this.getType()){
                case OPERATION_TYPE_QUERY: 
                
                    switch(this.m_client.getAccount().getType()){
                        case Account.ACCOUNT_NORMAL:
                            return
                                "CLIENTE: " + this.m_client.getName() +
                                ";OPERAÇÃO: " + this.getTypeDescription() +
                                ";ID OPERAÇÃO: " + this.getId() +
                                ";CONTA NÚMERO: " + account.getNumber() +
                                ";CONTA TIPO: " + account.getAccountDescription() +
                                ";SALDO: " + this.m_oldBalance + //armengue! todo operacao guarda o estado anterior do saldo da conta!
                                ";EXECUTADA EM: " + this.getDateString();
                    
                        case Account.ACCOUNT_SPECIAL:
                            double credit = ((SpecialAccount)account).getCredit();
                            return
                                "CLIENTE: " + this.m_client.getName() +
                                ";OPERAÇÃO: " + this.getTypeDescription() +
                                ";ID OPERAÇÃO: " + this.getId() +
                                ";CONTA NÚMERO: " + account.getNumber() +
                                ";CONTA TIPO: " + account.getAccountDescription() +
                                ";CRÉDITO: " +  credit +
                                ";SALDO PARCIAL: " + this.m_oldBalance +
                                ";SALDO TOTAL: " +  (credit + m_oldBalance) +//armengue! todo operacao guarda o estado anterior do saldo da conta!
                                ";EXECUTADA EM: " + this.getDateString();
                    }
                
                default:
                    return 
                    "CLIENTE: " + m_client.getName() +
                    ";OPERAÇÃO: " + this.getTypeDescription() +
                    ";ID OPERAÇÃO: " + this.getId() +
                    ";CONTA NÚMERO: " + account.getNumber() +
                    ";CONTA TIPO: " + account.getAccountDescription() +
                    ";EXECUTADA EM: " + this.getDateString();
            }
        }
        finally {
            m_locker.unlock();
        }
    }
    
    @Override
    public boolean execute() {
        m_locker.lock();
        try {
            m_oldBalance = m_client.getAccount().getBalance();
            m_client.getAccount().addOperation(this);
        } finally{
            m_locker.unlock();
        }
        return true;
    }
    
}