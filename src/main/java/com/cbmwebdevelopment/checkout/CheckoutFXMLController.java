/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.checkout;

import com.cbmwebdevelopment.tablecontrollers.CheckoutItemTableViewController;
import com.cbmwebdevelopment.bidder.Bidder;
import com.cbmwebdevelopment.output.ItemReceipt;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class CheckoutFXMLController implements Initializable {

    @FXML
    public TextField bidderIdTextField, bidderNameTextField, totalItemsTextField, totalAmountTextField;

    @FXML
    public TextArea billingAddressTextArea;

    @FXML
    public TableView bidderItemsTableView;

    @FXML
    public ProgressIndicator progressIndicator;

    @FXML
    public NotificationPane notificationPane;

    public CheckoutItemTableViewController tableController;
    private String bidderId;

    @FXML
    protected void printReceipt(ActionEvent event) throws IOException{
        ItemReceipt itemReceipt = new ItemReceipt(bidderIdTextField.getText(), bidderNameTextField.getText(), totalItemsTextField.getText(), totalAmountTextField.getText(), billingAddressTextArea.getText(), bidderItemsTableView);
        itemReceipt.printReceipt(bidderIdTextField.getText());
    }
    
    @FXML
    protected void emailReceipt(ActionEvent event) throws IOException, FileNotFoundException, ParseException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set recipient");
        dialog.setHeaderText("Add a single recipient or a semi-colon (;) separated list of recipients.");
        dialog.setContentText("Recipient(s):");
        dialog.getEditor().setText(new Bidder().getEmail(bidderIdTextField.getText()));
        Optional<String> result = dialog.showAndWait();
        
        if(result.get() != null && !result.get().trim().isEmpty()){
           ItemReceipt itemReceipt = new ItemReceipt(bidderIdTextField.getText(), bidderNameTextField.getText(), totalItemsTextField.getText(), totalAmountTextField.getText(), billingAddressTextArea.getText(), bidderItemsTableView);
           itemReceipt.emailPDF(bidderIdTextField.getText(), result.get());
        }
    }

    @FXML
    protected void saveReceipt(ActionEvent event) throws FileNotFoundException, IOException, ParseException {
        ItemReceipt itemReceipt = new ItemReceipt(bidderIdTextField.getText(), bidderNameTextField.getText(), totalItemsTextField.getText(), totalAmountTextField.getText(), billingAddressTextArea.getText(), bidderItemsTableView);
        itemReceipt.saveAsPDF();
    }

    protected void getCheckoutInformation(String bidderId, CheckoutFXMLController controller) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            Platform.runLater(() -> {
                Checkout checkout = new Checkout();
                checkout.getUserInformation(bidderId, controller);
                executor.shutdown();
                
            });
        });
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableController = new CheckoutItemTableViewController();
        tableController.tableView(bidderItemsTableView);

        bidderIdTextField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == false && newValue.booleanValue() != oldValue.booleanValue()) {
                if (bidderIdTextField != null && !bidderIdTextField.getText().trim().isEmpty()) {
                    progressIndicator.setVisible(true);
                    getCheckoutInformation(bidderIdTextField.getText(), this);
                }
            }
        });
    }

}
