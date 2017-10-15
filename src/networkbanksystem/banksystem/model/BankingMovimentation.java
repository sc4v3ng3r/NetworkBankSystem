/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.banksystem.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author scavenger Classe utilizada para representar Operações bancárias que
 * movimentam valores entre contas, DEPÓSITO, SAQUE e TRANSFERÊNCIAS
 * (MOVIMENTAÇÕES BANCÁRIAS).
 */
public class BankingMovimentation extends Operation {

    private double m_value;
    private String m_movimentationType;
    private Client m_currentClient;
    
    private transient final ReentrantLock m_locker = new ReentrantLock();
    public transient static final String BANKING_MOVIMENTATION_DEPOSIT = "DEPÓSITO";
    public transient static final String BANKING_MOVIMENTATION_TRANSFER = "TRANSFERÊNCIA";
    public transient static final String BANKING_MOVIMENTATION_DRAFT = "SAQUE";

    public BankingMovimentation(int Operationtype,
            String movimentationType, double value, Client ownerClient) {
        super(Operationtype);
        this.setMovimentationType(movimentationType);
        this.setValue(value);
        this.setClient(ownerClient);
    }

    public BankingMovimentation(String movimentationType,
            double value, Client owner) {
        super();
        this.setValue(value);
        this.setMovimentationType(movimentationType);
        this.setClient(owner);
        switch (movimentationType) {
            case BANKING_MOVIMENTATION_DEPOSIT:
                this.setType(OPERATION_TYPE_CREDIT);
                break;
            case BANKING_MOVIMENTATION_DRAFT:
                this.setType(OPERATION_TYPE_DEBIT);
                break;
        }
    }

    public void setValue(double value) {
        this.m_value = value;
    }

    public void setMovimentationType(String type) {
        this.m_movimentationType = type;
    }

    private void setClient(Client owner) {
        this.m_currentClient = owner;
    }

    public Client getClient() {
        return this.m_currentClient;
    }

    public String getMovimentationType() {
        return this.m_movimentationType;
    }

    public double getValue() {
        return this.m_value;
    }

    @Override
    public String getDetails() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = new Date(this.getDate());

        return "CLIENTE: " + this.m_currentClient.getName()
                + ";OPERAÇÃO: " + this.getMovimentationType() + "  |  " + this.getType()
                + ";ID OPERAÇÃO: " + this.getId()
                + ";CONTA NÚMERO: " + this.m_currentClient.getAccount().getNumber()
                + ";CONTA TIPO: " + this.m_currentClient.getAccount().getAccountDescription()
                + ";VALOR: " + this.getValue()
                + ";REALIZADA EM: " + this.getDateString();
    }

    @Override
    public boolean execute() {
        Account currentAccount = m_currentClient.getAccount();
        m_locker.lock();

        switch (this.getMovimentationType()) {

            case BANKING_MOVIMENTATION_DEPOSIT:
                if (m_value <= 0) {
                    return false;
                }
                m_locker.lock();
                    try {
                        currentAccount.setBalance(
                            (currentAccount.getBalance() + m_value));

                        currentAccount.addOperation(this);
                        return true;
                    } finally{
                        m_locker.unlock();
                    }
                   
               
            case BANKING_MOVIMENTATION_DRAFT:
                switch (currentAccount.getType()) {

                    case Account.ACCOUNT_NORMAL:
                        m_locker.lock();
                        try {
                            if (currentAccount.getBalance() >= m_value) {
                                currentAccount.setBalance((currentAccount.getBalance() - m_value));
                                currentAccount.addOperation(this);
                                return true;
                            }
                            
                        } finally {
                            m_locker.unlock();
                        }
                        break;
                            

                    case Account.ACCOUNT_SPECIAL:
                        try {
                            double balance = currentAccount.getBalance();
                            if (m_value <= balance) {

                                currentAccount.setBalance(balance - m_value);
                                currentAccount.addOperation(this);
                                return true;
                            } else if (m_value <= ((SpecialAccount) currentAccount).getBalanceTotal()) {
                                double balanceTotal = ((SpecialAccount) currentAccount).getBalanceTotal();
                                currentAccount.setBalance(balance - m_value);
                                currentAccount.addOperation(this);
                                return true;
                              }
                            
                        } finally{
                            m_locker.lock();
                        }
                        break;

                }

            default:
                /*System.out.println("OPERAÇÃO NÃO REALIZADA!: " +
                "CLIENTE: " + this.m_currentAccount.getOwner().getName() +
                "\nOPERAÇÃO: " + this.getMovimentationType() + "  |  " + this.getType() +
                "\nID OPERAÇÃO: " + this.getId() +
                "\nCONTA NÚMERO: " + this.m_currentAccount.getNumber() +
                "\nCONTA TIPO: " + this.m_currentAccount.getAccountDescription() +
                "\nVALOR: " + this.getValue() +
                "\nNÃO REALIZADA EM: " + this.getDateString() + "\n\n");*/
                break;
        }
        return false;
    }
    
    @Override 
    public String toString(){
        
        return "CLIENTE: " + this.m_currentClient.getName()
                + ";OPERAÇÃO: " + this.getMovimentationType() + "  |  " + this.getType()
                + ";ID OPERAÇÃO: " + this.getId()
                + ";CONTA NÚMERO: " + this.m_currentClient.getAccount().getNumber()
                + ";CONTA TIPO: " + this.m_currentClient.getAccount().getAccountDescription()
                + ";VALOR: " + this.getValue()
                + ";REALIZADA EM: " + this.getDateString();   
    }
}
