/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bid;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.Window;

/**
 *
 * @author cmeehan
 */
public class ViewBidsMain extends Application {
    public Group group;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ViewBidsFXML.fxml"));
        Parent root = (Parent) loader.load();
        ViewBidsFXMLController controller = (ViewBidsFXMLController) loader.getController();
        controller.group = group;
        
        Window window = new Window("View Bids");
        window.setBoundsListenerEnabled(true);
        window.setLayoutX(10);
        window.setLayoutY(100);
        window.setPrefHeight(250);
        window.setPrefWidth(600);
        window.getRightIcons().add(new CloseIcon(window));

        window.getContentPane().getChildren().add(root);

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
