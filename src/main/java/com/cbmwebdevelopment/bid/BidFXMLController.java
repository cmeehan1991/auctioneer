/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bid;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import jfxtras.scene.layout.GridPane;
import com.cbmwebdevelopment.main.MainApp;
import static com.cbmwebdevelopment.main.MainApp.CURRENCY_FORMAT;
import java.text.ParseException;
import javafx.scene.input.KeyCode;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class BidFXMLController implements Initializable {

    @FXML
    TextField itemNumberTextField, itemNameTextField, itemDescriptionTextField, bidderNumberTextField, bidAmountTextField;
    
    @FXML
    Label itemNumberLabel, itemNameLabel, itemDescriptionLabel, bidderNumberLabel, bidAmountLabel;

    @FXML
    Button submitWinnerButton;

    protected String itemNumber, itemName, itemDescription, bidderNumber, bidAmount;
    private List<String> requiredFields;
    

    /**
     * Validate that the required fields have been filled in.
     *
     * @return
     */
    private boolean validateFields() {
        boolean isValid = false;
        
        // Assign the values
        assignValues();
        
        // Initialize the required fields array list
        // This needs to be done every time so that the list is empty upon submit.
        requiredFields = new ArrayList<>();
        
        if (itemNumber == null || itemNumber.isEmpty()) {
            requiredFields.add("Item Number");
            itemNumberTextField.setStyle("-fx-border-color: #ff0000");
            itemNumberLabel.setStyle("-fx-text-fill: #ff0000");
        }
      
        if (bidderNumber == null || bidderNumber.isEmpty()) {
            requiredFields.add("Bidder Number");
            bidderNumberTextField.setStyle("-fx-border-color: #ff0000");
            bidderNumberLabel.setStyle("-fx-text-fill: #ff0000");
        }
        
        if (bidAmount == null || bidAmount.isEmpty()) {
            requiredFields.add("Bid Amount");
            bidAmountTextField.setStyle("-fx-border-color: #ff0000");
            bidAmountLabel.setStyle("-fx-text-fill: #ff0000");
        }
        
        if(requiredFields == null || requiredFields.isEmpty()){
            isValid = true;
        }
        
        return isValid;
    }

    /**
     * Assign the input values to their respective objects.
     */
    private void assignValues() {
        itemNumber = itemNumberTextField.getText();
        itemName = itemNameTextField.getText();
        itemDescription = itemDescriptionTextField.getText();
        bidderNumber = bidderNumberTextField.getText();
        bidAmount = bidAmountTextField.getText();
    }

    @FXML
    protected void submitBid(ActionEvent event) throws ParseException {
        if (validateFields()) {
            bidAmountTextField.setText(CURRENCY_FORMAT.format(Double.parseDouble(CURRENCY_FORMAT.parse(bidAmountTextField.getText()).toString())));
            Bid bid = new Bid();
            bid.submitBid(this);
            System.out.println("Valid");
        }else{
            System.out.println("Not valid");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Missing Items");
            alert.setHeaderText("Several required items are missing");
            alert.setContentText("Please correct the following missing items");
            
            // Expandable Exception
            TextArea textArea = new TextArea();
            requiredFields.forEach((item)->{
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
            
            alert.showAndWait();
        }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bidAmountTextField.setOnKeyReleased((event)->{
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                bidAmountTextField.setText(CURRENCY_FORMAT.format(Double.parseDouble(bidAmountTextField.getText())));
            }
        });
        
        // TODO
    }

}
