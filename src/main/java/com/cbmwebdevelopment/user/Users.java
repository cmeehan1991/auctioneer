/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.user;

import com.cbmwebdevelopment.alerts.Alerts;
import com.cbmwebdevelopment.connections.DBConnection;
import com.cbmwebdevelopment.main.MainApp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author cmeehan
 */
public class Users {

    protected void userSignIn(UserSignInFXMLController controller, String username, String password) {
        Connection conn = new DBConnection().connect();

        String sql = "SELECT ID FROM USERS WHERE USERNAME = ? AND PASSWORD = MD5(?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MainApp main = new MainApp();
                MainApp.IS_SIGNED_IN = true;
                MainApp.USER_ID = rs.getString("ID");
                main.start(new Stage());

                Stage currentStage = (Stage) controller.usernameLabel.getScene().getWindow();
                currentStage.close();
            } else {
                controller.passwordLabel.setStyle("-fx-text-color:#ff0000;");
                Alert alert = new Alerts().errorAlert("Invalid Credentials", "Invalid Log In Credentials", "The log in information that you entered is not valid. Please try again.", null);
                alert.showAndWait();
            }
        } catch (Exception ex) {
            ArrayList<String> error = new ArrayList<>();
            error.add(ex.getMessage());
            Alert alert = new Alerts().errorAlert("SQL Error", "Login Error", "Error Message:", error);
            alert.showAndWait();
            System.err.println(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                ArrayList<String> error = new ArrayList<>();
                error.add(ex.getMessage());
                Alert alert = new Alerts().errorAlert("Connection closed error", "Connetion close error", "Error Message:", error);
                alert.showAndWait();
                System.err.println("Error closing connection: " + ex.getMessage());
            }
        }
    }
}
