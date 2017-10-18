/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bidder;

import com.cbmwebdevelopment.alerts.Alerts;
import java.io.IOException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.Window;

/**
 *
 * @author cmeehan
 */
public class BidderMain extends Application {

    public String bidderId;
    public boolean isNew;
    public Group group;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BidderFXML.fxml"));
        Parent root = (Parent) loader.load();
        BidderFXMLController controller = (BidderFXMLController) loader.getController();

        if(!isNew){
            if (bidderId != null && !bidderId.trim().isEmpty()) {
                Bidder bidder = new Bidder();
                HashMap<String, String> bidderInfo = bidder.getUser(bidderId);
                System.out.println(bidderInfo);
                System.out.println(bidderInfo.isEmpty());
                if(!bidderInfo.isEmpty()){
                    controller.bidderIdTextField.setText(bidderId);
                    controller.bidderPrefixComboBox.getSelectionModel().select(bidderInfo.get("prefix"));
                    controller.firstNameTextField.setText(bidderInfo.get("firstName"));
                    controller.lastNameTextField.setText(bidderInfo.get("lastName"));
                    controller.suffixComboBox.getSelectionModel().select(bidderInfo.get("suffix"));
                    controller.phoneNumberTextField.setText(bidderInfo.get("telephone"));
                    controller.emailAddressTextField.setText(bidderInfo.get("email"));
                    controller.streetAddressTextField.setText(bidderInfo.get("primaryAddress"));
                    controller.suiteTextField.setText(bidderInfo.get("secondaryAddress"));
                    controller.cityTextField.setText(bidderInfo.get("city"));
                    controller.stateComboBox.getSelectionModel().select(bidderInfo.get("state"));
                    controller.postalCodeTextField.setText(bidderInfo.get("postalCode"));
                    controller.group = group;
                }else{
                    Alert alert = new Alerts().errorAlert("Invalid Bidder Id", "Invalid Bidder Id", "The bidder ID you entered does not match anything on record. Please try again.", null);
                    alert.showAndWait();
                    return;
                }
            }
        }
        

        Window window = new Window("Bidder");
        window.setBoundsListenerEnabled(true);
        window.setLayoutX(10);
        window.setLayoutY(100);
        window.setPrefHeight(400);
        window.setPrefWidth(700);

        window.getRightIcons().add(new CloseIcon(window));

        window.getContentPane().getChildren().add(root);

        group.getChildren().add(window);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
