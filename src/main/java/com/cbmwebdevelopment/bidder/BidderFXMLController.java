/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bidder;

import com.cbmwebdevelopment.alerts.Alerts;
import com.cbmwebdevelopment.main.MainApp;
import static com.cbmwebdevelopment.main.MainApp.ERROR_LABEL;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.PrefixSelectionComboBox;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class BidderFXMLController implements Initializable {

    @FXML
    TextField bidderIdTextField, firstNameTextField, lastNameTextField, streetAddressTextField, suiteTextField, cityTextField, postalCodeTextField, phoneNumberTextField, emailAddressTextField;

    @FXML
    PrefixSelectionComboBox bidderPrefixComboBox, suffixComboBox, stateComboBox;
    
    @FXML
    Label firstNameLabel, lastNameLabel, streetAddressLabel, cityLabel, postalCodeLabel, phoneNumberLabel, emailAddressLabel;

    private ArrayList<String> missingItems;
    protected String id, prefix, firstName, lastName, suffix, streetAddress, suite, city, state, postalCode, phone, email;

    @FXML
    protected void saveBidder(ActionEvent event) {
        if (validateFields()) {
            Bidder bidder = new Bidder();
            HashMap<String, String> args = new HashMap<>();
            args.put("id", id);
            args.put("prefix", prefix);
            args.put("suffix", suffix);
            args.put("firstName", firstName);
            args.put("lastName", lastName);
            args.put("primaryAddress", streetAddress);
            args.put("secondaryAddress", suite);
            args.put("city", city);
            args.put("state", state);
            args.put("postalCode", postalCode);
            args.put("telephone", phone);
            args.put("email", email);
            if(id == null || id.trim().isEmpty()){
                bidder.saveBidder(args, this);
            }else{
                bidder.updateBidder(args, this);
            }
            
        }else{
            new Alerts().errorAlert("Missing Items", "Several required items are missing", "Please correct the following missing items:", missingItems).showAndWait();
        }
    }

    /**
     * Validates the required fields.
     *
     * @return boolean
     */
    private boolean validateFields() {        
        // Assign all of the input values to their corresponding string.
        assignValues();
        
        missingItems = new ArrayList<>();
        
        if (firstName == null || firstName.isEmpty()) {
            missingItems.add("First Name");
            firstNameLabel.setStyle(ERROR_LABEL);           
        }
        
        if (lastName == null || lastName.isEmpty()){
            missingItems.add("Last Name");
            lastNameLabel.setStyle(ERROR_LABEL);
        }
        
        if(streetAddress == null || streetAddress.isEmpty()){
            missingItems.add("Street Address");
            streetAddressLabel.setStyle(ERROR_LABEL);
        }
        
        if(city == null || city.isEmpty()){
            missingItems.add("City");
            cityLabel.setStyle(ERROR_LABEL);
        }
        
        if(postalCode == null || postalCode.isEmpty()){
            missingItems.add("Postal Code");
            postalCodeLabel.setStyle(ERROR_LABEL);
        }


        return missingItems.isEmpty();
    }

    /**
     * Assigns values to each variable from the input fields
     */
    private void assignValues() {
        id = bidderIdTextField.getText();
        prefix = bidderPrefixComboBox.getSelectionModel().getSelectedItem().toString();
        firstName = firstNameTextField.getText();
        lastName = lastNameTextField.getText();
        suffix = suffixComboBox.getSelectionModel().getSelectedItem().toString();
        streetAddress = streetAddressTextField.getText();
        suite = suiteTextField.getText();
        city = cityTextField.getText();
        state = stateComboBox.getSelectionModel().getSelectedItem().toString();
        postalCode = postalCodeTextField.getText();
        phone = phoneNumberTextField.getText();
        email = emailAddressTextField.getText();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set the combobox values
        bidderPrefixComboBox.getItems().setAll(MainApp.PREFIXES);
        bidderPrefixComboBox.getSelectionModel().select(0);
        
        suffixComboBox.getItems().setAll(MainApp.SUFFIXES);
        suffixComboBox.getSelectionModel().select(0);
        
        stateComboBox.getItems().setAll(MainApp.STATES);
        stateComboBox.getSelectionModel().select(0);
        
        
    }

}
