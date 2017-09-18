/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bid;

import com.cbmwebdevelopment.bid.ViewBidsTableViewController.Bids;
import com.cbmwebdevelopment.checkout.CheckoutMain;
import com.cbmwebdevelopment.items.ItemMain;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
