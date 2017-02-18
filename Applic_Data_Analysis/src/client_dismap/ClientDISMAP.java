/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_dismap;

import client.Client;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;
import server_dismap.RunnableDISMAP;
import server_dismap.protocoleDISMAP;

/**
 *
 * @author bastin
 */
public class ClientDISMAP extends Client implements protocoleDISMAP {
    
    
    public ClientDISMAP(String ip, int p) throws IOException {
        super(ip, p);
    }
    
    public void interrupt()
    {
        Message request = new Message();
        
        request.setType(REQUEST_INTERRUPT);
        
        sendMessage(request);
    }
    
    public Message login(String login, String password)
    {
        Message request = new Message();
        Message response = new Message();
        
        request.setType(REQUEST_LOGIN);
        request.addParam("login", login);
        request.addParam("password", password);
        
        sendMessage(request);
        response = receiveMessage();
        
        return response;
    }
    
    public Message logout(String login, String password)
    {
        Message request = new Message();
        Message response = new Message();
        
        request.setType(REQUEST_LOGOUT);
        request.addParam("login", login);
        request.addParam("password", password);
        
        sendMessage(request);
        response = receiveMessage();
        
        return response;
    }
    
    public Message searchGoods(String id)
    {
        Message request = new Message();
        Message response = new Message();
        
        request.setType(REQUEST_SEARCH_GOODS);
        request.addParam("id", id);
        
        sendMessage(request);
        response = receiveMessage();
        
        return response;
    }
    
    public Message receiveMessage()
    {
        Message msg = null;
        
        try {
            msg = (Message) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public void sendMessage(Message msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
