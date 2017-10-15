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
public class Request {
    private String m_type; /* GET | SEND*/
    private String m_resource;
    private String m_data = null;
    
    public static final String REQUEST_TYPE_GET = "GET";
    public static final String REQUEST_TYPE_SEND = "SEND";
    
    
    public Request(String type, String resource, String data){
        setType(type);
        setResource(resource);
        setData(data);
    }
    
    public Request(String request){
        parseToRequest(request);
        
    }
    
    public void setType(String type){
        m_type = type;
    }
    
    public String getType(){ return m_type; }
    
    public void setResource(String resource){ m_resource = resource;}
    
    public String getResource(){ return m_resource; }
    
    public void setData(String data){
        m_data = data;
    }
    
    public String getRawData(){ return m_data; }
    
    public String getDataFormatted(){
        String data = null;
        
        if (m_data != null){
          String[] s = m_data.split(";");
          data = "";
          for(int i=0; i < s.length; i++){
              data+= s[i] + "\n";
          }
        }
        
        return data;
    }
    public void parseToRequest(String request){
        String[] data = request.split("@");
        //System.out.println("Request::parseToRequest data size: " + data.length);
        setType(data[0]);
        setResource(data[1]);
        
        if (data.length == 3)
            setData(data[2]);
    }
    
    @Override
    public String toString(){
        return m_type + "@" + m_resource + "@" + m_data;
    }
    
}
