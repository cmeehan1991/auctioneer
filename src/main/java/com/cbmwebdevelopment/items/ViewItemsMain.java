/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.items;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.Window;

/**
 *
 * @author cmeehan
 */
public class ViewItemsMain extends Application {

    public Group group;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ViewItemsFXML.fxml"));
        Parent root = (Parent) loader.load();
        ViewItemsFXMLController controller = (ViewItemsFXMLController) loader.getController();
        controller.group = group;
        
        Scene scene = new Scene(root);
        
        Window window = new Window("Auction Items");
        window.setLayoutX(10);
        window.setLayoutY(100);
        window.setPrefHeight(400);
        window.setPrefWidth(800);
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
