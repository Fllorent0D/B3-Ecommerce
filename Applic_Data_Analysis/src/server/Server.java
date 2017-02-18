/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import server_dismap.ServerDISMAP;
import server_indep.ServerINDEP;

/**
 *
 * @author bastin
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ServerDISMAP(6000, 10, null).start();
        new ServerINDEP(6001, 10, null).start();
    }
    
}
