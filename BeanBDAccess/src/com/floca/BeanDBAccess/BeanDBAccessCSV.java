/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.floca.BeanDBAccess;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author florentcardoen
 */
public class BeanDBAccessCSV extends BeanDBAccess {

    public BeanDBAccessCSV(Path fichier) {
        super(fichier.toString(), null, null, null, null);
        setDriver("org.relique.jdbc.csv.CsvDriver");
    }

    @Override
    public void connect() throws ClassNotFoundException, SQLException {
        String url = "jdbc:relique:csv:" + host;

        Class.forName(driver);
    
        connection = (Connection) DriverManager.getConnection(url);
    
        statement = (Statement) connection.createStatement();
        
    }
    
}
