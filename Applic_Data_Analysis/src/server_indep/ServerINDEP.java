package server_indep;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ListTasks;
import server.SourceTasks;
import server.ThreadClient;

/**
 *
 * @author bastin
 */
public class ServerINDEP extends Thread {
    private int port;
    private int nbrThreads;
    private SourceTasks tasksToExecute;
    private ServerSocket SSocket;
    
    public ServerINDEP(int p, int nt, SourceTasks t)
    {
        port = p;
        nbrThreads = nt;
        
        if(t == null)
            tasksToExecute = new ListTasks();
        else
            tasksToExecute = t;
    }
    
    @Override
    public void run()
    {
        try {
            SSocket = new ServerSocket(port);
            System.out.println("D'un serveur INDEP");

        } catch (IOException ex) {
            Logger.getLogger(ServerINDEP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        for(int i = 0 ; i < nbrThreads ; i++)
        {
            System.out.println("Lancement ThreadClient n°" + (i+1));
            ThreadClient thr = new ThreadClient(tasksToExecute);
            thr.start();
        }
        
        Socket CSocket = null;
        
        while(!isInterrupted())
        {
            try {
                CSocket = SSocket.accept();
                System.out.println("Un client contact le serveur");
            } catch (IOException ex) {
                Logger.getLogger(ServerINDEP.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
            
            tasksToExecute.recordTask(new RunnableINDEP(CSocket));
        }
    }
    
    public static void main(String[] args) {
        new ServerINDEP(6001, 5, null).start();
    }
}
