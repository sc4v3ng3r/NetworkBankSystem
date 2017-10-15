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
public class OperationGenerator {
    

    public OperationGenerator() { }
    
    public static BankingMovimentation createDraft(double value, Client client){
        BankingMovimentation draft = new BankingMovimentation(
                BankingMovimentation.BANKING_MOVIMENTATION_DRAFT, 
                value, client);
        
        //draft.setId( (long) draft.hashCode());
        
        return draft;
    }
    
    public static Transfer createTransfer(Client from, double value, Client to){
        Transfer t = new Transfer(from, to, value);
        return t;
    }
    
    public static BankingMovimentation createDeposit(Client client, double value){
        BankingMovimentation deposit = new BankingMovimentation(
                             BankingMovimentation.BANKING_MOVIMENTATION_DEPOSIT,
                             value, client);
        
        //deposit.setId( (long) deposit.hashCode());
        return deposit;
    }
     
    public static BankingOperation createQuery(Client client){
        BankingOperation query = new BankingOperation(
                 Operation.OPERATION_TYPE_QUERY, client);
        
        //query.setId( (long) query.hashCode());
        return query;
     }
    
    
    public static Account createAccount(Client client, byte accountType, double credit){
        BankingOperation op = null;
        Account account = null;
        
        switch (accountType) {
            case Account.ACCOUNT_NORMAL:
                account = new NormalAccount();

                op = new BankingOperation(
                        Operation.OPERATION_TYPE_CREATION, client);
                account.addOperation(op);
                break;

            //return op;
            case Account.ACCOUNT_SPECIAL:
                account = new SpecialAccount(credit);
                op = new BankingOperation(
                        Operation.OPERATION_TYPE_CREATION, client);
                account.addOperation(op);
                break;
                
            default:
                break;
        }
        
        client.setAccount(account);
        return account;
    }
    
}
