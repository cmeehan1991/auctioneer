package com.cbmwebdevelopment.user;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class UserSignInFXMLController implements Initializable {

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField userPasswordPasswordField, newPasswordField, confirmNewPasswordField;

    @FXML
    Label usernameLabel, passwordLabel;

    @FXML
    StackPane stackPane;

    @FXML
    Button setNewPasswordButton;

    @FXML
    GridPane signInPane, passwordPane;

    public String username, password;
    private ArrayList<String> missingValues;

    private void assignValues() {
        username = usernameTextField.getText();
        password = newPasswordField.getText().trim().isEmpty() || newPasswordField.getText() == null ? userPasswordPasswordField.getText() : newPasswordField.getText();
    }

    private boolean validateFields() {
        missingValues = new ArrayList<>();
        assignValues();

        if (username == null || username.trim().isEmpty()) {
            missingValues.add("Username");
        }

        if (password == null || password.trim().isEmpty()) {
            missingValues.add("Password");
        }

        return missingValues.isEmpty();
    }

    @FXML
    protected void signIn(ActionEvent event) {
        System.out.println("Signing In");
        Users users = new Users();
        users.userSignIn(this, usernameTextField.getText(), userPasswordPasswordField.getText());
    }

    @FXML
    protected void setNewPassword(ActionEvent event) {
        Users users = new Users();
        users.userSignIn(this, usernameTextField.getText(), confirmNewPasswordField.getText());
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        passwordPane.setVisible(false);
        signInPane.setVisible(true);
        setNewPasswordButton.setDisable(true);
        setNewPasswordButton.setVisible(false);
        confirmNewPasswordField.setDisable(true);

        newPasswordField.textProperty().addListener((obs, ov, nv) -> {
            Pattern letter = Pattern.compile("[A-Za-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%^&*_+=]");

            Matcher hasLetter = letter.matcher(nv);
            Matcher hasDigit = digit.matcher(nv);
            Matcher hasSpecial = digit.matcher(nv);
            if (nv.length() > 8 && hasLetter.find() && hasDigit.find() && hasSpecial.find()) {
                confirmNewPasswordField.setDisable(false);
                newPasswordField.setStyle("-fx-border-color:#008000;");
            }else{
                confirmNewPasswordField.setDisable(true);
                newPasswordField.setStyle("-fx-border-color:#ff0000;");
            }
        });

        confirmNewPasswordField.textProperty().addListener((obs, ov, nv) -> {
            if (nv.equals(newPasswordField.getText())) {
                setNewPasswordButton.setVisible(true);
                setNewPasswordButton.setDisable(false);
                confirmNewPasswordField.setStyle("-fx-border-color:#008000;");
            } else {
                setNewPasswordButton.setDisable(true);
                setNewPasswordButton.setVisible(false);
                confirmNewPasswordField.setStyle("-fx-border-color:#ff0000;");
            }
        });
    }

}
