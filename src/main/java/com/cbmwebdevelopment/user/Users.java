/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.user;

import com.cbmwebdevelopment.connections.DBConnection;
import com.cbmwebdevelopment.main.MainApp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.stage.Stage;

/**
 *
 * @author cmeehan
 */
public class Users {
    protected void userSignIn(UserSignInFXMLController controller, String username, String password){
        Connection conn = new DBConnection().connect();
        
        String sql = "SELECT ID FROM USERS WHERE USERNAME = ? AND PASSWORD = MD5(?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("Has results");
                MainApp main = new MainApp();
                MainApp.IS_SIGNED_IN = true;
                MainApp.USER_ID = rs.getString("ID");
                main.start(new Stage());
                
                Stage currentStage = (Stage) controller.usernameLabel.getScene().getWindow();
                currentStage.close();
            }else{
                System.out.println("No Results");
                controller.passwordLabel.setStyle("-fx-text-color:#ff0000;");
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }finally{
            try{
                conn.close();
            }catch(SQLException ex){
                System.err.println("Error closing connection: " + ex.getMessage());
            }
        }
    }
}
