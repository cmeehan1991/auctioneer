/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cmeehan
 */
public class DBConnection {
    private Connection connection;
    private final String url = "jdbc:mysql://ns8139.hostgator.com/cmeehan_auctioneer?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; /*"jdbc:mysql://localhost/auction?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";*/
    private final String dbUser = "cmeehan";
    private final String dbPass = "Wadiver15!";
    
    /**
     * Connect to the database. 
     * @return Connection connection
     */
    public Connection connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException ex){
            System.err.println("Class Error: " + ex.getMessage());
        }
        
        try{
            connection = DriverManager.getConnection(url, dbUser, dbPass);
        }catch(SQLException ex){
            System.err.println("Connection Error: " + ex.getMessage());
        }
        return connection;
    }
}
