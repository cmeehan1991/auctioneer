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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author cmeehan
 */
public class Users {

    protected void userSignIn(UserSignInFXMLController controller, String username, String password) {
        Connection conn = new DBConnection().connect();

        String sql = "SELECT ID, RESET_PASSWORD FROM USERS WHERE USERNAME = ? AND PASSWORD = MD5(?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean resetPassword = rs.getBoolean("RESET_PASSWORD");
                if (resetPassword) {
                    controller.signInPane.setVisible(false);
                    controller.passwordPane.setPrefWidth(0);
                    controller.passwordPane.setVisible(true);
                    Timeline timeline = new Timeline();
                    timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(controller.passwordPane.prefWidthProperty(), 0)
                            ),
                            new KeyFrame(Duration.millis(1500),
                                    new KeyValue(controller.passwordPane.prefWidthProperty(), controller.signInPane.getWidth())
                            )
                    );
                } else {
                    MainApp main = new MainApp();
                    MainApp.IS_SIGNED_IN = true;
                    MainApp.USER_ID = rs.getString("ID");
                    main.start(new Stage());

                    Stage currentStage = (Stage) controller.usernameLabel.getScene().getWindow();
                    currentStage.close();
                }
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

    protected void sendEmailNotification(String password, String name, String username, String email, String key) {
        // Set the host and port information 
        String smtpHost = "mail.cbmwebdevelopment.com";
        int smtpPort = 25;

        // Set the sender, message content and subject
        String sender = "connor.meehan@cbmwebdevelopment.com";
        String messageContent = name + ",\n\n" + "\tYou have been added as a user to The Pregnancy Center of Hilton Head\'s Auctioneer auction management application. Please use the below information to sign in. You will be prompted to change your password once you have successfully signed in." + "\n\n" + "username: " + username + "\n" + "password: " + password + "\n\n" + "DO NOT respond to this message. This message was automatically generated. Please contact your systems administrator with any questions. If you feel you received this message by accident please simply ignore it.";
        String subject = "Auctioneer New User";

        // Mailer properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        Session session = Session.getDefaultInstance(properties, null);

        try {
            // Construct the text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(messageContent);

            // Construct the mime multi part
            MimeMultipart mimeMultiPart = new MimeMultipart();
            mimeMultiPart.addBodyPart(textBodyPart);

            // Create the sender/recipient addresses
            InternetAddress iaSender = new InternetAddress(sender);
            InternetAddress iaRecipient = new InternetAddress(email);

            // Construct the mime message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSender(iaSender);
            mimeMessage.setFrom(iaSender);
            mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(mimeMultiPart);

            Transport.send(mimeMessage);
            //Alert alert = new Alert()
        } catch (MessagingException ex) {
            System.err.println(ex.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Notification Failed");
            alert.setHeaderText("Email Notification Failed");
            alert.setContentText("The email notification failed to send to the user.");
            alert.showAndWait();
        }
    }

    /**
     * Add the new user to the database
     *
     * @param controller
     */
    public void addNewUser(UserInformationController controller) {

        // Random password
        String password = MainApp.randomPasswordGenerator(16);
        Connection conn = new DBConnection().connect();

        String sql = "INSERT INTO USERS (USERNAME, PASSWORD, PREFIX, FIRST_NAME, LAST_NAME, SUFFIX, TELEPHONE, EMAIL, PRIMARY_ADDRESS, SECONDARY_ADDRESS, CITY, STATE, POSTAL_CODE, RESET_PASSWORD) VALUES(?,MD5(?),?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, controller.username);
            ps.setString(2, password);
            ps.setString(3, controller.prefix);
            ps.setString(4, controller.firstName);
            ps.setString(5, controller.lastName);
            ps.setString(6, controller.suffix);
            ps.setString(7, controller.telephone);
            ps.setString(8, controller.email);
            ps.setString(9, controller.streetAddress);
            ps.setString(10, controller.secondaryAddress);
            ps.setString(11, controller.city);
            ps.setString(12, controller.state);
            ps.setString(13, controller.postalCode);
            ps.setBoolean(14, true);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                String key = String.valueOf(rs.getInt(1));
                sendEmailNotification(password, controller.firstName + " " + controller.lastName, controller.username, controller.email, key);
                Alert alert = new Alerts().informationAlert("User Added", "The user was successfully added.", controller.firstName + " " + controller.lastName + " was successfully added.");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * Updates an existing user.
     *
     * @param controller
     */
    public void updateExistingUser(UserInformationController controller) {

    }

    /**
     * Check if the username has been taken
     *
     * @param username
     * @param userId
     * @return
     */
    public boolean userExists(String username, String userId) {
        boolean userExists = false;
        Connection conn = new DBConnection().connect();
        String sql = "SELECT COUNT(ID) as 'COUNT' FROM USERS WHERE USERNAME = ?";
        if (userId != null) {
            sql += " ID = ?";
        }
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            if (userId != null) {
                ps.setString(2, userId);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("COUNT") > 0) {
                    userExists = true;
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return userExists;
    }
}
