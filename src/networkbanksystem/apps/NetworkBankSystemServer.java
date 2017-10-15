/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.apps;

import networkbanksystem.server.Service;

/**
 *
 * @author scavenger
 */
public class NetworkBankSystemServer {
    public static final void main(String args[]){
        Service service = Service.getInstance();
        service.start();
        System.out.println("Server started!");
    }
}
