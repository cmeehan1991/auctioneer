/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.connections;

import com.cbmwebdevelopment.alerts.Alerts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author cmeehan
 */
public class DBConnection {
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/auction?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; //"jdbc:mysql://ns8139.hostgator.com/cmeehan_auctioneer?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; 
    private final String dbUser ="root"; //"cmeehan_auction";
    private final String dbPass = "root"; //"Wadiver15!";
    
    /**
     * Connect to the database. 
     * @return Connection connection
     */
    public Connection connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException ex){ ArrayList<String> error = new ArrayList<>();
            error.add(ex.getMessage());
            Alert alert = new Alerts().errorAlert("Class Error", "Class Error", "Error Message:", error);
            alert.showAndWait();
            System.err.println("Class Error: " + ex.getMessage());
        }
        
        try{
            connection = DriverManager.getConnection(url, dbUser, dbPass);
        }catch(SQLException ex){ ArrayList<String> error = new ArrayList<>();
            error.add(ex.getMessage());
            Alert alert = new Alerts().errorAlert("Connection Error", "Connection Error", "Error Message:", error);
            alert.showAndWait();
            System.err.println("Connection Error: " + ex.getMessage());
        }
        return connection;
    }
}
