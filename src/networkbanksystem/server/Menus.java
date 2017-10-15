/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.server;

/**
 *
 * @author scavenger
 */
public class Menus {
    public static final String MAIN_MENU = 
                      "#############################;" + /*28#*/
                      "#      Network Banking      #;" +
                      "#############################;" +
                      "# [1] Criar conta           #;" +
                      "# [2] Visualizar contas     #;" + 
                      "# [3] Depósito              #;" +
                      "# [4] Saque                 #;" +
                      "# [5] Transferência         #;" +
                      "# [6] Saldo                 #;" +
                      "# [7] Extrato               #;" +
                      "# [0] Sair                  #;" +
                      "#############################;" +
                      "Opção -> ";
    
    /**
     *  Menu estilo< /br>
     * 
     * Número da conta: " +
       Valor: "
     * 
     */
    public static final String NUMBER_VALUE_MENU = 
            "Número da conta: ;" +
            "Valor: ";
    /**
     * Menu para criação de uma nova conta
     */
    public static final String NEW_ACCOUNT_MENU = 
            "Cliente Nome: ;" +
            "[1] CONTA NORMAL;" +
            "[2] CONTA ESPECIAL;" + 
            "Opção -> ";
    /**
     * Menu para operação de transferência
     */
    public static final String TRANSFER_MENU =
            "De Conta: ;" +
            "Para Conta: ;" +
            "Valor: "; 
    /**
     * Utilizado para saldo e extrato
     */
    public static final String ACCOUNT_NUMBER_MENU = 
            "Número da Conta: ";
}
