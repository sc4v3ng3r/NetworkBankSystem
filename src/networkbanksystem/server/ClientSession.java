/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkbanksystem.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkbanksystem.banksystem.controler.ClientDataBase;
import networkbanksystem.banksystem.model.Account;
import networkbanksystem.banksystem.model.Client;
import networkbanksystem.banksystem.model.NormalAccount;
import networkbanksystem.banksystem.model.Operation;
import networkbanksystem.banksystem.model.OperationGenerator;
import networkbanksystem.banksystem.model.Request;
import networkbanksystem.banksystem.model.Resources;
import networkbanksystem.banksystem.model.SpecialAccount;

/**
 *
 * @author scavenger
 */
public class ClientSession implements Runnable{
    private Socket m_clientSocket;
    private InputStream m_input;
    private OutputStream m_output;
    private Scanner m_inputReader;
    private PrintWriter m_outputWriter;
    private Thread m_selfThread;
    
    public ClientSession(Socket client){
        try {
            m_clientSocket = client;
            m_input = m_clientSocket.getInputStream();
            m_inputReader = new Scanner(m_input);
            
            m_output = m_clientSocket.getOutputStream();
            m_outputWriter = new PrintWriter(m_output);
            
        } catch (IOException ex) {
            Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void start() {
        m_selfThread = new Thread(this);
        m_selfThread.start();
        
    }
    
    @Override
    public void run() {
        /*aguarda requisicao do cliente!*/
        while (true) {
            try {
                Request request = waitRequest();
                //System.out.println("Server recebeu " + data);
                if (request == null){
                    
                    return;
                    /*enviar alerta de desconeccao!*/
                    /*desconectar cliente+*/
                }
                
                
                /*Interpreta a requisicao*/
                /*Realiza o processamento da requisicao*/
                switch (request.getType()) {
                    case Request.REQUEST_TYPE_GET:
                        
                        switch (request.getResource()) {
                            case Resources.RESOURCE_MAIN_MENU:
                                /*Vai enviar o mainMenu na requisicao!*/
                                request.setType(Request.REQUEST_TYPE_SEND);
                                request.setData(Menus.MAIN_MENU);
                                break;
                                
                            case Resources.RESOURCE_NEW_ACCOUNT:
                                request.setType(Request.REQUEST_TYPE_SEND);
                                request.setData(Menus.NEW_ACCOUNT_MENU);
                                break;
                                
                            case Resources.RESOURCE_DEPOSIT:
                                request.setType(Request.REQUEST_TYPE_SEND);
                                request.setData(Menus.NUMBER_VALUE_MENU);
                                break;
                                
                            case Resources.RESOURCE_DRAFT: // saque
                                request.setType(Request.REQUEST_TYPE_SEND);
                                request.setData(Menus.NUMBER_VALUE_MENU);
                                break;
                                
                            case Resources.RESOURCE_TRANSFER:
                                request.setType(Request.REQUEST_TYPE_SEND);
                                request.setData(Menus.TRANSFER_MENU);
                                break;
                                
                            case Resources.RESOURCE_QUERY: // consulta a saldo!
                                request.setType(Request.REQUEST_TYPE_SEND);
                                request.setData(Menus.ACCOUNT_NUMBER_MENU);
                                break;
                                
                            case Resources.RESOURCE_EXTRACT: // extrato da conta!
                                request.setType(Request.REQUEST_TYPE_SEND);
                                request.setData(Menus.ACCOUNT_NUMBER_MENU);
                                break;
                                
                            case Resources.RESOURCE_SHOW_ACCOUNTS:
                                String data = "";
                                ClientDataBase db = ClientDataBase.getInstance();
                                request.setType(Request.REQUEST_TYPE_SEND);
                                
                                request.setData(getClientsAccounts());
                                break;
                                
                            case Resources.RESOURCE_DISCONNECT:
                                request.setType(Request.REQUEST_TYPE_SEND);
                                m_clientSocket.close();
                                return;
                                //request.setData("OK");
                                /*
                                disconectar socket do client!
                                close client session!
                                */
                                //break;
                                
                        } // end switch getResource
                        
                        break;/// break de REQUEST_TYPE_GET
                        
                        /*Caso o cliente tenha enviado dados!*/
                    case Request.REQUEST_TYPE_SEND:
                        switch( request.getResource() ) {
                            /*Aqui interpretamos as respostas enviadas
                            pelo cliente e enviamos os dados ou mensagem de erro
                            */
                            case Resources.RESOURCE_NEW_ACCOUNT:
                                String data[] = request.getRawData().split(";");
                                String name = data[0];
                                int type = Byte.parseByte( data[1]);
                                System.out.println("RECEBEMOS " + data);
                                
                                switch(type){
                                    case 1: // conta normal
                                        System.out.println("CRIANDO CONTA NORMAL!");
                                        Client client = new Client(data[0]);
                                        OperationGenerator.createAccount( client, Account.ACCOUNT_NORMAL, 0 );
                                        
                                        ClientDataBase.getInstance().addClient(client);
                                        
                                        request.setData(Menus.MAIN_MENU);
                                        request.setResource(Resources.RESOURCE_MAIN_MENU);
                                        request.setType(Request.REQUEST_TYPE_SEND);
                                        ClientDataBase.getInstance().save();
                                        //sendRequest(request);
                                        //ENVIAR
                                        //operacao realizada com sucesso!
                                        break;
                                        
                                    case 2: // conta especial
                                        System.out.println("CRIANDO CONTA ESPECIAL!");
                                        client = new Client(name);
                                        OperationGenerator.createAccount(client, Account.ACCOUNT_SPECIAL, 2000);
                                        
                                        ClientDataBase.getInstance().addClient(client);
                                        request.setData(Menus.MAIN_MENU);
                                        request.setResource(Resources.RESOURCE_MAIN_MENU);
                                        request.setType(Request.REQUEST_TYPE_SEND);
                                        ClientDataBase.getInstance().save();

                                        //sendRequest(request);
                                        break;
                                    
                                    default:
                                        request.setData(Menus.MAIN_MENU);
                                        request.setResource(Resources.RESOURCE_MAIN_MENU);
                                        request.setType(Request.REQUEST_TYPE_SEND);
                                        break;
                                        
                                }
                                
                                break;
                                
                            case Resources.RESOURCE_DEPOSIT:
                                data = request.getRawData().split(";");
                                long number = Long.parseLong(data[0]);
                                double valor = Double.parseDouble(data[1]);
                                
                                if (number > ClientDataBase.getInstance().getClientsNumber()){
                                    prepareMainMenuRequest(request);
                                    break;
                                }
                                
                                boolean status = OperationGenerator.createDeposit(
                                        ClientDataBase.getInstance().getClientByAccountNumber(number),
                                        valor).execute();
                                prepareMainMenuRequest(request);
                                ClientDataBase.getInstance().save();
                                break;
                                
                            case Resources.RESOURCE_DRAFT: // saque
                                data = request.getRawData().split(";");
                                number = Long.parseLong(data[0]);
                                if (number > ClientDataBase.getInstance().getClientsNumber()){
                                    prepareMainMenuRequest(request);
                                    break;
                                }
                                
                                valor = Double.parseDouble(data[1]);
                                status = OperationGenerator.createDraft(valor,
                                        ClientDataBase.getInstance().getClientByAccountNumber(number)).execute();
                                prepareMainMenuRequest(request);
                                
                                ClientDataBase.getInstance().save();
                                break;
                                
                            case Resources.RESOURCE_TRANSFER:
                                /*from, to, value*/
                                data = request.getRawData().split(";");
                                
                                ClientDataBase db = ClientDataBase.getInstance();
                                long from,to;
                                from = Long.parseLong(data[0]);
                                to = Long.parseLong(data[1]);
                                if ( (from > db.getClientsNumber()) || (to > db.getClientsNumber()) ){
                                    prepareMainMenuRequest(request);
                                    break;
                                }
                                
                                //System.out.println("Valor: " + data[2]);
                                if (OperationGenerator.createTransfer(
                                        db.getClientByAccountNumber( Integer.parseInt(data[0]) ),
                                        Double.parseDouble(data[2]),
                                        db.getClientByAccountNumber(Integer.parseInt(data[1]) ) ).execute() ){
                                    db.save();
                                }
                                prepareMainMenuRequest(request);
                                break;
                                
                            case Resources.RESOURCE_QUERY: // consulta a saldo!
                                number = Long.parseLong( request.getRawData() );
                                db = ClientDataBase.getInstance();
                                if (number > db.getClientsNumber()){
                                    prepareMainMenuRequest(request);
                                    break;
                                }
                                
                                Client client = db.getClientByAccountNumber(number);
                                //System.out.println("SALDO PRIPRIETARIO " + client.getName());
                                
                                Operation query = OperationGenerator.createQuery(client);
                                query.execute();
                                
                                 // query = client.getAccount().toString();
                                request.setResource(Resources.RESOURCE_QUERY_RESPONSE);
                                request.setData(query.getDetails());
                                break;
                                
                            case Resources.RESOURCE_EXTRACT:
                                number = Long.parseLong( request.getRawData() );
                                
                                Account account = ClientDataBase.getInstance().
                                        getClientByAccountNumber(number).getAccount();
                                
                                request.setType(Request.REQUEST_TYPE_SEND);
                                request.setResource(Resources.RESOURCE_EXTRACT_RESPONSE);
                                
                                String extract = "";
                                int operationsNumber = account.OperationsTotal();
                                
                                for(int i=0; i < operationsNumber; i++){
                                    extract += account.getOperationAt(i).getDetails() + ";;";
                                }
                                
                                request.setData(extract);
                                break;
                                
                            case Resources.RESOURCE_DISCONNECT:
                                //request.setType(Request.REQUEST_TYPE_SEND);
                                request.setData("OK");
                                break;
                        }
                        break; // break de REQUEST_TYPE_SEND
                }
                
                sendRequest(request);
                /*ENvia requisicao ao cliente!*/
            } catch (IOException ex) {
                Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private Request waitRequest(){
        System.out.print("aguardando REQUEST!");
        Request request = null;
        try {
            request = new Request (m_inputReader.nextLine());
            
        } catch(NoSuchElementException ex){
            System.out.println("Cliente desconectou!");
            closeClientConnection();
        }
        
        return request;
    }
    
    private void closeClientConnection() {
        try {
            m_input.close();
            m_output.close();
            m_inputReader.close();
            m_outputWriter.close();
            m_clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendRequest(Request request){
        System.out.println("ClientSession::sendRequest() " + request);
        m_outputWriter.println(request);
        m_outputWriter.flush();
    }
    
    private String getClientsAccounts(){
        String data = "";
        ClientDataBase db = ClientDataBase.getInstance();
        int size = db.getClientsNumber();
        if (size > 0){
            for(int i =0; i < size; i++){
                Client c = db.getClientAt(i);
                data+= "Conta Número: " + c.getAccount().getNumber() + " Cliente: " + c.getName() + ";";
            }
        }
        return data;
    }
    
   private boolean executeNewAccountOperation(Request clientRequest){
       String data = clientRequest.getDataFormatted();
       return true;
   }
   
   private void prepareMainMenuRequest(Request request){
       request.setData(Menus.MAIN_MENU);
       request.setResource(Resources.RESOURCE_MAIN_MENU);
       request.setType(Request.REQUEST_TYPE_SEND);
   }
}
