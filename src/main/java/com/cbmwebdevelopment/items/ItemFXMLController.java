/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.items;

import static com.cbmwebdevelopment.main.MainApp.CURRENCY_FORMAT;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;
import jfxtras.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class ItemFXMLController implements Initializable {

    @FXML
    TextField itemNumberTextField, itemNameTextField, itemDescriptionTextField, minimumBidTextField;

    @FXML
    ToggleGroup itemAuctionTypeGroup;

    @FXML
    ImageView itemImageImageView;

    @FXML
    RadioButton silentAuctionRadioButton, liveAuctionRadioButton;

    @FXML
    Label itemClosedLabel;

    protected String itemNumber, itemName, itemDescription, minimumBid;

    protected boolean silentAuction, liveAuction;

    protected File itemImage;

    private List<String> missingItems;

    @FXML
    protected void addItemImage(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        itemImage = fileChooser.showOpenDialog(new Stage());

        Image image = new Image(itemImage.toURI().toURL().toString());
        System.out.println(itemImage.toURI().toURL().toString());
        itemImageImageView.setImage(image);
        itemImageImageView.setFitWidth(100);
        itemImageImageView.setFitHeight(100);
        itemImageImageView.setPreserveRatio(true);

        System.out.println(itemImageImageView.getImage().toString());

    }

    @FXML
    protected void saveItem(ActionEvent event) throws IOException {
        if (validateFields()) {
            Item item = new Item();
            item.saveItem(this);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Items");
            alert.setHeaderText("Several required items are missing");
            alert.setContentText("Please correct the following missing items");

            // Expandable Exception
            TextArea textArea = new TextArea();
            missingItems.forEach((item) -> {
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

    private boolean validateFields() {
        boolean isValid = false;
        missingItems = new ArrayList<>();
        assignValues();

        if (itemName.trim().isEmpty() || itemName == null) {
            missingItems.add("Item Name");
        }

        if (liveAuction == false && silentAuction == false) {
            missingItems.add("Auction Type");
        }

        if (missingItems.isEmpty() || missingItems == null) {
            isValid = true;
        }

        return isValid;
    }

    private void assignValues() {
        itemNumber = itemNumberTextField.getText();
        itemName = itemNameTextField.getText();
        itemDescription = itemDescriptionTextField.getText();
        silentAuction = silentAuctionRadioButton.isSelected();
        liveAuction = liveAuctionRadioButton.isSelected();
        minimumBid = minimumBidTextField.getText();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        itemClosedLabel.setVisible(false); // Setting this label to invisible by default.
        // TODO
        
    }

}
