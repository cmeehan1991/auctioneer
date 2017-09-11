/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.items;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.Window;

/**
 *
 * @author cmeehan
 */
public class ItemMain extends Application {
    public Group group;
    public boolean isNew;
    public String itemNumber;
    @Override
    public void start(Stage primaryStage) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ItemFXML.fxml"));
       Parent root = (Parent) loader.load();
       ItemFXMLController controller = (ItemFXMLController) loader.getController();
       
       if(!isNew){
           HashMap<String,String> item = new Item().getValues(itemNumber);
           if(item == null || item.isEmpty()){
               Alert alert = new Alert(AlertType.ERROR);
               alert.setTitle("Item Missing");
               alert.setHeaderText("The item number you entered does not exist.");
               alert.setContentText("The item you are trying to retreive does not exist. Please check the item number and try again.");
               alert.showAndWait();
               return;
           }
                controller.itemNumberTextField.setText(itemNumber);
                controller.itemNameTextField.setText(item.get("name"));
                controller.itemDescriptionTextField.setText(item.get("description"));
                controller.minimumBidTextField.setText(item.get("reserve"));
                controller.silentAuctionRadioButton.setSelected(Boolean.parseBoolean(item.get("silentAuction")));
                controller.liveAuctionRadioButton.setSelected(Boolean.parseBoolean(item.get("liveAuction")));
                controller.itemClosedLabel.setVisible(Boolean.parseBoolean(item.get("closed")));
                controller.itemImage = item.get("imageUrl") == null ? new File(getClass().getResource("/images/auction.jpg").getFile()) : new File(item.get("imageUrl"));
                controller.itemImageImageView.setImage(new Image(controller.itemImage.toURI().toURL().toString()));
       }
       
       Window window = new Window("Item");
       window.setBoundsListenerEnabled(true);
       window.setLayoutX(10);
       window.setLayoutY(80);
       window.setPrefHeight(500);
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
