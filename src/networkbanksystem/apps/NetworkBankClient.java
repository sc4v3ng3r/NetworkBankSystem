/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.apps;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkbanksystem.banksystem.model.Request;
import networkbanksystem.banksystem.model.Resources;

/**
 *
 * @author scavenger
 */
public class NetworkBankClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            
            Scanner reader = new Scanner(in);
            Scanner keyboard = new Scanner(System.in);
            boolean running = true;
            PrintWriter writer = new PrintWriter(out);
            String[] menu;
            
            Request request = new Request(Request.REQUEST_TYPE_GET,
                    Resources.RESOURCE_MAIN_MENU, null);
            
            sendRequest(writer, request);
            
            while(running){
                // aguarda resposta do server!
                request.parseToRequest( reader.nextLine() );
                
                switch(request.getResource()){
                    
                    case Resources.RESOURCE_MAIN_MENU:
                        showMainMenu(request.getDataFormatted());
                        int opt = Integer.parseInt( keyboard.nextLine() );
                        request.setType(Request.REQUEST_TYPE_GET);
                        //switch de opcoes do menu principal!!
                        switch(opt){
                            case 0:
                                request.setType(Request.REQUEST_TYPE_GET);
                                request.setResource(Resources.RESOURCE_DISCONNECT);
                                sendRequest(writer, request);
                                socket.close();
                                return;
                            case 1:
                                request.setResource(Resources.RESOURCE_NEW_ACCOUNT);
                                request.setData(null);
                                break;
                            case 2:
                                request.setResource(Resources.RESOURCE_SHOW_ACCOUNTS);
                                request.setData(null);
                                break;
                            case 3:
                                request.setResource(Resources.RESOURCE_DEPOSIT);
                                request.setData(null);
                                break;
                            case 4:
                                request.setResource(Resources.RESOURCE_DRAFT);
                                request.setData(null);
                                break;
                            case 5:
                                request.setResource(Resources.RESOURCE_TRANSFER);
                                request.setData(null);
                                break;
                            case 6:
                                request.setResource(Resources.RESOURCE_QUERY);
                                request.setData(null);
                                break;
                            case 7:
                                request.setResource(Resources.RESOURCE_EXTRACT);
                                request.setData(null);
                                break;

                        }
                        sendRequest(writer, request);
                        /*envia nova requisicao ao servidor!*/
                        break;
                        
                    case Resources.RESOURCE_NEW_ACCOUNT:
                        
                        menu = request.getRawData().split(";");
                        
                        System.out.print(menu[0]);
                        String nome = keyboard.nextLine();
                        //System.out.println("");
                        System.out.println(menu[1]);
                        System.out.println(menu[2]);
                        
                        System.out.print("-> ");
                        String contaTipo = keyboard.nextLine();
                        
                        //System.out.println("Enviando" + nome + contaTipo);
                        request.setType(Request.REQUEST_TYPE_SEND);
                        request.setData(nome + ";" + contaTipo);
                        
                        sendRequest(writer, request);
                        /*exibe o menu*/
                        /*coleta os dados*/
                        /*envia para o servidor*/
                        break;
                    case Resources.RESOURCE_DEPOSIT:
                        String conta, valor;
                        menu = request.getRawData().split(";");
                        
                        System.out.print(menu[0]);
                        conta = keyboard.nextLine();
                        
                        System.out.print(menu[1]);
                        valor = keyboard.nextLine();
                        
                        request.setType(Request.REQUEST_TYPE_SEND);
                        request.setData( conta + ";" + valor);
                        sendRequest(writer, request);
                        break;
                        
                    case Resources.RESOURCE_DRAFT:
                        menu = request.getRawData().split(";");
                        System.out.print(menu[0]);
                        
                        conta = keyboard.nextLine();
                        
                        System.out.print(menu[1]);
                        valor = keyboard.nextLine();
                        
                        request.setType(Request.REQUEST_TYPE_SEND);
                        request.setData( conta + ";" + valor);
                        sendRequest(writer, request);
                        break;
                    
                    case Resources.RESOURCE_TRANSFER:
                        menu = request.getRawData().split(";");
                        System.out.print(menu[0]);
                        String from = keyboard.nextLine();
                        
                        System.out.print(menu[1]);
                        
                        String to = keyboard.nextLine();
                        
                        System.out.println(menu[2]);
                        valor = keyboard.nextLine();
                        
                        request.setType(Request.REQUEST_TYPE_SEND);
                        request.setResource(Resources.RESOURCE_TRANSFER);
                        request.setData(from + ";" + to + ";" + valor);
                        sendRequest(writer, request);
                        break;
                    
                    case Resources.RESOURCE_SHOW_ACCOUNTS:
                        String data = request.getRawData();
                        if (data!= null){
                            menu = data.split(";");
                            for(int i=0; i < menu.length; i++)
                                System.out.println(menu[i]);
                        }
                        
                        request.setType(Request.REQUEST_TYPE_GET);
                        request.setResource(Resources.RESOURCE_MAIN_MENU);
                        sendRequest(writer, request);
                        break;
                    
                    case Resources.RESOURCE_QUERY:
                        System.out.print( request.getDataFormatted() );
                        conta = keyboard.nextLine();
                        
                        request.setData(Request.REQUEST_TYPE_SEND);
                        request.setData(conta);
                        sendRequest(writer, request);
                        break;
                        
                    case Resources.RESOURCE_QUERY_RESPONSE:
                        menu = request.getRawData().split(";");
                        for(int i=0; i < menu.length; i++){
                            System.out.println(menu[i]);
                        }
                        
                        request.setType(Request.REQUEST_TYPE_GET);
                        request.setResource(Resources.RESOURCE_MAIN_MENU);
                        sendRequest(writer, request);
                        break;  
                    
                    case Resources.RESOURCE_EXTRACT:
                        menu = request.getRawData().split(";");
                        System.out.print(menu[0]);
                        conta = keyboard.nextLine();
                        request.setData(conta);
                        request.setResource(Resources.RESOURCE_EXTRACT);
                        request.setType(Request.REQUEST_TYPE_SEND);
                        sendRequest(writer, request);
                        break;
                    
                    case Resources.RESOURCE_EXTRACT_RESPONSE:
                        menu = request.getRawData().split(";");
                        for(int i = 0; i < menu.length; i++){
                            System.out.println(menu[i]);
                        }
                        
                        request.setType(Request.REQUEST_TYPE_GET);
                        request.setResource(Resources.RESOURCE_MAIN_MENU);
                        sendRequest(writer, request);
                        break;
                }
                
            }// end of loop while
            
        } catch (IOException ex) {
            Logger.getLogger(NetworkBankClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void showMainMenu(String menu){
        System.out.print(menu);
    }
    
    private static void sendRequest(PrintWriter writer, Request request){
        writer.println(request);
        writer.flush();
    }
}
