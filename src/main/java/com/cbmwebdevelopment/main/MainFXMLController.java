package com.cbmwebdevelopment.main;

import com.cbmwebdevelopment.alerts.Alerts;
import com.cbmwebdevelopment.bid.BidMain;
import com.cbmwebdevelopment.bid.ViewBidsMain;
import com.cbmwebdevelopment.bidder.BidderMain;
import com.cbmwebdevelopment.checkout.CheckoutMain;
import com.cbmwebdevelopment.items.ItemMain;
import com.cbmwebdevelopment.items.ViewItemsMain;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainFXMLController implements Initializable {

    @FXML 
    public AnchorPane mainAnchorPane, navigationAnchorPane;
    
    public Group group;
    private BidMain bidMain;
    private ItemMain itemMain;
    private ViewBidsMain viewBidsMain;
    private  ViewItemsMain viewItems;
    

    @FXML
    protected void addNewBidder(ActionEvent event) throws IOException {
        BidderMain bidderMain = new BidderMain();
        bidderMain.isNew = false;
        bidderMain.group = group;
        bidderMain.start(new Stage());
    }

    @FXML
    protected void viewEditBidder(ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Item");
        dialog.setHeaderText("View/Edit an existing item");
        dialog.setContentText("Enter a valid item number");
        dialog.showAndWait();

        String bidderId = dialog.getEditor().getText();
        if (!bidderId.trim().isEmpty()) {
            BidderMain bidderMain = new BidderMain();
            bidderMain.bidderId = bidderId;
            bidderMain.isNew = false;
            bidderMain.group = group;
            bidderMain.start(new Stage());
        }
    }

    @FXML
    protected void bidderCheckout(ActionEvent event) throws IOException {

        CheckoutMain checkoutMain = new CheckoutMain();

       Optional<String> result = new Alerts().inputAlert("Bidder Id", "Enter the Bidder Id", "Bidder Id").showAndWait();
       
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            checkoutMain.bidderId = result.get();
            checkoutMain.group = group;
            checkoutMain.start(new Stage());
        }
    }

    @FXML
    protected void addBid(ActionEvent event) throws IOException {
        bidMain = new BidMain();
        bidMain.isNew = true;
        bidMain.group = group;
        bidMain.start(new Stage());
    }

    @FXML
    protected void viewBids(ActionEvent event) throws IOException {
        viewBidsMain = new ViewBidsMain();
        viewBidsMain.group = group;
        viewBidsMain.start(new Stage());
    }

    @FXML
    protected void addNewItem(ActionEvent event) throws IOException {
        itemMain = new ItemMain();
        itemMain.isNew = true;
        itemMain.group = group;
        itemMain.start(new Stage());
    }
    
    @FXML
    protected void viewEditItems(ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Item");
        dialog.setHeaderText("View/Edit an existing item");
        dialog.setContentText("Enter a valid item number");
        dialog.showAndWait();

        String itemId = dialog.getEditor().getText();

        itemMain = new ItemMain();
        itemMain.itemNumber = itemId;
        itemMain.isNew = false;
        itemMain.group = group;
        itemMain.start(new Stage());
    }
    
    @FXML 
    protected void viewAllItems(ActionEvent event) throws IOException{
        viewItems = new ViewItemsMain();
        viewItems.group = group;
        viewItems.start(new Stage());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
