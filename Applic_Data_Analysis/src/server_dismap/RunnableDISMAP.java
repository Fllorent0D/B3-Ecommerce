package server_dismap;

import com.floca.BeanDBAccess.BeanDBAccessOracle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bastin
 */
public class RunnableDISMAP implements Runnable, protocoleDISMAP {
    
    private Socket CSocket = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private BeanDBAccessOracle beanOracle = null;
    private String login, password;
    
    public RunnableDISMAP(Socket s) {
        CSocket = s;
        
        try {
            System.out.println("Création du flux sortant du client...");
            oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
            oos.flush(); // Nécessaire pour que le programme ne se bloque pas sur "new ObjectInputStream
            System.out.println("Création du flux rentrant du client...");
            ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
            System.out.println("Les flux du client établis");
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        System.out.println("Connexion du client à la BD...");
        beanOracle = new BeanDBAccessOracle("127.0.0.1", "1522", "SHOP", "oracle");

        try {
            beanOracle.connect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        } catch (SQLException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        System.out.println("Client opérationnel");
    }
    
    boolean terminated = false;
    boolean logged = false;
    
    @Override
    public void run() {

        Message request = null;
        
        System.out.println("Lancement d'un thread client");

        do
        {
            request = receiveMessage();
            
            if(request.getType() != REQUEST_LOGIN)
            {
                if(request.getType() == REQUEST_INTERRUPT)
                    return;
                else
                    continue;
            }
            
            logged = executeLogin(request);
            
        }while(!logged);
        
        System.out.println("Client connectée");
        
        do
        {
            System.out.println("En attente d'une requête");
            
            request = receiveMessage();
            
            System.out.println("Type de la requete: " + request.getType());
            
            switch(request.getType())
            {
                case REQUEST_SEARCH_GOODS:
                    executeSearchGoods(request);
                    break;
                    
                case REQUEST_TAKE_GOODS:
                    
                    break;
                    
                case REQUEST_BUY_GOODS:
                    
                    break;
                    
                case REQUEST_DELIVERY_GOODS:
                    
                    break;
                    
                case REQUEST_LOGOUT:
                    terminated = executeLogout(request);
                    break;
                case REQUEST_INTERRUPT:
                default:
                    terminated = true;
                    break;
            }
            System.out.println("Action executée");
        } while(!terminated);
        
        System.out.println("Fermeture du thread client...");
        
        try {
            CSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Thread client fermé");
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
    
    private boolean executeLogin(Message request)
    {    
        Message response = new Message();
        response.setType(REQUEST_LOGIN);
        String query;
        int count = 0;
        
        login = (String)request.getParam("login");
        password = (String)request.getParam("password");
        
        query = "SELECT count(*) AS count FROM personnel WHERE login = '" + login + "' AND password = '" + password + "'";
       
        try {
            ResultSet result = beanOracle.executeQuery(query);
            result.next();
            count = result.getInt("count");
        } catch (SQLException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("status", "0");
            response.addParam("error", "Un problème interne est survenu. Veuillez contacter l'administrateur");
        }
        
        if(count == 1)
        {
            response.addParam("status", "1");
            response.addParam("error", "Authentification valide");
        }
        else
        {
            response.addParam("status", "0");
            
            if(count == 0)
                response.addParam("error", "Login ou password invalide");
            else
                response.addParam("error", "Un problème interne est survenu. Veuillez contacter l'administrateur");
        }
        
        sendMessage(response);
        
        if(count == 1)
            return true;
        else
            return false;
    }
    
    private boolean executeLogout(Message request)
    {
        Message response = new Message();
        response.setType(request.getType());
        boolean status;
        
        String login2 = (String)request.getParam("login");
        String password2 = (String)request.getParam("password");
        
        if(login.equals(login2) && password.equals(password2))
        {
            response.addParam("status", "1");
            status =  true;
        }
        else
        {
            response.addParam("status", "0");
            response.addParam("error", "Login ou password invalide");
            status = false;
        }
        
        sendMessage(response);
        
        return status;
    }
    
    private void executeSearchGoods(Message request)
    {
        Message response = new Message();
        response.setType(request.getType());
        ResultSet result = null;
        
        String id = (String)request.getParam("id");
        
        try {
            System.out.println("ID: " + id);
            result = beanOracle.executeQuery("SELECT * FROM type_appareils WHERE type_precis = " + id);
            
            ArrayList<String> data = new ArrayList();
            
            while(result.next())
            {
                String row = result.getString("ID_TYPE_APPAREIL") + Message.SEP + result.getString("LIBELLE") + Message.SEP + result.getString("PRIX_VENTE_BASE") + Message.SEP + result.getString("MARQUE");  
                data.add(row);
            }
            
            response.setData(data);
            
        } catch (SQLException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sendMessage(response);
    }
    
    private void executeTakeGoods(Message msg)
    {
        
    }
    
    private void executeBuyGoods(Message msg)
    {
        
    }
    
    private void executeDeliveryGoods(Message msg)
    {
        
    }
    
    private void executeListSales(Message msg)
    {
        
    }
}
