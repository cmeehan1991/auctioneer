/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bid;

import com.cbmwebdevelopment.alerts.Alerts;
import com.cbmwebdevelopment.items.Item;
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
import static com.cbmwebdevelopment.main.MainApp.CURRENCY_FORMAT;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import jfxtras.labs.scene.control.BigDecimalField;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class BidFXMLController implements Initializable {

    @FXML
    public TextField itemNumberTextField, itemNameTextField, itemDescriptionTextField, bidderNumberTextField;
    
    @FXML
    public BigDecimalField bidAmountTextField;

    @FXML
    Label itemNumberLabel, itemNameLabel, itemDescriptionLabel, bidderNumberLabel, bidAmountLabel;

    @FXML
    public Button submitWinnerButton;

    @FXML
    public ProgressIndicator progressIndicator;

    protected String itemNumber, itemName, itemDescription, bidderNumber, bidAmount;
    private ArrayList<String> requiredFields;

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

        if (requiredFields == null || requiredFields.isEmpty()) {
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
        System.out.println("Submit Bid");
        if (validateFields()) {
            bidAmountTextField.setText(CURRENCY_FORMAT.format(Double.parseDouble(CURRENCY_FORMAT.parse(bidAmountTextField.getText()).toString())));
            Bid bid = new Bid();
            bid.submitBid(this);
            System.out.println("Valid");
        } else {
            System.out.println("Not valid");
            Alert alert = new Alerts().errorAlert("Missing Items", "Missing required items.", "Please correct the following items", requiredFields);
            alert.showAndWait();
        }
    }

    private void getItemInformation(String id, BidFXMLController controller) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                progressIndicator.setVisible(true);
                Item item = new Item();
                item.getItem(id, controller);
                executor.awaitTermination(2, TimeUnit.SECONDS);
                executor.shutdown();
            } catch (InterruptedException ex) {
                System.err.println("Process Interrupted: " + ex.getMessage());
            } finally {
                if (!executor.isShutdown()) {
                    executor.shutdownNow();
                    System.err.println("Forced shutdown");
                }
                progressIndicator.setVisible(false);
                bidderNumberTextField.setDisable(false);
                bidAmountTextField.setDisable(false);
                submitWinnerButton.setDisable(false);
                bidderNumberTextField.requestFocus();
            }
        });
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Hide the progress indicator
        progressIndicator.setVisible(false);
        progressIndicator.toFront();

        // Disable everything except the item number textfield 
        itemNameTextField.setDisable(true);
        itemDescriptionTextField.setDisable(true);
        bidderNumberTextField.setDisable(true);
        bidAmountTextField.setDisable(true);
        submitWinnerButton.setDisable(true);

        itemNumberTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                itemNameTextField.setDisable(true);
                itemDescriptionTextField.setDisable(true);
                bidderNumberTextField.setDisable(true);
                bidAmountTextField.setDisable(true);
                submitWinnerButton.setDisable(true);
            }
        });

        itemNumberTextField.setOnKeyReleased((event) -> {
            if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) && itemNumberTextField.getText() != null && !itemNumberTextField.getText().trim().isEmpty()) {
                getItemInformation(itemNumberTextField.getText(), this);
            }
        });

        // TODO
    }

}
