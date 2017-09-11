/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.checkout;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.Window;

/**
 *
 * @author cmeehan
 */
public class CheckoutMain extends Application {

    public String bidderId;
    public Group group;

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("checkout");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CheckoutFXML.fxml"));
        Parent root = (Parent) loader.load();
        CheckoutFXMLController controller = (CheckoutFXMLController) loader.getController();

        AnchorPane anchorPane = loader.getRoot();
        
        anchorPane.setPrefWidth(775);
        
        Window window = new Window("Check Out");
        window.setBoundsListenerEnabled(true);
        window.setLayoutX(10);
        window.setLayoutY(100);
        window.setPrefHeight(250);
        window.setPrefWidth(775);
        window.getRightIcons().add(new CloseIcon(window));
        window.getContentPane().getChildren().add(root);
        
        controller.getCheckoutInformation(bidderId, controller);
        
        
        
        group.getBoundsInParent();
        group.getChildren().add(window);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
