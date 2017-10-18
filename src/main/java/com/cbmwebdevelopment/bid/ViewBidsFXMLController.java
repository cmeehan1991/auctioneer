/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bid;

import com.cbmwebdevelopment.alerts.Alerts;
import com.cbmwebdevelopment.bid.ViewBidsTableViewController.Bids;
import com.cbmwebdevelopment.items.ItemMain;
import com.cbmwebdevelopment.output.WinningBids;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class ViewBidsFXMLController implements Initializable {

    @FXML
    TableView bidsTableView;

    ViewBidsTableViewController tableViewController = new ViewBidsTableViewController();
    private final ObservableList<Bids> DATA = FXCollections.observableArrayList();
    private Bid bid = new Bid();
    private Timer timer;
    protected Group group;

    @FXML
    protected void refreshFeed(ActionEvent event) {
        refreshTable();
    }
    
    @FXML
    protected void exportData(ActionEvent event) throws IOException, FileNotFoundException, ParseException{
        HashMap<String, HashMap<String, Boolean>> options = new HashMap<>();
        options.put("Send as an Email", new HashMap(){{put("Email the item data to one or multiple recipients.", false);}});
        options.put("Export to Excel File", new HashMap(){{put("Save the item data to an excel file on your machine.", true);}});
        Dialog dialog = new Alerts().commandLinkDialog("Export Data", "Export the winning items.", null, options);
        Optional<ButtonType> result = dialog.showAndWait();
        
        WinningBids winningBids = new WinningBids();
        switch(result.get().getText()){
            case "Export to Excel File":
                winningBids.exportWinningBidsToExcel();
                break;
            case "Send as an Email":
                TextInputDialog recipients = new Alerts().inputAlert("Recipients", "Email Recipients", "To:");
                Optional<String> sendTo = recipients.showAndWait();
                if(sendTo.isPresent()){
                    winningBids.emailList(sendTo.get());
                }
                break;
            default:break;
        }
    }

    public void refreshTable() {
        bid = new Bid();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            ObservableList<Bids> data = bid.getBids();
            if (data != DATA) {
                data.forEach((item) -> {
                    ObservableList<String> newItems = FXCollections.observableArrayList(item.getItemId(), item.getItemName(), item.getBidderName(), item.getAmount());
                    if (!DATA.containsAll(newItems)) {
                        ObservableList<Bids> items = FXCollections.observableArrayList(new Bids(item.getItemId(), item.getItemName(), item.getBidderName(), item.getAmount()));
                        Platform.runLater(() -> {
                            DATA.addAll(items);
                        });
                    }
                });
            }
            executor.shutdown();
        });
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableViewController.data = bid.getBids();
        tableViewController.setTableView(bidsTableView);
        // Set listener for double click on row
        bidsTableView.setRowFactory(tv -> {
            TableRow<Bids> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Bids bids = row.getItem();
                    ItemMain itemMain = new ItemMain();
                    itemMain.group = group;
                    itemMain.isNew = false;
                    itemMain.itemNumber = bids.getItemId();
                    try {
                        itemMain.start(new Stage());
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }
                }
            });
            return row;
        });
    }

}
