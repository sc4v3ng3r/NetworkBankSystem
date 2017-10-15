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
public class NormalAccount extends Account {
    
    public NormalAccount() {
        super(ACCOUNT_NORMAL);
        setTypeDescription(ACCOUNT_TYPE_NORMAL);
    }
    
    
}
