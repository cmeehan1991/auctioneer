package com.cbmwebdevelopment.user;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class UserSignInFXMLController implements Initializable {

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField userPasswordPasswordField;

    @FXML
    Label usernameLabel, passwordLabel;
    
    @FXML
    protected void signIn(ActionEvent event){
        Users users = new Users();
        users.userSignIn(this, usernameTextField.getText(), userPasswordPasswordField.getText());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
