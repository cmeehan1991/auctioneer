/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.alerts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Priority;
import jfxtras.scene.layout.GridPane;
import org.controlsfx.dialog.CommandLinksDialog;
import org.controlsfx.dialog.CommandLinksDialog.CommandLinksButtonType;

/**
 *
 * @author cmeehan
 */
public class Alerts {

    /**
     * Returns an error alert with an optional error display list. 
     * @param title
     * @param headerText
     * @param content
     * @param errors
     * @return 
     */
    public Alert errorAlert(String title, String headerText, String content, ArrayList<String> errors) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);

        if (errors != null) {
            // Expandable Exception
            TextArea textArea = new TextArea();
            errors.forEach((item) -> {
                textArea.appendText(item + "\n");
            });
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(textArea, 0, 0);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.getDialogPane().setExpanded(true);

        }

        return alert;
    }
    
    /**
     * Returns an information alert. 
     * @param title
     * @param headerText
     * @param content
     * @return 
     */
    public Alert informationAlert(String title, String headerText, String content){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);
        
        return alert;
    }
    
    public TextInputDialog inputAlert(String title, String headerText, String content){
        TextInputDialog input = new TextInputDialog();
        input.setTitle(title);
        input.setHeaderText(headerText);
        input.setContentText(content);
        return input;
    }
    
    public Dialog commandLinkDialog(String title, String headerText, String content, HashMap<String, HashMap<String, Boolean>> options){
        
        List<CommandLinksButtonType> links = new ArrayList<>();
        options.forEach((buttonTitle, nestedMap)->{
           nestedMap.forEach((buttonDescription, isDefault)->{
               links.add(new CommandLinksButtonType(buttonTitle, buttonDescription, isDefault));
           });
        });
        
        CommandLinksDialog dialog = new CommandLinksDialog(links);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        
        return dialog;
    }
}
